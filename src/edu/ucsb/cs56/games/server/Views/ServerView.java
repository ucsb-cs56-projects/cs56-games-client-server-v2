package edu.ucsb.cs56.games.server.Views;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ServerView {
	
	private JFrame mainFrame;
	private JPanel main;
	private Container container;
	private JTextField portBox;
	private JButton connectButton;
	private JLabel status;
	
	public ServerView(int port) {
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

	public JFrame getMainFrame() {
		return mainFrame;
	}

	public void setMainFrame(JFrame mainFrame) {
		this.mainFrame = mainFrame;
	}

	public JPanel getMain() {
		return main;
	}

	public void setMain(JPanel main) {
		this.main = main;
	}

	public Container getContainer() {
		return container;
	}

	public void setContainer(Container container) {
		this.container = container;
	}

	public JTextField getPortBox() {
		return portBox;
	}

	public void setPortBox(JTextField portBox) {
		this.portBox = portBox;
	}

	public JButton getConnectButton() {
		return connectButton;
	}

	public void setConnectButton(JButton connectButton) {
		this.connectButton = connectButton;
	}

	public JLabel getStatus() {
		return status;
	}

	public void setStatus(JLabel status) {
		this.status = status;
	}
}
