package edu.ucsb.cs56.games.client_server.v2.client.Controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import edu.ucsb.cs56.games.client_server.v2.Controllers.JavaClient;
import edu.ucsb.cs56.games.client_server.v2.client.Views.TicTacToeViewPanel;

/**
 * Tic Tac Toe Controller responsible for client game handling
 * 
 * @author Joseph Colicchio
 * @author Adam Ehrlich
 * @version for CS56, Spring 2013
 */
public class TicTacToeController extends TwoPlayerGameController {
	
	private TicTacToeViewPanel view;
	boolean isPlaying;
	
	public TicTacToeController(final JavaClient client) {
		super(client);
		
		this.view = new TicTacToeViewPanel(this);
		
		ActionListener playActionListener = new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {                  
            	if(isPlaying) {
                    client.sendMessage("MSG;/spec");
                    view.getPlaySpecButton().setText("Play");
                    isPlaying = false;
                } else {
                    client.sendMessage("MSG;/play");
                    view.getPlaySpecButton().setText("Spectate");
                    isPlaying = true;
                }
            }
    	};                
    	view.getPlaySpecButton().addActionListener(playActionListener);
    	
    	ActionListener leaveActionListener = new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {                  
            	client.sendMessage("MSG;/join lobby");
            }
    	};                
    	view.getLeaveButton().addActionListener(leaveActionListener);
    	
    	ActionListener newGameActionListener = new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {                  
            	client.sendMessage("MSG;/newgame");
            }
    	};                
    	view.getNewGameButton().addActionListener(newGameActionListener);
	}
    
    @Override
    public void handleMessage(String string) {
        System.out.println("handling as tictactoe: "+string);
        if(string.indexOf("INIT;") == 0) {
            view.init();
        } else if(string.indexOf("STATE[") == 0) {
        	String[] info = string.substring(6).split("]");
        	String[] rows = info[1].split(";");
            for(int i=0;i<3;i++) {
                String[] cols = rows[i].split(",");
                for(int j=0;j<3;j++) {
                    view.setGridSpot(i, j, Integer.parseInt(cols[j]));
                }
            }
        } else if(string.indexOf("MOVE[") == 0) {
            String[] data = string.substring(5).split("]");
            int pid = Integer.parseInt(data[0]);
            String[] coords = data[1].split(",");
            int X = Integer.parseInt(coords[0]);
            int Y = Integer.parseInt(coords[1]);

            view.setGridSpot(Y, X, pid);
            view.setTurn(3 - pid);
        } else if(string.indexOf("PLAYERS;") == 0) {
            String[] data = string.substring(8).split(",");
            int pid1 = Integer.parseInt(data[0]);
            int pid2 = Integer.parseInt(data[1]);
            System.out.println(pid1+", "+client.getClients().size());
            if(pid1 >= 0 && pid1 < client.getClients().size()) {
                view.setPlayer1(client.getClients().get(Integer.parseInt(data[0])));
            } else
            	view.setPlayer1(null);
            if(pid2 >= 0 && pid2 < client.getClients().size())
            	view.setPlayer2(client.getClients().get(Integer.parseInt(data[1])));
            else
                view.setPlayer2(null);
            
            if(pid1 == client.getId() || pid2 == client.getId()) {
                isPlaying = true;
                if(view.getPlayer1() != null && view.getPlayer2() != null)
                    view.getNewGameButton().setEnabled(true);
                else
                	view.getNewGameButton().setEnabled(false);
            } else {
                isPlaying = false;
                view.getNewGameButton().setEnabled(false);;
            }
            
            if(isPlaying || view.getPlayer1() == null || view.getPlayer2() == null)
                view.getPlaySpecButton().setEnabled(true);
            else
            	view.getPlaySpecButton().setEnabled(false);
        } else if(string.indexOf("WINNER;")==0) {
            view.setWinner(Integer.parseInt(string.substring(7)));
        }
    }

	public TicTacToeViewPanel getView() {
		return view;
	}

	public void setView(TicTacToeViewPanel view) {
		this.view = view;
	}

	public boolean isPlaying() {
		return isPlaying;
	}

	public void setPlaying(boolean isPlaying) {
		this.isPlaying = isPlaying;
	}

	public JavaClient getClient() {
		return client;
	}

	public void setClient(JavaClient client) {
		this.client = client;
	}

}
