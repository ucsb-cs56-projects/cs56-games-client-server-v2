package edu.ucsb.cs56.games.client_server.v2.client.Views;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

/**
 * The lobby panel is a panel which displays currently available games which, when clicked on, will take the user to a
 * new or in-progress copy of that game. Soon, buttons will take players to instruction screens where they can choose
 * a few settings and specify if they want any server, or specifically an empty one to start a new game
 *
 * @author Joseph Colicchio
 * @version for CS56, Choice Points, Winter 2012
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

    public void paintComponent(Graphics g) {
        g.setColor(Color.white);
        g.fillRect(0, 0, getWidth(), getHeight());
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent){
        System.out.println("HI");
//            sendMessage("MSG;/join TicTacToe");
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent){
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent){
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent){
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void mouseExited(MouseEvent mouseEvent){
        //To change body of implemented methods use File | Settings | File Templates.
    }

	public JButton getTicTacToeButton() {
		return ticTacToeButton;
	}

	public void setTicTacToeButton(JButton ticTacToeButton) {
		this.ticTacToeButton = ticTacToeButton;
	}

	public JButton getGomokuButton() {
		return gomokuButton;
	}

	public void setGomokuButton(JButton gomokuButton) {
		this.gomokuButton = gomokuButton;
	}

	public JButton getChessButton() {
		return chessButton;
	}

	public void setChessButton(JButton chessButton) {
		this.chessButton = chessButton;
	}
}