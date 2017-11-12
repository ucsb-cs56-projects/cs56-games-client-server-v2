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
import edu.ucsb.cs56.games.client_server.v2.client.Controllers.TicTacToeController;
import edu.ucsb.cs56.games.client_server.v2.client.Controllers.TwoPlayerGameController;
import edu.ucsb.cs56.games.client_server.v2.client.Models.MessageModel;
import edu.ucsb.cs56.games.client_server.v2.client.Models.UsernameModel;
import edu.ucsb.cs56.games.client_server.v2.client.Views.ClientViewPanel;
import edu.ucsb.cs56.games.client_server.v2.client.Views.OfflineViewPanel;
import edu.ucsb.cs56.games.client_server.v2.client.Views.OnlineViewPanel;
import edu.ucsb.cs56.games.client_server.v2.server.Controllers.ServiceController;

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
public abstract class JavaClientProto{
    
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
    
    //protected InputReader thread;
    protected RefreshThread refreshThread;
    protected boolean connected;
    
    protected TwoPlayerGameController gameController = null;
    
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

    public abstract void connect(String ip, int port);


    //public void update() {
    //    for(int i=0;i<clients.size();i++)
    //        if(clients.get(i) != null)
    //            clients.get(i).update();
    //}

    public abstract void changeLocation(int L);

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

   

/** renders usernames with bold or italics
 * useful when a user is in another location
 * or to highlight the client's username
 */
class MyCellRenderer extends DefaultListCellRenderer {

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        UsernameModel user = (UsernameModel)value;
        if (user.getStyle() == 2) {// <= put your logic here
            c.setFont(c.getFont().deriveFont(Font.BOLD));
        } else if(user.getStyle() == 1) {
            c.setFont(c.getFont().deriveFont(Font.ITALIC));
        } else {
            c.setFont(c.getFont().deriveFont(Font.PLAIN));
        }
        return c;
    }
}

/**
 * refresh thread constantly repaints the application
 */
class RefreshThread extends Thread implements Runnable {
    public boolean running;
    JavaClient javaClient;
    public RefreshThread(JavaClient client) {
        running = false;
        javaClient = client;
    }

    public void run() {
        running = true;
        while(running) {
            //javaClient.update();
            SwingUtilities.invokeLater(
                    new Runnable() {
                        public void run() {
                            javaClient.getView().getCanvas().repaint();
                        }
                    }
            );
            try {
                Thread.sleep(50);
            } catch(InterruptedException e) {
                e.printStackTrace();
                System.out.println("refresh thread broke");
            }
        }
    }}}
