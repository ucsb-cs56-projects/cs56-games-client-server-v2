package edu.ucsb.cs56.games.client_server.v2.server.Controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;

//import edu.ucsb.cs56.games.server.Controllers.ChessController;
//import edu.ucsb.cs56.games.server.Controllers.GomokuController;
//import edu.ucsb.cs56.games.server.Controllers.TicTacToeController;
import edu.ucsb.cs56.games.client_server.v2.server.Controllers.ClientNetworkController;
import edu.ucsb.cs56.games.client_server.v2.server.Controllers.LobbyController;
import edu.ucsb.cs56.games.client_server.v2.server.Controllers.ServiceController;
import edu.ucsb.cs56.games.client_server.v2.games.ServerControllers.TicTacToeController;
import edu.ucsb.cs56.games.client_server.v2.server.Views.ServerViewPanel;
import edu.ucsb.cs56.games.client_server.v2.games.ServerControllers.GomokuController;

/**
 * JavaServer is the main server-side application, can be run without gui by using a port number as a single argument
 * on the command line. keeps track of clients connected and broadcasts data to one or multiple clients by setting up
 * clientconnect objects for each of them and keeping a list of users connected.
 * clientconnect handles server-related input from users and, if necessary, can query the server to find users by name
 * or an available, open game service
 *
 * @author David Roster
 * @author Harrison Wang
 * @version for CS56, Spring 2017
 */

//start a java message server that listens for connections to port X and then connects the client 
public class JavaServer extends JavaServerTemplate{
    
    /**
     *start a java message server that listens for connections to port X 
     */
    public static void main(String [] args) {
        if(args.length > 0) {
            try {
                int portNum = Integer.parseInt(args[0]);
                JavaServer javaServer = new JavaServer(true);
                javaServer.connect(portNum);
            } catch(Exception ex) {
                System.out.println("bad port: "+args[0]);
                System.exit(1);
            }
        } else {
            JavaServer JavaServer = new JavaServer(false);
        }
    }

    /**
     * start server on specified port
     * @param port port number to bind to
     */
    public void connect(int port) {
        try {
            URL ipGetter = new URL(" http://api.externalip.net/ip/");
            BufferedReader ip = new BufferedReader(new InputStreamReader(ipGetter.openStream()));
            runningOn = ip.readLine();
            System.out.println(runningOn);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        mainThread = new MainThread(port, this);
        mainThread.start();
    }

    /**
     * gracefully stop server for whatever reason
     */
    public void stop() {
        running = false;
        try {
            Socket socket = new Socket("127.0.0.1",portNum);
        } catch(Exception ex) {
            ex.printStackTrace();
            if(!nogui)
                view.getStatus().setText("Couldn't stop");
        }
    }
    
    public JavaServer(boolean nogui) {
        if(nogui)
            return;
        
    	this.view = new ServerViewPanel(JavaServer.PORT);
    	connectActionListener = new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {                  
            	if(connected) {
                    view.getConnectButton().setText("Start Server");
                    stop();
                } else {
                	view.getConnectButton().setText("Stop Server");
                    connect(Integer.parseInt(view.getPortBox().getText()));
                }
            }
    	};                
    	view.getConnectButton().addActionListener(connectActionListener);   
    }

    /**
     * broadcast data to all clients on server
     * @param string data to send
     */
    public void broadcastMessage(String string) {
        System.out.println("broadcasting... "+string);
        synchronized (clients) {
            for(int i=0;i<clients.size();i++)
                if(clients.get(i) != null)
                    clients.get(i).sendMessage(string);
        }
    }

    /** find a client given a string name
     *
     * @param name name of client
     * @return id of client or -1 if not found
     */
    public int findClientByName(String name) {
        synchronized (clients) {
            for(int i=0;i<clients.size();i++) {
                if(clients.get(i) != null && clients.get(i).client.getName().equalsIgnoreCase(name))
                    return i;
            }
        }

        return -1;
    }
    
    public String findUnusedName() {
        String name = "";
        int id = 0;
        int foundAt;
        synchronized (clients) {
            do {
                name = "User"+id;
                foundAt = findClientByName(name);
                id++;
            } while(foundAt != -1);
        }
        return name;
    }

    /**
     * finds a service by name, or creates a new one if none found
     * @param name name of type of service
     * @param empty if service must be empty
     * @return the id of the server being searched for
     */
    public int findServiceByName(String name, boolean empty) {
        for(int i=0;i<services.size();i++) {
            if(services.get(i) != null && services.get(i).name.equalsIgnoreCase(name))
                if(!empty || services.get(i).clients.size() <= 1)
                    return i;
        }
        
        int serviceType = -1;
        for(int i=0;i<ServiceController.getNumServices();i++) {
            if(ServiceController.getGameType(i).equalsIgnoreCase(name))
                serviceType = i;
        }
        
        if(serviceType == -1)
            return -1;

        int serviceID = services.size();

        ServiceController service = null;
        if(serviceType == 0)
            service = new LobbyController(serviceID, this);
        // XXX fix later
        else if(serviceType == 1)
            service = new TicTacToeController(serviceID, this);
        
        else if(serviceType == 2)
            service = new GomokuController(serviceID, this);
	//originally only had serviceID param
	/*
        else if(serviceType == 3)
            service = new ChessController(serviceID);*/

        if(service == null)
            return -1;

        services.add(service);
        broadcastMessage("NEW;"+serviceType);

//        service.addClient(clients.get(0));
        
        return serviceID;
    }

    //chat convention

    /** ban ip from server
     *
     * @param IP ip to ban
     */
    public void banIP(String IP) {
        for(int i=0;i<bannedList.size();i++) {
            if(IP.equals(bannedList.get(i)))
                return;
        }
        System.out.println("B&: "+IP);
        bannedList.add(IP.split(":")[0]);
    }

    //chat convention

    /**
     * unban an IP
     * @param IP ip to unban
     */
    public void unbanIP(String IP) {
        for(int i=0;i<bannedList.size();i++) {
            if(IP.equals(bannedList.get(i))) {
                bannedList.remove(i);
                return;
            }
        }
    }

    //chat convention

    /**
     * is IP banned?
     * @param IP ip in question
     * @return if IP is banned
     */
    public boolean isBanned(String IP) {
        String ADDR = IP.split(":")[0];
        for(int i=0;i<bannedList.size();i++) {
            if(ADDR.equals(bannedList.get(i)))
                return true;
        }
        return false;
    }

}


//server update thread goes here
