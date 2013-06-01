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
    
    public void updateCavnasPanel() {
    	container.remove(canvas);
        canvas = canvasRef;
        container.add(BorderLayout.CENTER, canvas);
        canvas.addMouseListener(canvas);
        container.validate();
    }
	
	@Override
    public void keyTyped(KeyEvent keyEvent){ }

    @Override
    public void keyPressed(KeyEvent keyEvent){
        Keys[keyEvent.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent keyEvent){
        Keys[keyEvent.getKeyCode()] = false;
    }

	public JFrame getFrame() {
		return frame;
	}

	public void setFrame(JFrame frame) {
		this.frame = frame;
	}

	public Container getContainer() {
		return container;
	}

	public void setContainer(Container container) {
		this.container = container;
	}

	public GameViewPanel getCanvas() {
		return canvas;
	}

	public void setCanvas(GameViewPanel canvas) {
		this.canvas = canvas;
	}

	public GameViewPanel getCanvasRef() {
		return canvasRef;
	}

	public void setCanvasRef(GameViewPanel canvasRef) {
		this.canvasRef = canvasRef;
	}

	public ChatViewPanel getSouthPanel() {
		return southPanel;
	}

	public void setSouthPanel(ChatViewPanel southPanel) {
		this.southPanel = southPanel;
	}

	public JEditorPane getOutputBox() {
		return outputBox;
	}

	public void setOutputBox(JEditorPane outputBox) {
		this.outputBox = outputBox;
	}

	public JList getUserList() {
		return userList;
	}

	public void setUserList(JList userList) {
		this.userList = userList;
	}

	public DefaultListModel getListModel() {
		return listModel;
	}

	public void setListModel(DefaultListModel listModel) {
		this.listModel = listModel;
	}

	public boolean[] getKeys() {
		return Keys;
	}

	public void setKeys(boolean[] keys) {
		Keys = keys;
	}

	public JPanel getMenuPanel() {
		return menuPanel;
	}

	public void setMenuPanel(JPanel menuPanel) {
		this.menuPanel = menuPanel;
	}

	public JPanel getUserPanel() {
		return userPanel;
	}

	public void setUserPanel(JPanel userPanel) {
		this.userPanel = userPanel;
	}

	public JScrollPane getUserScroll() {
		return userScroll;
	}

	public void setUserScroll(JScrollPane userScroll) {
		this.userScroll = userScroll;
	}

	public JButton getFollowButton() {
		return followButton;
	}

	public void setFollowButton(JButton followButton) {
		this.followButton = followButton;
	}

	public JButton getMessageButton() {
		return messageButton;
	}

	public void setMessageButton(JButton messageButton) {
		this.messageButton = messageButton;
	}

}
