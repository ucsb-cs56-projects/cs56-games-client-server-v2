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

public abstract class JavaClientHelperMethods {
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
    
    public abstract void changeLocation(int L);
    //public abstract void connect(java.lang.String ip, int port);
    public abstract java.util.ArrayList<ClientModel> getClients();
    public abstract int getId();
    //public abstract ClientViewPanel getView();
    //public abstract void init();
    public abstract boolean isConnected();
    public abstract void setConnected(boolean connected);
    public abstract void setId(int id);
    public abstract void sendMessage(java.lang.String string);
    public abstract void handleMessage(java.lang.String string);
    public abstract void updateClients();
    public abstract void updateMessages();
    
    protected void handleMessageCON(String string){
	int pid = Integer.parseInt(string.substring(4));
            System.out.println("Client "+pid+" has connected");
            while(getClients().size() <= pid)
                getClients().add(null);
            if(getClients().get(pid) == null)
                getClients().set(pid, new ClientModel(pid));
            else
                sendMessage("INFO;");
            messages.add(new MessageModel(getClients().get(pid).getName()+" connected", "Server",true,false));
            updateClients();
            updateMessages();
    }
    
    protected void handleMessageDCON(String string){
	String[] data = string.substring(5).split("]");
            int pid = Integer.parseInt(data[0]);
            System.out.println("Client " + pid + " has disconnected: " + data[1]);
            if(getClients().size() > pid && getClients().get(pid) != null) {
                messages.add(new MessageModel(getClients().get(pid).getName() + " disconnected: "+data[1], "Server", true, false));
                getClients().set(pid, null);
            }
            updateClients();
            updateMessages();
            if(pid == getId())
                thread.running = false;
    }
    
    protected void handleMessageMSG(String string){
         String[] data = string.substring(4).split("]");
            int pid = Integer.parseInt(data[0]);
            if(getClients().size() <= pid || getClients().get(pid) == null)
                return;
            String msg = string.substring(4+data[0].length()+1);
            System.out.println("Client "+pid+" said "+msg);
            if(getClients().size() > pid) {
                messages.add(new MessageModel(msg,getClients().get(pid).getName(),false,false));
                updateMessages();
            }
    }

    protected void handleMessagePMSG(String string){
	 String[] data = string.substring(5).split("]");
            int pid = Integer.parseInt(data[0]);
            String msg = string.substring(5+data[0].length()+1);
            System.out.println("Client "+pid+" privately said "+msg);
            if(getClients().size() > pid) {
                messages.add(new MessageModel(msg,getClients().get(pid).getName(), true, false));
                updateMessages();
            }
    }

    protected void handleMessageRMSG(String string){
	String[] data = string.substring(5).split("]");
            int pid = Integer.parseInt(data[0]);
            String msg = string.substring(5+data[0].length()+1);
            if(getClients().size() > pid) {
                messages.add(new MessageModel(msg,getClients().get(pid).getName(),true,true));
                updateMessages();
            }	
    }

    protected void handleMessageSMSG(String string){
	String msg = string.substring(5);
            if(msg != null && msg.length() > 0) {
                messages.add(new MessageModel(msg,"Server",true,false));
                updateMessages();
            }
    }

    protected void handleMessageID(String string){
	setId(Integer.parseInt(string.substring(3)));
            if(name == null)
                name = "User"+getId();

            sendMessage("CON;");
            sendMessage("NAME;"+name);
            sendMessage("INFO;");
            System.out.println(location);
    }
    
    protected void handleMessageALL(String string){
	String[] connected = string.substring(4).split(";");
            for(int i=0;i<connected.length;i++) {
                String[] info = connected[i].split(",");
                if(getClients().size() <= i)
                    getClients().add(null);
                if(connected[i].equals(","))
                    continue;
                if(info[0].equals("")) {
                    if(getClients().get(i) != null)
                        getClients().set(i, null);
                } else {
                    getClients().set(i, new ClientModel(i, info[0], Integer.parseInt(info[1])));
                    if(getId() == i)
                        changeLocation(Integer.parseInt(info[1]));
                }
            }
            //the problem is here, we need to have something else removing the clients from the list and re-adding them
            //otherwise when the thing redraws, it'll freak out
            updateClients();
    }

    protected void handleMessageSERV(String string){
	String[] serv = string.substring(5).split(",");
            for(int i=0;i<serv.length;i++) {
                if(services.size() <= i)
                    services.add(null);
                services.set(i, Integer.parseInt(serv[i]));
            }
            updateClients();
            changeLocation(location);
    }

    protected void handleMessageNAME(String string){
	String[] data = string.substring(5).split("]");
            int pid = Integer.parseInt(data[0]);
            String pname = data[1];
            if(getClients().size() <= pid)
                return;
            if(getClients().get(pid) == null)
                getClients().set(pid, new ClientModel(getId(), pname, 0));
            //messages.add(new edu.ucsb.cs56.W12.jcolicchio.issue535.Message(clients.get(pid).name+" changed his name to "+pname, "Server",true,false,clients.get(0).getColor()));
            getClients().get(pid).setName(pname);
            if(pid == getId())
                name = pname;
            updateClients();
            updateMessages();
    }

    protected void handleMessageMOVED(String string){
	String[] data = string.substring(6).split("]");
            int pid = Integer.parseInt(data[0]);
            getClients().get(pid).setLocation(Integer.parseInt(data[1]));
            if(pid == getId()) {
                changeLocation(getClients().get(getId()).getLocation());
            }
            updateClients();
            updateMessages();
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
