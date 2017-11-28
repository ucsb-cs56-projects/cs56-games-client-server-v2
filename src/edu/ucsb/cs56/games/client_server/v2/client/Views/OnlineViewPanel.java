package edu.ucsb.cs56.games.client_server.v2.client.Views;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import edu.ucsb.cs56.games.client_server.v2.games.Views.GameViewPanel;

/**
 * The lobby panel is a panel which displays currently available games which, when clicked on, will take the user to a
 * new or in-progress copy of that game. Soon, buttons will take players to instruction screens where they can choose
 * a few settings and specify if they want any server, or specifically an empty one to start a new game
 *
 * @author Joseph Colicchio
 * @author Adam Ehrlich
 * @version for CS56, Spring 2013
 */

public class OnlineViewPanel extends GameViewPanel {
	
	private JButton ticTacToeButton;
	private JButton gomokuButton;
	private JButton chessButton;
	
    public OnlineViewPanel() {
        setLayout(new FlowLayout());
        
        ticTacToeButton = new JButton("TicTacToe");
        gomokuButton = new JButton("Gomoku");
        chessButton = new JButton("Chess");
        
        add(BorderLayout.NORTH, ticTacToeButton);
        add(BorderLayout.NORTH, gomokuButton);
        add(BorderLayout.NORTH, chessButton);
    }
/**
 *One of the most important methods! This paints are corresponding size rectangle to white.
 *
 */
    public void paintComponent(Graphics g) {
        g.setColor(Color.white);
        g.fillRect(0, 0, getWidth(), getHeight());
    }
/** WE override the mouseClicked method to print "HI" when called.
 *
 *
 */
    
    @Override
    public void mouseClicked(MouseEvent mouseEvent){
        System.out.println("HI");
//            sendMessage("MSG;/join TicTacToe");
        //To change body of implemented methods use File | Settings | File Templates.
    }
/**
 *This method is used to see when one of our game buttons is pressed.
 */
    @Override
    public void mousePressed(MouseEvent mouseEvent){
        //To change body of implemented methods use File | Settings | File Templates.
    }
/**
 *This method is used to record when one of the game buttons is released.
 */
    @Override
    public void mouseReleased(MouseEvent mouseEvent){
        //To change body of implemented methods use File | Settings | File Templates.
    }
/**
 *Basic method that gets registered in our Mouse Listener. Monitors what gets entered.
 */
    @Override
    public void mouseEntered(MouseEvent mouseEvent){
        //To change body of implemented methods use File | Settings | File Templates.
    }
/**
 *A method that tracks when a mouse is exited. 
 */

    @Override
    public void mouseExited(MouseEvent mouseEvent){
        //To change body of implemented methods use File | Settings | File Templates.
    }
/**
 *Returns the tictactoe button if clicked. This will take you to the TicTacToe game.
 */

	public JButton getTicTacToeButton() {
		return ticTacToeButton;
	}
/**
 *This creates the TicTacToe button when creating our online view.
 */
	public void setTicTacToeButton(JButton ticTacToeButton) {
		this.ticTacToeButton = ticTacToeButton;
	}
/**
 *This can be used to check whether the Gomoku button was pressed. If so, Gomoku will be played.
 */
	public JButton getGomokuButton() {
		return gomokuButton;
	}
/**
 *This is used to create the Gomuku button. The buttons can't change size.
 */
	public void setGomokuButton(JButton gomokuButton) {
		this.gomokuButton = gomokuButton;
	}
/**
 *Returns if the chess button was pressed. Then the players will play chess. 
 */
	public JButton getChessButton() {
		return chessButton;
	}
/**
 *This is used to initialize the chess button upon creation. This button can't change size.
 */

	public void setChessButton(JButton chessButton) {
		this.chessButton = chessButton;
	}
}
