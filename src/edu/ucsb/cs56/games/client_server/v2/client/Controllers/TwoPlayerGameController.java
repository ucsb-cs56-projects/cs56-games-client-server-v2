package edu.ucsb.cs56.games.client_server.v2.client.Controllers;

import edu.ucsb.cs56.games.client_server.v2.Controllers.JavaClient;

/**
 * Two Player Game Controller which should be extended from for all two player games.
 * 
 * @author Joseph Colicchio
 * @author Adam Ehrlich
 * @version for CS56, Spring 2013
 */
public class TwoPlayerGameController {

	protected JavaClient client;
	
	public TwoPlayerGameController(JavaClient client) {
		this.client = client;
	}
	
	/**
	 * Responsible for communicating with the server
	 * 
	 * @param string the message from the server
	 */
	public void handleMessage(String string) { }
	
}
