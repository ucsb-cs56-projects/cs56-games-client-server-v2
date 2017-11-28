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

/**
 * This class exists to provide helper methods for handleMessage() from JavaClient to make it more readable.
 *
 * @author Hong Wang
 * @author David Roster
 * @version for CS56, Fall 2017
 */

public class MessageHandler {  
   
     /** handleMessage is passed a string and the client itself from the JavaClient to handle the message.     
     * @param string the data from the client to handle
     * @param client the client itself
     */
    public static void handleMessage(String string, JavaClient client) {

	if(string.indexOf("CON;") == 0) {
            handleMessageCON(string, client);
        }
	else if(string.indexOf("DCON[") == 0) {
            handleMessageDCON(string, client);
        }
	else if(string.indexOf("MSG[") == 0) {
            handleMessageMSG(string, client);
        }
	else if(string.indexOf("PMSG[") == 0) {
            handleMessagePMSG(string, client);
        }
	else if(string.indexOf("RMSG[") == 0) {
            handleMessageRMSG(string, client);
        }
	else if(string.indexOf("SMSG;") == 0) {
            handleMessageSMSG(string, client);
        }
	else if(string.indexOf("ID;") == 0) {
            handleMessageID(string, client);
        }
	else if(string.indexOf("ALL;") == 0) {
            handleMessageALL(string, client);
        }
	else if(string.indexOf("SERV;") == 0) {
            handleMessageSERV(string, client);
        }
	else if(string.indexOf("NEW;") == 0) {
            client.services.add(Integer.parseInt(string.substring(4)));
        }
	else if(string.indexOf("NAME[") == 0) {
            handleMessageNAME(string, client);
        }
	else if(string.indexOf("MOVED[") == 0) {
            handleMessageMOVED(string, client);
        }
    }

    /**
 *Handle the message for connection.
 */
    private static void handleMessageCON(String string, JavaClient client){
	int pid = Integer.parseInt(string.substring(4));
            System.out.println("Client "+pid+" has connected");
            while(client.getClients().size() <= pid)
                client.getClients().add(null);
            if(client.getClients().get(pid) == null)
                client.getClients().set(pid, new ClientModel(pid));
            else
                client.sendMessage("INFO;");
            client.messages.add(new MessageModel(client.getClients().get(pid).getName()+" connected", "Server",true,false));
            client.updateClients();
            client.updateMessages();
    }

    /**
 *Handle the message for disconnection.
 */
    private static void handleMessageDCON(String string, JavaClient client){
	String[] data = string.substring(5).split("]");
            int pid = Integer.parseInt(data[0]);
            System.out.println("Client " + pid + " has disconnected: " + data[1]);
            if(client.getClients().size() > pid && client.getClients().get(pid) != null) {
                client.messages.add(new MessageModel(client.getClients().get(pid).getName() + " disconnected: "+data[1], "Server", true, false));
                client.getClients().set(pid, null);
            }
            client.updateClients();
            client.updateMessages();
            if(pid == client.getId())
                client.thread.running = false;
    }

    /**
 *Handle the message for public messages.
 */
    private static void handleMessageMSG(String string, JavaClient client){
         String[] data = string.substring(4).split("]");
            int pid = Integer.parseInt(data[0]);
            if(client.getClients().size() <= pid || client.getClients().get(pid) == null)
                return;
            String msg = string.substring(4+data[0].length()+1);
            System.out.println("Client "+pid+" said "+msg);
            if(client.getClients().size() > pid) {
                client.messages.add(new MessageModel(msg,client.getClients().get(pid).getName(),false,false));
                client.updateMessages();
            }
    }

    /**
 *Handle the message for private messages.
 */
    private static void handleMessagePMSG(String string, JavaClient client){
	 String[] data = string.substring(5).split("]");
            int pid = Integer.parseInt(data[0]);
            String msg = string.substring(5+data[0].length()+1);
            System.out.println("Client "+pid+" privately said "+msg);
            if(client.getClients().size() > pid) {
                client.messages.add(new MessageModel(msg,client.getClients().get(pid).getName(), true, false));
                client.updateMessages();
            }
    }

