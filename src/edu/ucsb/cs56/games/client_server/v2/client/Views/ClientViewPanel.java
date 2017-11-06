package edu.ucsb.cs56.games.client_server.v2.client.Views;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import edu.ucsb.cs56.games.client_server.v2.Controllers.JavaServer;

/**
 * Main Client Panel
 * 
 * @author Harrison Wang
 * @author David Roster
 * @version for CS56, Spring 2017
 */
public class ClientViewPanel implements KeyListener {

	private JFrame frame;
    private Container container;
    private GameViewPanel canvas;//the actual canvas currently being used by the gui
    private GameViewPanel canvasRef;//a reference to the current canvas being used by the game logic
    private ChatViewPanel southPanel;
    private JPanel menuPanel;
    private JPanel userPanel;
    
    private JEditorPane outputBox;
    
    private JList userList;
    private DefaultListModel listModel;
    private JScrollPane userScroll;
    private JButton followButton;
    private JButton messageButton;

    private boolean[] Keys;
    
    public ClientViewPanel() {
    	frame = new JFrame("Java Games Online");
        frame.setSize(640, 512);
        frame.setMinimumSize(new Dimension(480,512));
        
        container = frame.getContentPane();
        
        // Offline View
        canvas = new OfflineViewPanel();
        canvasRef = canvas;
        container.add(BorderLayout.CENTER,canvas);

        southPanel = new ChatViewPanel();
        container.add(BorderLayout.SOUTH, southPanel);
        
        southPanel.setFocusable(true);
        canvas.setFocusable(true);

        canvas.addKeyListener(this);
        canvas.addMouseListener(canvas);
        southPanel.getInputBox().addKeyListener(this);

        listModel = new DefaultListModel();

        userList = new JList(listModel);
        userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        userList.setLayoutOrientation(JList.VERTICAL);
        userList.setVisibleRowCount(-1);

        userScroll = new JScrollPane(userList);
        userScroll.setAlignmentX(JScrollPane.LEFT_ALIGNMENT);
        userScroll.setPreferredSize(new Dimension(160,100));
        userPanel = new JPanel();
        userPanel.setLayout(new BorderLayout());
        userPanel.add(BorderLayout.CENTER,userScroll);
        followButton = new JButton("Follow");
        messageButton = new JButton("Message");
        
        menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel,BoxLayout.X_AXIS));
        menuPanel.add(followButton);
        menuPanel.add(Box.createHorizontalGlue());
        menuPanel.add(messageButton);
        userPanel.add(BorderLayout.SOUTH,menuPanel);  
        container.add(BorderLayout.WEST, userPanel);

        outputBox = new JEditorPane("text/html", "");
        JScrollPane outputScroll = new JScrollPane(outputBox);
        outputBox.setEditable(false);
        southPanel.add(BorderLayout.NORTH, outputScroll);
        outputScroll.setPreferredSize(new Dimension(100, 100));

        frame.setVisible(true);
        
        Keys = new boolean[255];
        for(int i=0;i<255;i++)
            Keys[i] = false;
    }
    /**
     *updateCanvasPanel removes the canvas linked with the gui and replaces with a refernce to the canvas for game logic.
     *
     *
     */
    public void updateCavnasPanel() {
    	container.remove(canvas);
        canvas = canvasRef;
        container.add(BorderLayout.CENTER, canvas);
        canvas.addMouseListener(canvas);
        container.validate();
    }
	/**
	 *No Implementation
	 */
	@Override
    public void keyTyped(KeyEvent keyEvent){ }
	/**
	 *Sets the Keys array to true when clicked upon
	 */
    @Override
    public void keyPressed(KeyEvent keyEvent){
        Keys[keyEvent.getKeyCode()] = true;
    }
	/**
	 *Sets the Keys array to false when released
	 */
    @Override
    public void keyReleased(KeyEvent keyEvent){
        Keys[keyEvent.getKeyCode()] = false;
    }
	/**
	 *Getter function used to return JFrame
	 */
	public JFrame getFrame() {
		return frame;
	}
	/**
	 *Setter function that redefines our existing JFrame
	 */
	public void setFrame(JFrame frame) {
		this.frame = frame;
	}
	/**
	 *Getter Function that returns the Frame's content panel
	 */
	public Container getContainer() {
		return container;
	}
	/**
	 *Redefines using a setter function our Frame's content plane
	 *
	 */
	public void setContainer(Container container) {
		this.container = container;
	}
	/**
	 *Returns the canvas used by the GUI
	 */
	public GameViewPanel getCanvas() {
		return canvas;
	}
	/**
	 *Sets the our existing GUI canvas to the incoming parameter
	 */
	public void setCanvas(GameViewPanel canvas) {
		this.canvas = canvas;
	}
	/**
	 *Returns the canvas referenced by the game logic
	 */
	public GameViewPanel getCanvasRef() {
		return canvasRef;
	}
	/**
	 *Sets the canvas referenced by game logic to the parameter
	 */
	public void setCanvasRef(GameViewPanel canvasRef) {
		this.canvasRef = canvasRef;
	}
	/**
	 *Returns the South Panel
	 */
	public ChatViewPanel getSouthPanel() {
		return southPanel;
	}
	/**
	 *Resets the south view panel
	 */
	public void setSouthPanel(ChatViewPanel southPanel) {
		this.southPanel = southPanel;
	}
	/**
	 *Returns the text box
	 */
	public JEditorPane getOutputBox() {
		return outputBox;
	}
	/**
	 *Sets the text box
	 */
	public void setOutputBox(JEditorPane outputBox) {
		this.outputBox = outputBox;
	}
	/**
	 *Returns the list of users
	 */
	public JList getUserList() {
		return userList;
	}
	/**
	 *Resets the userlist with the redefined one
	 */
	public void setUserList(JList userList) {
		this.userList = userList;
	}
	/**
	 *Returns a simple implementation of the ListModel that you can use for modelling simple data models
	 */
	public DefaultListModel getListModel() {
		return listModel;
	}
	/**
	 *Sets the listModel to the incoming parameter
	 */
	public void setListModel(DefaultListModel listModel) {
		this.listModel = listModel;
	}
	/**
	 *Returns the Keys array 
	 */
	public boolean[] getKeys() {
		return Keys;
	}
	/**
	 *Resets the Keys array
	 */
	public void setKeys(boolean[] keys) {
		Keys = keys;
	}
	/**
	 * Returns our menu panel
	 */
	public JPanel getMenuPanel() {
		return menuPanel;
	}
	/**
	 *Resets our menu panel to the incoming parameter
	 */
	public void setMenuPanel(JPanel menuPanel) {
		this.menuPanel = menuPanel;
	}
	/**
	 *Returns the user's panel
	 */
	public JPanel getUserPanel() {
		return userPanel;
	}
	/**
	 *Resets the User Panel
	 */
	public void setUserPanel(JPanel userPanel) {
		this.userPanel = userPanel;
	}
	/**
	 *Returns a lightweight component who's size can change dynamically
	 */
	public JScrollPane getUserScroll() {
		return userScroll;
	}
	/**
	 *Redefines our user scroll with new parameter
	 */
	public void setUserScroll(JScrollPane userScroll) {
		this.userScroll = userScroll;
	}
	/**
	 *Returns our follow button
	 */
	public JButton getFollowButton() {
		return followButton;
	}
	/**
	 *Resets our follow button
	 */
	public void setFollowButton(JButton followButton) {
		this.followButton = followButton;
	}
	/**
	 *Returns the message button
	 */
	public JButton getMessageButton() {
		return messageButton;
	}
	/**
	 *Resets the message button
	 */
	public void setMessageButton(JButton messageButton) {
		this.messageButton = messageButton;
	}

}
