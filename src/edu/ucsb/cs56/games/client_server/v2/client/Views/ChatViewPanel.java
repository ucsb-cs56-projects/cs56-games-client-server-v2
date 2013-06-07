package edu.ucsb.cs56.games.client_server.v2.client.Views;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Chat panel used in client
 * 
 * @author Joseph Colicchio
 * @author Adam Ehrlich
 * @version for CS56, Spring 2013
 */
public class ChatViewPanel extends JPanel {
	
	private JTextField inputBox;
    private JButton sendButton;
    
    public ChatViewPanel() {
    	super(new BorderLayout());
    	
    	inputBox = new JTextField();
    	sendButton = new JButton("Send");
    	
    	this.add(BorderLayout.EAST, sendButton);
        this.add(BorderLayout.CENTER, inputBox);
    }

	public JTextField getInputBox() {
		return inputBox;
	}

	public void setInputBox(JTextField inputBox) {
		this.inputBox = inputBox;
	}

	public JButton getSendButton() {
		return sendButton;
	}

	public void setSendButton(JButton sendButton) {
		this.sendButton = sendButton;
	}

}
