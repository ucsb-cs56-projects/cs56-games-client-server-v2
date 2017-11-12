package edu.ucsb.cs56.games.client_server.v2.server.Views;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Server View Panel
 * 
 * @author Joseph Colicchio
 * @author Adam Ehrlich
 * @version for CS56, Spring 2013
 */
public class ServerViewPanel {
	
	private JFrame mainFrame;
	private JPanel main;
	private Container container;
	private JTextField portBox;
	private JButton connectButton;
	private JLabel status;

    	/**
	 *The constructor creates a new frame for the server with a textbox to input the server's port number, an exit and open button, and a label recording activity on the server.
	 */
	public ServerViewPanel(int port) {
		mainFrame = new JFrame();
		
		// Add sections we need
	    container = mainFrame.getContentPane();
	    main = new JPanel();
	    container.add(BorderLayout.CENTER, main);
	    
	    portBox = new JTextField();
	    portBox.setText(port + "");
	    main.add(portBox);
	    
	    connectButton = new JButton("Start Server");
	    main.add(connectButton);
	    
	    status = new JLabel();
	    main.add(status);
	    
	    // Put it all together
	    mainFrame.setSize(200,100);
	    mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    mainFrame.setVisible(true);
	}
/**
 *Returns the server application 
 */

	public JFrame getMainFrame() {
		return mainFrame;
	}
/**
 *Resets the server's frame if it is resized or when first created.
 */
	public void setMainFrame(JFrame mainFrame) {
		this.mainFrame = mainFrame;
	}
/**
 *Main is the JPanel where the port input and server button are stored on.
 */
	public JPanel getMain() {
		return main;
	}
/**
 *SetMain is used to modify our panel with widgets on it, however the panel is fixed size
 */
	public void setMain(JPanel main) {
		this.main = main;
	}
/**
 *Conatiner is defined as our Servers' content pane. We are just returning whats on our server application here. 
 */
	public Container getContainer() {
		return container;
	}
/**
 *SetContainer allows us to change the entire look of our server easily. However, it is basically just used to create and setup our container.
 */
	public void setContainer(Container container) {
		this.container = container;
	}
/**
 *Returns the Port ID the user typed into the box
 */
	public JTextField getPortBox() {
		return portBox;
	}
/**
 *Used to reset the port ID box and orginally create it.
 */

	public void setPortBox(JTextField portBox) {
		this.portBox = portBox;
	}
/**
 *We can call this method to see when the server side connect button is pressed - thus creating an open server for clients to join
 */
	public JButton getConnectButton() {
		return connectButton;
	}
/**
 *Allows us to set the dimmensions of our connect butoon and give it a name
 */
	public void setConnectButton(JButton connectButton) {
		this.connectButton = connectButton;
	}
/**
 *Returns the number of users in server along with its activity.
 */
	public JLabel getStatus() {
		return status;
	}
/**
 *Allows us to change the settings based on the flow of game.
 */
	public void setStatus(JLabel status) {
		this.status = status;
	}
}
