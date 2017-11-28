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
public class JavaServer{
    //this belongs to the server itself, independent of the chat standards
    public static ArrayList<ClientNetworkController> clients;
    public static ArrayList<ServiceController> services;
    private LobbyController lobby;

    private  boolean running;

    //this could go in either, it's used mostly for the chat
    private ArrayList<String> bannedList;

    private static final String IP_ADDR = "127.0.0.1"; // localhost
    private static final int PORT = 12345;

    private boolean connected;
    private MainThread mainThread;
    private String runningOn;
    private int portNum;
    private boolean nogui;
    
    private ServerViewPanel view = null;
    
    private ActionListener connectActionListener;
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
     * update gui with number of clients
     */
    public void updateServerGUI() {
        if(nogui)
            return;
        if(running)
            view.getStatus().setText(runningOn+", "+clients.size()+" user"+(clients.size()!=1?"s":""));
        else
            view.getStatus().setText("Offline");
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
        /*
        else if(serviceType == 2)
            service = new GomokuController(serviceID);
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

    /** thread to prevent gui from freezing on connect
     *
     */
    class MainThread extends Thread implements Runnable {
    	JavaServer server;
        public MainThread(int P, JavaServer server) {
        	this.server = server;
            portNum = P;
        }
	/**
         *Takes a potential client that wants to connect and runs it in a thread
	 */
        public void run() {
            running = true;
            clients = new ArrayList<ClientNetworkController>();
            bannedList = new ArrayList<String>();

            services = new ArrayList<ServiceController>();
            lobby = new LobbyController(0, server);
            services.add(lobby);

            //clients.add(new edu.ucsb.cs56.W12.jcolicchio.issue535.EchoConnect(clients.size()));
            //clients.add(new edu.ucsb.cs56.W12.jcolicchio.issue535.ShoutConnect(clients.size()));
            ServerSocket serverSock = null;
            Socket sock = null;
            System.out.println("total users: "+clients.size());
            try {
                connected = true;
                serverSock = new ServerSocket(portNum);
                
                while(running) {
                    //a new client wants to connect
                    System.out.println("waiting for next connection...");
                    updateServerGUI();
                    sock = serverSock.accept();
                    if(!running) {
                        updateServerGUI();
                        sock.close();
                        break;
                    }

                    System.out.println("incoming connecting...");
                    //give them a client object, run it in a thread
                    ClientNetworkController conn = new ClientNetworkController(sock, server);
                    Thread thread = new Thread(conn);
                    thread.start();
                    System.out.println("thread started");
                }
            } catch(IOException ex) {
                if(!nogui) {
                    view.getStatus().setText("Port already taken");
                    view.getConnectButton().setText("Start Server");
                }
                ex.printStackTrace();
                System.out.println("requested port already taken. quitting...");
            }
            try {
                for(int i=0;i<clients.size();i++)
                    if(clients.get(i) != null)
                        clients.get(i).disconnect("Server stopping");
                int left = 0;
                do {
                    left = 0;
                    for(int i=0;i<clients.size();i++) {
                        if(clients.get(i) != null)
                            left++;
                    }
                    Thread.sleep(50);
                } while(left > 0);
                serverSock.close();
            } catch(Exception ex) {
                ex.printStackTrace();
            }
            connected = false;
        }
    }
    /**
     *Returns an instance of our LobbyController class that is in charge of users
     */
	public LobbyController getLobby() {
		return lobby;
	}
    /**
     *Returns are modified lobby when a new user connects or an user disconnects
     */
	public void setLobby(LobbyController lobby) {
		this.lobby = lobby;
	}
    /**
     *Returns the value of our server thread to see if it is still active
     */
	public boolean isRunning() {
		return running;
	}
    /**
     *Used to create thread that our server runs on to connect with potential clients
     */
	public void setRunning(boolean running) {
		this.running = running;
	}
    /**
     *Returns an ArrayList of users banned from Lobby. THis could be because of offensive language, unfair gameplay, ect.
     */
	public ArrayList<String> getBannedList() {
		return bannedList;
	}
    /**
     *Modifies our list of banned clients by either potentially removing or adding clients
     */
	public void setBannedList(ArrayList<String> bannedList) {
		this.bannedList = bannedList;
	}
    /**
     *Returns whether a potential client has connected to the server
     */
	public boolean isConnected() {
		return connected;
	}
    /**
     *Allows to set a client with the server to play games
     */
	public void setConnected(boolean connected) {
		this.connected = connected;
	}
    /**
     *Returns the main thread our server runs on
     */
	public MainThread getMainThread() {
		return mainThread;
	}
    /**
     *Sets the main thread of our server when the server is first started
     */
	public void setMainThread(MainThread mainThread) {
		this.mainThread = mainThread;
	}
    /**
     *Finds out which thread client is running on
     */
	public String getRunningOn() {
		return runningOn;
	}
    /**
     *Can reset a client's thread if there is trouble connecting
     */
	public void setRunningOn(String runningOn) {
		this.runningOn = runningOn;
	}
    /**
     *Gets the port number of the server
     */
	public int getPortNum() {
		return portNum;
	}
    /**
     *Can redefine the port number of the server
     */
	public void setPortNum(int portNum) {
		this.portNum = portNum;
	}
    /**
     *Checks to see if there is a GUI windown for the server. The server can still run via command line.
     */
	public boolean isNogui() {
		return nogui;
	}
    /**
     *Can change the potential server gui window - mostly used for creation of the server window primarily
     */
	public void setNogui(boolean nogui) {
		this.nogui = nogui;
	}
    /**
     *Returns server view panel which contains the port number box and connect button for the server application.
     */
	public ServerViewPanel getView() {
		return view;
	}
    /**
     *Sets the view for the server application which is the little window
     */
	public void setView(ServerViewPanel view) {
		this.view = view;
	}
    /**
     *Returns the LIstener we are using to hear calls made by our widgets
     */
	public ActionListener getConnectActionListener() {
		return connectActionListener;
	}
    /**
     *creates an action listener to listen for transactions between server and its widgets
     */
	public void setConnectActionListener(ActionListener connectActionListener) {
		this.connectActionListener = connectActionListener;
	}
    /**
     *Returns the ip address of the server, in our case this is the local host ip
     */
	public static String getIpAddr() {
		return IP_ADDR;
	}
    /**
     *Gets the port number that are server is using.
     */
	public static int getPort() {
		return PORT;
	}
}


//server update thread goes here
