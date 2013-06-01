package edu.ucsb.cs56.games.client_server.v2.client.Views;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * Two Game Player setup for all basic games
 * 
 * @author Joseph Colicchio
 * @author Adam Ehrlich
 * @version for CS56, Spring 2013
 */
public class TwoPlayerGameViewPanel extends GameViewPanel {
	
	private JPanel menuButtons;
	private JButton playSpecButton;
	private JButton leaveButton;
	private JButton newGameButton;

	TwoPlayerGameViewPanel() {
		super();
		setLayout(new BorderLayout());
		
		playSpecButton = new JButton("Play");
        playSpecButton.setAlignmentX(CENTER_ALIGNMENT);
        
        leaveButton = new JButton("Leave");
        leaveButton.setAlignmentX(CENTER_ALIGNMENT);
        
        newGameButton = new JButton("New Game");
        newGameButton.setAlignmentX(CENTER_ALIGNMENT);
        newGameButton.setEnabled(false);
        
        menuButtons = new JPanel();
        menuButtons.add(playSpecButton);
        menuButtons.add(leaveButton);
        menuButtons.add(newGameButton);
		
		add(BorderLayout.SOUTH, menuButtons);
	}

	public JButton getPlaySpecButton() {
		return playSpecButton;
	}

	public void setPlaySpecButton(JButton playSpecButton) {
		this.playSpecButton = playSpecButton;
	}

	public JButton getLeaveButton() {
		return leaveButton;
	}

	public void setLeaveButton(JButton leaveButton) {
		this.leaveButton = leaveButton;
	}

	public JButton getNewGameButton() {
		return newGameButton;
	}

	public void setNewGameButton(JButton newGameButton) {
		this.newGameButton = newGameButton;
	}

	public JPanel getMenuButtons() {
		return menuButtons;
	}

	public void setMenuButtons(JPanel menuButtons) {
		this.menuButtons = menuButtons;
	}
	
}
