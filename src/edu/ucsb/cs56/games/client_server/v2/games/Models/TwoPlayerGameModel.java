//package edu.ucsb.cs56.games.client_server.v2.server.Models;
package edu.ucsb.cs56.games.client_server.v2.games.Models;

/**
 * Basic two player game interface which is to be used to store the main 3 functions that every two
 * player game must implement in order to function properly.
 * 
 * @author Joseph Colicchio
 * @author Adam Ehrlich
 * @version for CS56, Spring 2013
 */
public interface TwoPlayerGameModel {

	/**
	 * Return whether a player has won the game in the current turn yet or not
	 * 
	 * @return has someone won the game
	 */
	public boolean checkWinner();
	
	/**
	 * Is responsible for updating the model as to what is going on.  Usually it is a wise idea
	 * to pass in some string that is parsable by your setState() function to update the variables
	 * in the model.
	 * 
	 * @param state the state of the game
	 */
	public void setState(String state);
	
	/**
	 * Is responsible for getting information about the instance variables in the game using some form
	 * of string parser.  Similar to setState, but is meant to be handled by the Server Controller for
	 * the specified game.
	 * 
	 * @return the current state of the instance variables for the game
	 */
	public String getState();
	
}
