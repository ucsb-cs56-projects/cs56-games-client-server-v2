package edu.ucsb.cs56.games.client_server.v2.client.Views;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

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
	 *A simple getter function that returns the current inputBox
	 *
	 */
	public JTextField getInputBox() {
		return inputBox;
	}
	/**
	 *Sets the inputBox used for texting in the chat
	 *
	 */
	public void setInputBox(JTextField inputBox) {
		this.inputBox = inputBox;
	}
	/**
	 *A simple getter function to return the Chat's send button
	 *
	 */
	public JButton getSendButton() {
		return sendButton;
	}
	/**
	 *A simple setter function that sets our "send button" for our chat
	 */
	public void setSendButton(JButton sendButton) {
		this.sendButton = sendButton;
	}

}
