package edu.ucsb.cs56.games.client_server.v2.server.Controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import edu.ucsb.cs56.games.client_server.v2.Controllers.JavaServer;
import edu.ucsb.cs56.games.client_server.v2.Models.ClientModel;

/**
 * Clientconnect is a runnable object representing a connection between the server and a client
 * it provides all functionality for transmitting data to clients and receiving incoming data from them
 *
 * @author Joseph Colicchio
 * @author Adam Ehrlich
 * @version for CS56, Spring 2013
 */

//server-wide convention for managing cilents connected to server
public class ClientNetworkController implements Runnable {
    Socket sock;
    BufferedReader reader;
    PrintWriter writer;

    boolean closed;

    //the client data object
    public ClientModel client;

    public ServiceController currentService;
    
    private JavaServer server;

    //setup

    /**
     * set up the clientconnect with a socket
     * @param clientSocket active connection to server
     */
    public ClientNetworkController(Socket clientSocket, JavaServer server) {
    	this.server = server;
        if(clientSocket == null)
            return;
        if(server.isBanned(clientSocket.getRemoteSocketAddress().toString()))
            closed = true;
        else
            closed = false;
        System.out.println("new thing");
        try {
            sock = clientSocket;
            reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            writer = new PrintWriter(sock.getOutputStream());
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    //life cycle of the client
    public void run() {
        System.out.println("running");
        synchronized (JavaServer.clients) {
            for(int i=0;i<JavaServer.clients.size();i++) {
                if(JavaServer.clients.get(i) == null) {
                    client = new ClientModel(i);
                    JavaServer.clients.set(i,this);
                    break;
                }
            }
            if(client == null) {
                client = new ClientModel(JavaServer.clients.size());
                JavaServer.clients.add(this);
                server.updateServerGUI();
            }
        }

        //tell client what its id is
        sendMessage("ID;"+client.getId());
        currentService = server.getLobby();
        currentService.addClient(this);
        //edu.ucsb.cs56.W12.jcolicchio.issue535.JavaServer.broadcastMessage("CON;"+client.id);

        //tell everyone else, player id has joined

        //edu.ucsb.cs56.W12.jcolicchio.issue535.JavaServer.ActiveGame.addClient(this);

        String str;
        try {
            while(!closed && (str = reader.readLine()) != null) {
                //do something with str
                handleMessage(str);
            }
        } catch(Exception ex) {
            System.out.println("closed? "+closed);
            ex.printStackTrace();
            server.broadcastMessage("DCON["+client.getId()+"]Client crashed!");
        }

        try{
            System.out.println("closing this");
            writer.close();
            reader.close();
            sock.close();
        }catch(IOException e){
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        //tell everyone the client has disconnected
        currentService.removeClient(this);
        System.out.println(closed+" well at least this got called...");
        synchronized (JavaServer.clients) {
            JavaServer.clients.set(client.getId(), null);
            if(client.getId() == JavaServer.clients.size()-1){
                for(int i=client.getId();i>=0;i--) {
                    if(JavaServer.clients.get(i) == null) {
                        JavaServer.clients.remove(i);
                    } else {
                        break;
                    }
                }
            }
        }
        server.updateServerGUI();
    }

    /**
     * handle data from client
     * @param string data from client
     */
    public void handleMessage(String string) {
        //this should send the message to the current service as well

        System.out.println("incoming... "+string);
        //do something with the message from the client
        if(string.indexOf("CON;") == 0) {
            //if incoming is CON;, alert everyone that cilent.id has connected
            server.broadcastMessage("CON;"+client.getId());
        } else if(string.indexOf("DCON;") == 0) {
            //if incoming is DCON;, alert everyone that client.id has disconnected
            disconnect(string.substring(5));
        } else if(string.indexOf("INFO;") == 0) {
            sendServers();
            sendAll();
        } else if(string.indexOf("NAME;") == 0) {
            //change this to just call a function with two parameters
            //it's susceptible to users who try to enter with names that contain ', ], etc
            if(server.findClientByName(string.substring(5)) > -1)
                rename(server.findUnusedName());
            else
                rename(string.substring(5));
        }

        currentService.handleData(this, string);
    }

    /**
     * send all connected user info to client
     */
    public void sendAll() {
        String r = "ALL;";
        synchronized (JavaServer.clients) {
        System.out.println("total users: "+JavaServer.clients.size());
        ClientNetworkController client;
            for(int i=0;i<JavaServer.clients.size();i++) {
                client = JavaServer.clients.get(i);
                if(client == null)
                    r += ",";
                else
                    r += client.client.getName()+","+client.client.getLocation();
                if(i < JavaServer.clients.size()-1)
                    r += ";";
            }
        }
        sendMessage(r);
    }

    /**
     * send all info about services to client
     */
    public void sendServers() {
        String r = "SERV;"+JavaServer.services.get(0).type;
        synchronized (JavaServer.clients) {
            for(int i=1;i<JavaServer.services.size();i++)
                r += ","+JavaServer.services.get(i).type;
        }
        sendMessage(r);
    }

    /**
     * try kick the user off the server with optional message
     * @param message optional message to display to all users
     */
    public void kick(String message) {
        String reason = null;
        int id = -1;
        String clientName;
        String[] data;
        if(message.indexOf(" ") == -1)
            clientName = message;
        else {
            data = message.split(" ");
            clientName = data[0];
            reason = message.substring(data[0].length()+1);
        }

        id = server.findClientByName(clientName);
        if(id < 0) {
            fromServer("Could not find user: "+clientName);
            return;
        }

        ClientNetworkController victim = JavaServer.clients.get(id);
        if(reason == null || reason.equals(""))
            victim.disconnect("Kicked by "+client.getName());
        else
            victim.disconnect("Kicked by "+client.getName()+". Reason: "+reason);
    }

    /**
     * ban a user's ip from connecting to the server
     * @param message option ban message
     */
    public void ban(String message) {
        String[] data = message.split(" ");

        int id = server.findClientByName(data[0]);
        if(id < 0) {
            fromServer("Could not find user: "+data[0]);
            return;
        }
        ClientNetworkController victim = JavaServer.clients.get(id);
        server.banIP(victim.sock.getRemoteSocketAddress().toString());
    }

    /**
     * unban an ip
     * @param message message to display while unbanning
     */
    public void unban(String message) {
        String[] data = message.split(" ");

        int id = server.findClientByName(data[0]);
        if(id < 0) {
            fromServer("Could not find user: "+data[0]);
            return;
        }
        ClientNetworkController victim = JavaServer.clients.get(id);
        server.unbanIP(victim.sock.getRemoteSocketAddress().toString());
    }

    /**
     * kick and ban a user with message
     * @param message message to display
     */
    public void kickBan(String message) {
        kick(message);
        ban(message);
    }

    //quit with optional message

    /** disconnect this client with message
     *
     * @param message message to display
     */
    public void disconnect(String message) {
        closed = true;
        String msg = "DCON["+client.getId()+"]";
        if(message != null)
            msg += message;
        server.broadcastMessage(msg);
    }

    //sends message to client

    /**
     * send message to client, could be any kind of data
     * @param string data to send
     */
    public void sendMessage(String string) {
        if(writer == null)
            return;
        System.out.println("outgoing to "+client.getId()+"... "+string);
        writer.println(string);
        writer.flush();
    }

    /**
     * attempt to rename client
     * @param newName new name to give to client
     */
    public void rename(String newName) {
        if(newName == null || newName.equals(""))
            return;
        if(newName.indexOf(",") != -1
                || newName.indexOf("]") != -1
                || newName.indexOf("/") != -1
                || newName.indexOf(" ") != -1) {
            fromServer("Invalid name: \""+newName+"\", cannot contain ' ', '/', ']' or ','");
            return;
        }
        if(server.findClientByName(newName) >= 0) {
            fromServer("Name \""+newName+"\" already taken!");
            return;
        }

        //this doesn't belong here
        if(!client.getName().equals(""))
            server.broadcastMessage("SMSG;"+client.getName()+" changed name to "+newName);
        client.setName(newName);
        server.broadcastMessage("NAME["+client.getId()+"]"+client.getName());
    }

    /** send a message as the server
     *
     * @param string message to send
     */
    public void fromServer(String string) {
        sendMessage("SMSG;"+string);
    }

    /**
     * promote user to operator status, if possible
     * @param user username
     */
    public void op(String user) {
        if(!client.isOp()) {
            fromServer("You can't make "+user+" OP because you are not OP!");
            return;
        }

        int userID = server.findClientByName(user);
        if(userID != -1)
            server.broadcastMessage("OP;"+userID);
    }
}