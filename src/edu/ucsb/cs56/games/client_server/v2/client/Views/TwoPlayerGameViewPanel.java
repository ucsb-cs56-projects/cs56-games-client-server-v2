package edu.ucsb.cs56.games.client_server.v2.client.Views;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * Two Game Player setup for all basic games
 * 
 * @author David Roster
 * @author Harrison Wang
 * @version for CS56, Spring 2017
 */
public class TwoPlayerGameViewPanel extends GameViewPanel {
	
	private JPanel menuButtons;
	private JButton playSpecButton;
	private JButton leaveButton;
	private JButton newGameButton;
	
	/**
	 *Constructor that initializes basic two player buttons like play, leave, and new game.
	 */
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
	/**
	 *Returns whose spectating
	 */
	public JButton getPlaySpecButton() {
		return playSpecButton;
	}
	/**
	 *Changes whose spectating
	 */
	public void setPlaySpecButton(JButton playSpecButton) {
		this.playSpecButton = playSpecButton;
	}
	/**
	 *Returns whether leave button was clicked
	 */
	public JButton getLeaveButton() {
		return leaveButton;
	}
	/**
	 *Resets the basic leave button
	 */
	public void setLeaveButton(JButton leaveButton) {
		this.leaveButton = leaveButton;
	}
	/**
	 *Returns the basic new game button
	 */
	public JButton getNewGameButton() {
		return newGameButton;
	}
	/**
	 *Resets the new game button
	 */
	public void setNewGameButton(JButton newGameButton) {
		this.newGameButton = newGameButton;
	}
	/**
	 *returns our basic two game player buttons in the menu
	 */
	public JPanel getMenuButtons() {
		return menuButtons;
	}
	/**
	 *Resets the menu buttons for our basic two player design
	 */
	public void setMenuButtons(JPanel menuButtons) {
		this.menuButtons = menuButtons;
	}
	
}
