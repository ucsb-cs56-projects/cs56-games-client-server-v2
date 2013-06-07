package edu.ucsb.cs56.games.client_server.v2.server.Controllers;

import edu.ucsb.cs56.games.client_server.v2.Controllers.JavaServer;
import edu.ucsb.cs56.games.client_server.v2.server.Models.TicTacToeModel;

/**
 * gictactoeservice allows clientconnect to communicate with tictactoe game
 *
 * @author Joseph Colicchio
 * @author Adam Ehrlich
 * @version for CS56, Spring 2013
 */
public class TicTacToeController extends TwoPlayerGameController {
    public TicTacToeModel gameData;

    public ClientNetworkController player1;
    public ClientNetworkController player2;

    public boolean gameStarted;

    public TicTacToeController(int ID, JavaServer server) {
        super(ID, server);
        gameData = new TicTacToeModel();
        type = 1;
        name = "TicTacToe";
    }
    
    public void init() {
        gameData.init();
        broadcastData("INIT;");
    }

    public void playClient(ClientNetworkController client) {
        if(player1 == null) {
            player1 = client;
            gameData.player1 = client.client;
        } else if(player2 == null && player1 != client) {
            player2 = client;
            gameData.player2 = client.client;
            gameStarted = true;
            System.out.println("ready to play: "+player1.client.getId()+" vs "+player2.client.getId());
            gameData.init();
        }

        updateAll();
    }

    //if a client was a player, spec him, and then probably stop the game
    public void specClient(ClientNetworkController client) {
        if(player1 != client && player2 != client)
            return;
        if(player1 == client) {
            player1 = player2;
            gameData.player1 = gameData.player2;
            player2 = null;
            gameData.player2 = null;
            gameStarted = false;
            gameData.init();
        }
        if(player2 == client) {
            player2 = null;
            gameData.player2 = null;
            gameStarted = false;
            gameData.init();
        }

        updateAll();
    }

    //get move from player, if it's their turn
    public void handleData(ClientNetworkController client, String string) {
        if(string.indexOf("PLAY;") == 0)
            playClient(client);
        else if(string.indexOf("SPEC;") == 0)
            specClient(client);
        else if(string.indexOf("MSG;") == 0) {
            String message = string.substring(4);
            if(message.indexOf("/play")==0) {
                playClient(client);
            } else if(message.indexOf("/spec") == 0) {
                specClient(client);
            } else if(message.indexOf("/newgame") == 0) {
                if(client == player1 || client == player2)
                    init();
            } else
                super.handleData(client, string);
        }

        if(!gameStarted)
            return;
        System.out.println(gameData.turn+", "+client.client.getId()+", "+player1.client.getId()+":"+player2.client.getId());
        if(gameData.turn == 1 && client != player1)
            return;
        if(gameData.turn == 2 && client != player2)
            return;
        if(string.indexOf("MOVE;") == 0) {
            if(gameData.winner != 0)
                return;
            System.out.println("got move command from "+client.client.getId()+": "+string);
            String[] data = string.substring(5).split(",");
            int X = Integer.parseInt(data[0]);
            int Y = Integer.parseInt(data[1]);

            if(gameData.grid[Y][X] != 0)
                return;

            gameData.grid[Y][X] = gameData.turn;
            broadcastData("MOVE[" + gameData.turn + "]" + X + "," + Y);
            if(gameData.checkWinner())
                broadcastData("WINNER;"+gameData.winner);
            gameData.turn = 3-gameData.turn;
        }
    }

    //this could be done better, just broadcast gameData.getGameState and have that function generate this:
    //wait but that isnt possible
    //sends the state of the game to a player
    public void sendGameState(ClientNetworkController client) {
        if(client == null)
            return;
        synchronized (client) {
            client.sendMessage(gameData.getState());
            String players = "PLAYERS;";
            if(gameData.player1 != null)
                players += gameData.player1.getId();
            else
                players += "-1";
            players += ",";
            if(gameData.player2 != null)
                players += gameData.player2.getId();
            else
                players += "-1";

            client.sendMessage(players);
        }
    }
}