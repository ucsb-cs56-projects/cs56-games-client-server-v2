package edu.ucsb.cs56.games.client_server.v2.client.Controllers;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.SwingUtilities;

import edu.ucsb.cs56.games.client_server.v2.client.Models.ClientModel;
import edu.ucsb.cs56.games.client_server.v2.games.ClientControllers.TicTacToeController;
import edu.ucsb.cs56.games.client_server.v2.games.ClientControllers.TwoPlayerGameController;
import edu.ucsb.cs56.games.client_server.v2.client.Models.MessageModel;
import edu.ucsb.cs56.games.client_server.v2.client.Models.UsernameModel;
import edu.ucsb.cs56.games.client_server.v2.client.Views.ClientViewPanel;
import edu.ucsb.cs56.games.client_server.v2.client.Views.OfflineViewPanel;
import edu.ucsb.cs56.games.client_server.v2.client.Views.OnlineViewPanel;
import edu.ucsb.cs56.games.client_server.v2.server.Controllers.ServiceController;
import edu.ucsb.cs56.games.client_server.v2.games.ClientControllers.GomokuController;

/**
 * JavaClient is the main runnable client-side application, it allows users to connect to a server on a specific port
 * and chat with other connected users, as well as play games like tic tac toe, gomoku, and chess with them
 * it is composed of a user list, a message box, input box and send button for chatting, and a panel area to display
 * the lobby or current game
 *
 * @author Hong Wang
 * @author David Roster
 * @version for CS56, Fall 2017
 */

//start a java message client that tries to connect to a server at localhost:X
public class JavaClient{   

    protected ClientViewPanel view = null;

    protected Socket sock;
    protected InputStreamReader stream;
    protected BufferedReader reader;
    protected PrintWriter writer;

    protected ArrayList<ClientModel> clients;
    protected ArrayList<Integer> services;
    
    protected ArrayList<MessageModel> messages;
    
    protected int id;
    protected String name;
    protected int location;
    
    protected InputReader thread;
    protected RefreshThread refreshThread;
    protected boolean connected;
    
    protected TwoPlayerGameController gameController = null; 
    
    public static void main(String [] args) {
        JavaClient javaClient = new JavaClient();
    }

    public JavaClient() {
    	// XXX Later for chess
        //ResModel.init(this.getClass());
        this.view = new ClientViewPanel();
        
        // Add listeners
        view.getFrame().addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent winEvt) {
                if(thread != null)
                    thread.running = false;
                if(isConnected())
                    sendMessage("DCON;Window Closed");

                System.exit(0);
            }
        });
        
        SendListener listener = new SendListener();
        view.getSouthPanel().getInputBox().addActionListener(listener);
        view.getSouthPanel().getSendButton().addActionListener(listener);
        
	//double click to follow
	MouseListener mouseListener = new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int index = view.getUserList().locationToIndex(e.getPoint());
                    //follow player into game
                    UsernameModel user = (UsernameModel)(view.getListModel().getElementAt(index));
                    if(user != null)
                        sendMessage("MSG;/follow "+user.getName());
                }
            }
        };
        view.getUserList().addMouseListener(mouseListener);
        
        // Allow users to follow friends into game
        ActionListener followActionListener = new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {                  
            	UsernameModel user = (UsernameModel)view.getUserList().getSelectedValue();
                if (user != null)
                   sendMessage("MSG;/follow "+user.getName());
            }
    	};                
    	view.getFollowButton().addActionListener(followActionListener);
    	
    	// Fills the input box with a command to send user a message
        ActionListener messageActionListener = new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {                  
            	UsernameModel user = (UsernameModel)view.getUserList().getSelectedValue();
                if(user == null)
                    return;

                view.getSouthPanel().getInputBox().setText("/msg " + user.getName() + " ");
                view.getSouthPanel().getInputBox().requestFocus();
            }
    	};                
    	view.getMessageButton().addActionListener(messageActionListener);
    	
    	// Default offline mode
    	ActionListener connectActionListener = new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
            	OfflineViewPanel offlinePanel = (OfflineViewPanel) view.getCanvas();
            	connect(offlinePanel.getIp_box().getText(),Integer.parseInt(offlinePanel.getPort_box().getText()));
            }
    	};
    	OfflineViewPanel offlinePanel = (OfflineViewPanel) view.getCanvas();
    	offlinePanel.getConnectButton().addActionListener(connectActionListener);
    	
    	// Add cell renderer
    	JList userList = view.getUserList();
    	userList.setCellRenderer(new MyCellRenderer());
    	view.setUserList(userList);
    	
        //TODO: use the standardized list!!

        location = -1;
    }
