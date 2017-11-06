package edu.ucsb.cs56.games.client_server.v2.client.Views;

import javax.swing.*;

import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * offline panel provides two textfields for IP address and port, and a connect button
 * if the ip and port combination are invalid, it'll stall for a few seconds while it times out
 * in the future, i'd like to have a thread do this, so the gui doesn't freeze with the connect button clicked
 *
 * @author Harrison Wang
 * @author David Roster
 * @version for CS56, Spring 2017
 */

//TODO: make new thread when connect is clicked, which is resolved as soon as the connection is made, but does not freeze the gui while waiting
public class OfflineViewPanel extends GameViewPanel {
	
    private JTextField ip_box;
    private JTextField port_box;
    private JButton connectButton;
    private JPanel connect_panel;
    /**
     *Constructor defines the ip_box , port_box , connectButoon , and connect_panel
     */
    public OfflineViewPanel() {
        setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        
        ip_box = new JTextField("127.0.0.1");
        ip_box.setPreferredSize(new Dimension(100,22));
        ip_box.setAlignmentY(JTextField.CENTER_ALIGNMENT);
        
        port_box = new JTextField("12345");
        port_box.setPreferredSize(new Dimension(45,22));
        port_box.setAlignmentY(JTextField.CENTER_ALIGNMENT);
        
        connectButton = new JButton("Connect");
        connectButton.setAlignmentY(JButton.CENTER_ALIGNMENT);
        
        connect_panel = new JPanel();
        connect_panel.add(ip_box);
        connect_panel.add(port_box);
        connect_panel.add(connectButton);
        
        add(Box.createVerticalGlue());
        add(connect_panel);
        ip_box.requestFocus();
    }
	/**
	 *Mouse is clicked
	 */
    @Override
    public void mouseClicked(MouseEvent mouseEvent){}
	/**
	 *Mouse is pressed
	 */
    @Override
    public void mousePressed(MouseEvent mouseEvent){}
	/**
	 *Mouse is released
	 */
    @Override
    public void mouseReleased(MouseEvent mouseEvent){}
	/**
	 *Mouse is entered
	 */
    @Override
    public void mouseEntered(MouseEvent mouseEvent){}
	/**
	 *Mouse is exited
	 */
    @Override
    public void mouseExited(MouseEvent mouseEvent){}
	/**
	 *Getter function returning ip address in client box
	 */
	public JTextField getIp_box() {
		return ip_box;
	}
	/**
	 *Setter function changes client's ip address in box
	 */
	public void setIp_box(JTextField ip_box) {
		this.ip_box = ip_box;
	}
	/**
	 *Returns Port ID in client box
	 */
	public JTextField getPort_box() {
		return port_box;
	}
	/**
	 *Setter function redefines port id in client box
	 */
	public void setPort_box(JTextField port_box) {
		this.port_box = port_box;
	}
	/**
	 *Returns the connect button
	 */
	public JButton getConnectButton() {
		return connectButton;
	}
	/**
	 *Setter function that redefines a new connect button
	 */
	public void setConnectButton(JButton connectButton) {
		this.connectButton = connectButton;
	}
	/**
	 *Getter returns the clients connect panel
	 */
	public JPanel getConnect_panel() {
		return connect_panel;
	}
	/**
	 *Setter redefines the clients connect panel
	 */
	public void setConnect_panel(JPanel connect_panel) {
		this.connect_panel = connect_panel;
	}
}