    /**
 *Handle the message for receiving messages.
 */
    private static void handleMessageRMSG(String string, JavaClient client){
	String[] data = string.substring(5).split("]");
            int pid = Integer.parseInt(data[0]);
            String msg = string.substring(5+data[0].length()+1);
            if(client.getClients().size() > pid) {
                client.messages.add(new MessageModel(msg,client.getClients().get(pid).getName(),true,true));
                client.updateMessages();
            }	
    }

    /**
 *Handle the message for sending messages.
 */
    private static void handleMessageSMSG(String string, JavaClient client){
	String msg = string.substring(5);
            if(msg != null && msg.length() > 0) {
                client.messages.add(new MessageModel(msg,"Server",true,false));
                client.updateMessages();
            }
    }

    /**
 *Handle the message for ID.
 */
    private static void handleMessageID(String string, JavaClient client){
	client.setId(Integer.parseInt(string.substring(3)));
            if(client.name == null)
                client.name = "User"+client.getId();

            client.sendMessage("CON;");
            client.sendMessage("NAME;"+client.name);
            client.sendMessage("INFO;");
            System.out.println(client.location);
    }

    /**
 *Handle the message for all.
 */
    private static void handleMessageALL(String string, JavaClient client){
	String[] connected = string.substring(4).split(";");
            for(int i=0;i<connected.length;i++) {
                String[] info = connected[i].split(",");
                if(client.getClients().size() <= i)
                    client.getClients().add(null);
                if(connected[i].equals(","))
                    continue;
                if(info[0].equals("")) {
                    if(client.getClients().get(i) != null)
                        client.getClients().set(i, null);
                } else {
                    client.getClients().set(i, new ClientModel(i, info[0], Integer.parseInt(info[1])));
                    if(client.getId() == i)
                        client.changeLocation(Integer.parseInt(info[1]));
                }
            }
            //the problem is here, we need to have something else removing the clients from the list and re-adding them
            //otherwise when the thing redraws, it'll freak out
            client.updateClients();
    }

    /**
 *Handle the message for services.
 */
    private static void handleMessageSERV(String string, JavaClient client){
	String[] serv = string.substring(5).split(",");
            for(int i=0;i<serv.length;i++) {
                if(client.services.size() <= i)
                    client.services.add(null);
                client.services.set(i, Integer.parseInt(serv[i]));
            }
            client.updateClients();
            client.changeLocation(client.location);
    }

    /**
 *Handle the message for names.
 */
    private static void handleMessageNAME(String string, JavaClient client){
	String[] data = string.substring(5).split("]");
            int pid = Integer.parseInt(data[0]);
            String pname = data[1];
            if(client.getClients().size() <= pid)
                return;
            if(client.getClients().get(pid) == null)
                client.getClients().set(pid, new ClientModel(client.getId(), pname, 0));
            //messages.add(new edu.ucsb.cs56.W12.jcolicchio.issue535.Message(clients.get(pid).name+" changed his name to "+pname, "Server",true,false,clients.get(0).getColor()));
            client.getClients().get(pid).setName(pname);
            if(pid == client.getId())
                client.name = pname;
            client.updateClients();
            client.updateMessages();
    }

    /**
 *Handle the message for moving location.
 */
    private static void handleMessageMOVED(String string, JavaClient client){
	String[] data = string.substring(6).split("]");
            int pid = Integer.parseInt(data[0]);
            client.getClients().get(pid).setLocation(Integer.parseInt(data[1]));
            if(pid == client.getId()) {
                client.changeLocation(client.getClients().get(client.getId()).getLocation());
            }
            client.updateClients();
            client.updateMessages();
    }
       
}
