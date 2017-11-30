package edu.ucsb.cs56.games.client_server.v2.games.ServerControllers;

import java.util.ArrayList;

import edu.ucsb.cs56.games.client_server.v2.server.Controllers.JavaServer;
import edu.ucsb.cs56.games.client_server.v2.server.Controllers.ClientNetworkController;
import edu.ucsb.cs56.games.client_server.v2.server.Controllers.ChatController;

/**
 * an abstract service classification for games which have two players
 * all two-player games should extend this
 *
 * @author David Roster
 * @author Harrison Wang
 * @version for CS56, Spring 2017
 */
public abstract class TwoPlayerGameController extends ChatController{
    public ClientNetworkController player1;
    public ClientNetworkController player2;
    
    public boolean gameStarted;
     /**
     *Creates the beginning of the basic two player game.
     */
    public TwoPlayerGameController(int ID, JavaServer server) {
    	super(server);
        clients = new ArrayList<ClientNetworkController>();
        gameStarted = false;
        id = ID;
    }
    /**
     *Our basic initial method that each game will extend and prepare the basics for the start of each game.
     */
    public abstract void init();
        /**
     *Updates all Clients when their positioning is changed within the lobby. Uses a synchronized method.
     */
    public void updateAll() {
        synchronized (clients) {
            for(int i=0;i<clients.size();i++)
                sendGameState(clients.get(i));
        }
    }
    /**
     *Adds a potential client to our new game by sending the clients Game state
     */
    public void addClient(ClientNetworkController client) {
        synchronized (clients) {
            super.addClient(client);
            sendGameState(client);
        }
    }
    /**
     *Abstract method that must be implemented by each new game.
     */
    public abstract void playClient(ClientNetworkController client);
    /**
     *Abstract method that must be implemented by each new game.
     */
    public abstract void specClient(ClientNetworkController client);
    /**
     *General functionality for removing a client from its respected game.
     */
    public void removeClient(ClientNetworkController client) {
        specClient(client);
        super.removeClient(client);
    }
    /**
     *Broadcasts each of the clients messages and data through a basic for loop of total clients.
     */
    public void broadcastDate(String data) {
        synchronized (clients) {
            for(int i=0;i<clients.size();i++)
                clients.get(i).sendMessage(data);
        }
    }

    public abstract void sendGameState(ClientNetworkController client);
}
