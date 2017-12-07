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

import edu.ucsb.cs56.games.client_server.v2.server.Controllers.ClientNetworkController;
import edu.ucsb.cs56.games.client_server.v2.server.Controllers.LobbyController;
import edu.ucsb.cs56.games.client_server.v2.server.Controllers.ServiceController;
import edu.ucsb.cs56.games.client_server.v2.games.ServerControllers.TicTacToeController;
import edu.ucsb.cs56.games.client_server.v2.server.Views.ServerViewPanel;

/**
 * This abstract class stores the most basic methods(mostly getters and setters) and MainThread class for JavaServer. 
 *All global variables are declared here as well.
 *
 * @author David Roster
 * @author Harrison Wang
 * @version for CS56, Spring 2017
 */

//start a java message server that listens for connections to port X and then connects the client 
public abstract class JavaServerTemplate{
    //this belongs to the server itself, independent of the chat standards
    protected static ArrayList<ClientNetworkController> clients;
    protected static ArrayList<ServiceController> services;
    protected LobbyController lobby;

    protected  boolean running;

    //this could go in either, it's used mostly for the chat
    protected ArrayList<String> bannedList;

    protected static final String IP_ADDR = "127.0.0.1"; // localhost
    protected static final int PORT = 12345;

    protected boolean connected;
    protected MainThread mainThread;
    protected String runningOn;
    protected int portNum;
    protected boolean nogui;
    
    protected ServerViewPanel view = null;
    
    protected ActionListener connectActionListener;
    
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
}


//server update thread goes here
