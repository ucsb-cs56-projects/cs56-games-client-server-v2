package edu.ucsb.cs56.games.client_server.v2.server.Controllers;

import java.util.ArrayList;

//import edu.ucsb.cs56.games.client_server.v2.Controllers.JavaServer;

/**
 * an abstract service classification for games which have two players
 * all two-player games should extend this
 *
 * @author Joseph Colicchio
 * @author Adam Ehrlich
 * @version for CS56, Spring 2013
 */
public abstract class TwoPlayerGameController extends ChatController{
    public ClientNetworkController player1;
    public ClientNetworkController player2;
    
    public boolean gameStarted;
    
    public TwoPlayerGameController(int ID, JavaServer server) {
    	super(server);
        clients = new ArrayList<ClientNetworkController>();
        gameStarted = false;
        id = ID;
    }
    
    public abstract void init();
    
    public void updateAll() {
        synchronized (clients) {
            for(int i=0;i<clients.size();i++)
                sendGameState(clients.get(i));
        }
    }

    public void addClient(ClientNetworkController client) {
        synchronized (clients) {
            super.addClient(client);
            sendGameState(client);
        }
    }

    public abstract void playClient(ClientNetworkController client);

    public abstract void specClient(ClientNetworkController client);

    public void removeClient(ClientNetworkController client) {
        specClient(client);
        super.removeClient(client);
    }
    
    public void broadcastDate(String data) {
        synchronized (clients) {
            for(int i=0;i<clients.size();i++)
                clients.get(i).sendMessage(data);
        }
    }

    public abstract void sendGameState(ClientNetworkController client);
}
