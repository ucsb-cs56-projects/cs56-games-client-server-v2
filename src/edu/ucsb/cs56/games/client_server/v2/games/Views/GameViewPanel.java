package edu.ucsb.cs56.games.client_server.v2.games.Views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Game panel is a largely abstract panel that provides an outline for how panels should be formatted, as well as provides
 * basic functions every panel should have. Might consider turning this into an interface, but before the offlinepanel existed,
 * gamepanel was used when a client was disconnected
 *
 * @author David Roster
 * @author Harrison Wang
 * @version for CS56, Spring 2017
 */

public class GameViewPanel extends JPanel implements MouseListener{
    public GameViewPanel() {
        setLayout(new BorderLayout());
    }

    /**
     * update the game
     */
    public void update() {

    }
	/**
	 *mouse clicked during game
	 */
    @Override
    public void mouseClicked(MouseEvent mouseEvent){
    }
	/**
	 *Returns mouse being pressed during game
	 */
    @Override
    public void mousePressed(MouseEvent mouseEvent){
    }
	/**
	 *mouse is released during game
	 */
    @Override
    public void mouseReleased(MouseEvent mouseEvent){
    }
	/**
	 *Mouse eneters game view
	 */
    @Override
    public void mouseEntered(MouseEvent mouseEvent){
    }
	/**
	 *Mouse exits the game view
	 */
    @Override
    public void mouseExited(MouseEvent mouseEvent){
    }
}
