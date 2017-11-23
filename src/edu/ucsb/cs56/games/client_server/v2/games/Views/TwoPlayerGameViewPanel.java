package edu.ucsb.cs56.games.client_server.v2.games.Views;

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
	 *Returns whether the spectating button was pressed. 
	 */
	public JButton getPlaySpecButton() {
		return playSpecButton;
	}
	/**
	 *Creates the spectating button that players can click to to into spectating view instead of playing.
	 */
	public void setPlaySpecButton(JButton playSpecButton) {
		this.playSpecButton = playSpecButton;
	}
	/**
	 *Returns whether leave button was clicked. The leave button will cause the player to leave their respected game.
	 */
	public JButton getLeaveButton() {
		return leaveButton;
	}
	/**
	 *Resets the basic leave button when the game was first started. The size of the button stay the same no matter the changing aspect ratio of the window.
	 */
	public void setLeaveButton(JButton leaveButton) {
		this.leaveButton = leaveButton;
	}
	/**
	 *Returns whether the user clicks the new game button, thus creating a new game of that type.
	 */
	public JButton getNewGameButton() {
		return newGameButton;
	}
	/**
	 *Resets the new game button  - typically when it is first created.
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
