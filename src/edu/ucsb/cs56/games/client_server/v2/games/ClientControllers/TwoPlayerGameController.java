package edu.ucsb.cs56.games.client_server.v2.games.ClientControllers;

import edu.ucsb.cs56.games.client_server.v2.client.Controllers.JavaClient;

/**
 * Two Player Game Controller which should be extended from for all two player games.
 * 
 * @author David Roster
 * @author Hong Wang
 * @version for CS56, Fall 2017
 */
public class TwoPlayerGameController {

	protected JavaClient client;
	
	/**
	 *TwoPlayerGameController constructor is responsible for redefining the client
	 with the desired new client for any two-player game
	 *@param -  JavaClient client
	 *
	*/
	public TwoPlayerGameController(JavaClient client) {
		this.client = client;
	}
	
	/**
	 * Responsible for communicating with the server
	 * 
	 * @param string the message from the server
	 * @return empty function
	 */
	public void handleMessage(String string) { }
	
}
