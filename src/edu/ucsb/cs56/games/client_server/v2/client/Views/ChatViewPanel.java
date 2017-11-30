package edu.ucsb.cs56.games.client_server.v2.client.Views;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import edu.ucsb.cs56.games.client_server.v2.games.Views.GameViewPanel;

/**
 * Chat panel used in client
 * 
 * @author David Roster
 * @author Harrison wang
 * @version for CS56, Spring 2013
 */
public class ChatViewPanel extends JPanel {
	
	private JTextField inputBox;
    private JButton sendButton;
    
    /**
     *The ChatviewPanel constructor that instantiates an inputBox and sendButton to create the window for the chat service
     *@param SuperConstructor Calls the super constructor to create a new BorderLayout.
     *
     */
    public ChatViewPanel() {
    	super(new BorderLayout());
    	
    	inputBox = new JTextField();
    	sendButton = new JButton("Send");
    	
    	this.add(BorderLayout.EAST, sendButton);
        this.add(BorderLayout.CENTER, inputBox);
    }
	/**
	 *Returns the inputBox which is the textfield the user types in.
	 *
	 */
	public JTextField getInputBox() {
		return inputBox;
	}
	/**
	 *Sets the inputBox used for texting in the chat in the beginning or resizes it based off window size change.
	 *
	 */
	public void setInputBox(JTextField inputBox) {
		this.inputBox = inputBox;
	}
	/**
	 *A getter to see whether the send button for our user typing a message was pressed
	 *
	 */
	public JButton getSendButton() {
		return sendButton;
	}
	/**
	 *A simple setter function that sets our "send button" for our chat box depending on chat application size.

	 */
	public void setSendButton(JButton sendButton) {
		this.sendButton = sendButton;
	}

}
