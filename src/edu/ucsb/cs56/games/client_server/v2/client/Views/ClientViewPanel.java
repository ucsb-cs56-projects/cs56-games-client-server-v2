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

import edu.ucsb.cs56.games.client_server.v2.server.Controllers.JavaServer;
import edu.ucsb.cs56.games.client_server.v2.games.Views.GameViewPanel;

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
        /**
     *The ClientViewPanel constructor creates the main window the client sees with its major buttons such as an output text field, follow button, message button, and other neat buttons. Sizing ir orientated here.  
     *
     *
     */

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
	 *Returns which key was typed on the client window. 
	 * This a general method for keys that we will call a lot
	 */
	@Override
    public void keyTyped(KeyEvent keyEvent){ }
	/**
	 *Sets the value of the key that the user pressed to true and stores it. 
	 */
    @Override
    public void keyPressed(KeyEvent keyEvent){
        Keys[keyEvent.getKeyCode()] = true;
    }
	/**
	 *Sets the value of the user's released key to false and stores it.
	 */
    @Override
    public void keyReleased(KeyEvent keyEvent){
        Keys[keyEvent.getKeyCode()] = false;
    }
	/**
	 *Getter function used to return JFrame, the main window of our client application
	 */
	public JFrame getFrame() {
		return frame;
	}
	/**
	 **Setter function that redefines our existing JFrame. 
	 *	This can/will be used when changing the size of the frame itself
	 */
	public void setFrame(JFrame frame) {
		this.frame = frame;
	}
	/**
	 *Getter Function that returns the Frame's content panel. We redefined container to stand for the Frame's content plane for easier readibility and space.
	 */
	public Container getContainer() {
		return container;
	}
	/**
	 *Redefines using a setter function our Frame's content plane. The size of our content plane can be changing on depending the size you prefer for the client window.
	 *
	 */
	public void setContainer(Container container) {
		this.container = container;
	}
	/**
	 *Returns the canvas used by the GUI for the game(tic-tac-toe..)

	 */
	public GameViewPanel getCanvas() {
		return canvas;
	}
	/**
	 *Sets the our existing game canavas to the new game canvas with the new user's moves
	 */
	public void setCanvas(GameViewPanel canvas) {
		this.canvas = canvas;
	}
	/**
	 *Returns the canvas referenced by the game logic, wheich it will then make modifications to based on the next user's move.
	 */
    public GameViewPanel getCanvasRef() {
		return canvasRef;
	}
	/**
	 *Sets the canvas basedd on game logic to the the new game logic featuring the new user's moves.
	 */
	public void setCanvasRef(GameViewPanel canvasRef) {
		this.canvasRef = canvasRef;
	}
	/**
	 *Returns the South Panel of the Client Application view at its current state
	 * 	The south panel is at the bottom of the window
	 */
	public ChatViewPanel getSouthPanel() {
		return southPanel;
	}
	/**
	 *Resets the south view panel(at bottom of screen) to show users' new messages
	 */
	public void setSouthPanel(ChatViewPanel southPanel) {
		this.southPanel = southPanel;
	}
	/**
	 *Returns the text box containing all the users messages (bottom of client application)
	 */
	public JEditorPane getOutputBox() {
		return outputBox;
	}
	/**
	 *Changes our textbox to add new messages to it. THis happens after very text
	 */
	public void setOutputBox(JEditorPane outputBox) {
		this.outputBox = outputBox;
	}
	/**
	 *Returns the list of users present in lobby and playing in-game
	 */
	public JList getUserList() {
		return userList;
	}
	/**
	 *Resets the userlist with the modified one containing new or lost users
	 */
	public void setUserList(JList userList) {
		this.userList = userList;
	}
	/**
	 *Returns a simple implementation of the ListModel that you can use for modelling simple data models. Ours is numbered sequentially
	 */
	public DefaultListModel getListModel() {
		return listModel;
	}
	/**
	 *Resets our listModel with any new or gone users.
	 */
	public void setListModel(DefaultListModel listModel) {
		this.listModel = listModel;
	}
	/**
	 *Returns the Keys array which is what we use to store which keys are pressed/not pressed
	 */
	public boolean[] getKeys() {
		return Keys;
	}
	/**
	 *Resets the Keys array with the new value when key is pressed 

	 */
	public void setKeys(boolean[] keys) {
		Keys = keys;
	}
	/**
	 *  Returns our menu panel that has our follow and message button
	 */
	public JPanel getMenuPanel() {
		return menuPanel;
	}
	/**
	 *Resets our menu panel to the incoming parameter. Called either on creation or when the client application is resized.
	 */
	public void setMenuPanel(JPanel menuPanel) {
		this.menuPanel = menuPanel;
	}
	/**
	 *Returns the user's panel where all the current users displayed. This is on the west side of the screen.
	 */
	public JPanel getUserPanel() {
		return userPanel;
	}
	/**
	 *Resets the User Panel when new users are added or if they leave.
	 */
	public void setUserPanel(JPanel userPanel) {
		this.userPanel = userPanel;
	}
	/**
	 *Returns a lightweight component who's size can change dynamically. This is used for our text dialog box to more conveniently see all texts quickly and easily.
	 */
	public JScrollPane getUserScroll() {
		return userScroll;
	}
	/**
	 *Redefines our user scroll based on the size of the client application
	 */
	public void setUserScroll(JScrollPane userScroll) {
		this.userScroll = userScroll;
	}
	/**
	 *Returns our follow button when pressed by the user.
	 */
	public JButton getFollowButton() {
		return followButton;
	}
	/**
	 *Resets our follow button when client side is just starting or if the aspect ratio changes of our application.
	 */
	public void setFollowButton(JButton followButton) {
		this.followButton = followButton;
	}
	/**
	 *Returns that the message button was clicked by the user. 
	 */
	public JButton getMessageButton() {
		return messageButton;
	}
	/**
	 *Resets the message button either due to size adjustments or first creation.
	 */
	public void setMessageButton(JButton messageButton) {
		this.messageButton = messageButton;
	}

}
