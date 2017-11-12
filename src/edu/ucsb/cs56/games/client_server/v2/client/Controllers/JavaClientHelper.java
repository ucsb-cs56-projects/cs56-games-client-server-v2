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
    }
}
