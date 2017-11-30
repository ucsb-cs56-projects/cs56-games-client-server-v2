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
 * Refresh thread constantly repaints the application.
 * This class used to be in JavaClient.java, but it was moved into this file for better readability since it was parallel to JavaClient class.
 *
 * @author Hong Wang
 * @author David Roster
 * @version for CS56, Fall 2017
 */

public class RefreshThread extends Thread implements Runnable {
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