/**
 *Creates three new ArrayLists to SetClients, services, and messages.
 */

    public void init() {
        setClients(new ArrayList<ClientModel>());
        services = new ArrayList<Integer>();
        messages = new ArrayList<MessageModel>();
    }

    /** updateClients updates the client list with the names and locations of everyone on the server
     * should be called whenever a user joins, leaves, or changes locations
     */
    public void updateClients() {
        SwingUtilities.invokeLater(
            new Runnable() {
                public void run() {
                    synchronized (getClients()) {
                    	DefaultListModel listModel = view.getListModel();
                        listModel.clear();
                        if(location < 0)
                            return;
                        listModel.addElement(new UsernameModel(name,null,2));
                        for(int i=getClients().size()-1;i>=0;i--) {
                            ClientModel client = getClients().get(i);
                            if(client != null) {
                                if(client.getId() == getId())
                                    continue;
                                if(client.getLocation() == location || services.size() <= client.getLocation())
                                    listModel.insertElementAt((new UsernameModel(client.getName(),null,0)),1);
                                else {
//                                    System.out.println(client.location+", "+serviceList.size()+", "+services.size());
                                    listModel.addElement(new UsernameModel(client.getName()," ("+client.getLocation()+":"+ServiceController.getGameType(services.get(client.getLocation()))+")",1));
                                }
                            }
                        }
                    }
                }
            }
        );
    }

    /** updateMessages updates the message box, and then scrolls down to the bottom to see the most recent
     * message. should be called whenever a new message is received
     */
    public void updateMessages() {
        SwingUtilities.invokeLater(
            new Runnable() {
                public void run() {
                    String content = "";
                    for(int i=0;i<messages.size();i++) {
                        content += messages.get(i).toString() + "<br>";
                    }
                    view.getOutputBox().setText(content);
                    int caret = view.getOutputBox().getDocument().getLength()-1;
                    if(caret > 0)
                    	view.getOutputBox().setCaretPosition(caret);
                }
            }
        );
    }

    /** connect is called when the player enters an IP and port number, and clicks connect
     * it attempts to connect the player to the associated running server if it exists
     * @param ip - the ip address string to connect to
     * @param port - the port number
     */
    public void connect(String ip, int port) {
        if(isConnected())
            return;
        try {
            System.out.println("Connecting to "+ip+":"+port);
            sock = new Socket(ip,port);
            System.out.println("Connected");
            setConnected(true);
            init();
            stream = new InputStreamReader(sock.getInputStream());
            reader = new BufferedReader(stream);
            writer = new PrintWriter(sock.getOutputStream());
            sendMessage("ACKNOWLEDGE ME!");
            thread = new InputReader();
            thread.start();
            refreshThread = new RefreshThread(this);
            refreshThread.start();
        } catch(IOException ex) {
            ex.printStackTrace();
            System.out.println("unable to connect");
            //System.out.println("quitting...");
            //System.exit(1);
        }
    }

    //public void update() {
    //    for(int i=0;i<clients.size();i++)
    //        if(clients.get(i) != null)
    //            clients.get(i).update();
    //}

    /** handleMessage is passed a string which has been sent from the server
     * it attempts to resolve the request but may forward it to the active game panel, if applicable
     * it manages things like users connecting, disconnecting, receiving private messages, nick changes, etc
     * whereas the game panel handles data regarding the current game
     * @param string the data from the server to handle
     */
    public void handleMessage(String string) {
	MessageHandler.handleMessage(string, this);       
        if (gameController != null)
        	gameController.handleMessage(string);
    }

    
    /** changes the location of the client, in order to generate a service panel associated with
     * that location to start interacting with the specified service
     * @param L the service id number
     */
    public void changeLocation(int L) {
        if(location == L)
            return;
        location = L;
        if(location == -1) {
            view.setCanvasRef(new OfflineViewPanel());
        } else {
            int serviceType = services.get(location);
            if(serviceType == 0) {
            	OnlineViewPanel tmp = new OnlineViewPanel();
	            ActionListener joinActionListener = new ActionListener() {
	            	public void actionPerformed(ActionEvent actionEvent) { 
	            	    // XXX fix me
	            		String tst = actionEvent.getActionCommand();
	            		sendMessage("MSG;/join " + tst);
	            	}
	        	};
	        	tmp.getTicTacToeButton().addActionListener(joinActionListener);
	        	tmp.getGomokuButton().addActionListener(joinActionListener);
	        	tmp.getChessButton().addActionListener(joinActionListener);
	        	view.setCanvasRef(tmp);
            }
            else if(serviceType == 1) {
            	gameController = new TicTacToeController(this);
                view.setCanvasRef(((TicTacToeController)gameController).getView());
            }
            else if(serviceType == 2){
		gameController = new GomokuController(this);
                view.setCanvasRef(((GomokuController)gameController).getView());
		// CanvasRef = new GomokuViewPanel();
	    //hopefully this works
	    }
	    /*
            else if(serviceType == 3)
                canvasRef = new ChessViewPanel();*/
        }

        SwingUtilities.invokeLater(
            new Runnable() {
                public void run() {
                    messages = new ArrayList<MessageModel>();
                    view.updateCavnasPanel();
                }
            }
        );
    }

    /** sends a message to the server, which might be a request for information, game data,
     * or a literal message to be broadcast to all users in the message box
     * @param string a string of data to send to the server
     */
    public void sendMessage(String string) {
        writer.println(string);
        writer.flush();
    }

    public ArrayList<ClientModel> getClients() {
		return clients;
	}

    public void setClients(ArrayList<ClientModel> clients) {
		this.clients = clients;
	}

    public int getId() {
		return id;
	}

    public void setId(int id) {
		this.id = id;
	}

    public boolean isConnected() {
		return connected;
	}

    public void setConnected(boolean connected) {
		this.connected = connected;
	}

    public ClientViewPanel getView() {
		return this.view;
	}

    //Classes within the class    
    /** listens for the send button's action and sends a message, if connected
     *
     */
    class SendListener implements ActionListener {
        public SendListener() {

        }

        public void actionPerformed(ActionEvent event) {
            String message = view.getSouthPanel().getInputBox().getText();
            if(message.length() == 0)
                return;

            view.getSouthPanel().getInputBox().setText("");
            if(isConnected()) {
                sendMessage("MSG;"+message);
            }
        }
    }

    /** input reader waits for data from the server and forwards it to the client
     *
     */
    class InputReader extends Thread implements Runnable {
        public boolean running;
        public void run() {
            String line;
            running = true;
            try {
                while(running && (line = reader.readLine()) != null) {
                    System.out.println("incoming... "+line);
                    handleMessage(line);
                }
            } catch(SocketException ex) {
                ex.printStackTrace();
                System.out.println("lost connection to server...");
            } catch(Exception ex) {
                ex.printStackTrace();
                System.out.println("crashed for some other reason, disconnecting...");
                writer.println("DCON;"+getId());
                writer.flush();
            }

            try{
                sock.close();
            }catch(IOException e){
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            setConnected(false);
            view.getOutputBox().setText("");
            updateClients();
            changeLocation(-1);
            System.out.println("quitting, cause thread ended");
            //System.exit(0);
        }
    }
}


