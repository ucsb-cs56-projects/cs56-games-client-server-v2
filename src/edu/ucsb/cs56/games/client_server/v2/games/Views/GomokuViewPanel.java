package edu.ucsb.cs56.games.client_server.v2.games.Views;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.GeneralPath;

import javax.swing.JPanel;

import edu.ucsb.cs56.games.client_server.v2.client.Controllers.JavaClient;
import edu.ucsb.cs56.games.client_server.v2.client.Models.ClientModel;
import edu.ucsb.cs56.games.client_server.v2.games.ClientControllers.GomokuController;


/**
 * Gomoku panel allows user to interface with server while playing gomoku game, accepts input and draws board and
 * current player's turn on the screen
 *
 * @author David Roster
 * @author Harrison Wang
 * @version for CS56, Spring 2017
 */

public class GomokuViewPanel extends TwoPlayerGameViewPanel {
        private ClientModel player1, player2;
        private GomokuController controller;

    private int offsetX, offsetY;
    private int margin = 3;
    private int gridSize;
    private int panelSize;
    private int topMargin = 40;
    private int bottomMargin = 30;
    private int [][] grid;
    private int winner;
    private int turn;

    /**
     *Constructor creates grid for Gomoku
     */
    public GomokuViewPanel(GomokuController controller )  {
        super();

        this.controller = controller;
        GomokuCanvas canvas = new GomokuCanvas();
        canvas.addMouseListener(canvas);
        add(BorderLayout.CENTER, canvas);

        grid = new int[10][10];
        for (int i = 0; i < 10; ++i) {
                for (int j = 0; j < 10; ++j) {
                        grid[i][j] = 0;
                }
        }

        turn = 1;
        winner = 0;
    }

public void init() {
        // This is redundant, will probably remove
        for (int i = 0; i < 10; ++i) {
                for (int j = 0; j < 10; ++j) {
                        grid[i][j] = 0;
                }
        }

        turn = 1;
        winner = 0;
    }


   /**
     * Creates our Gomoku Canvas
     */
    class GomokuCanvas extends JPanel implements MouseListener {
        /**
         *Colors in our Gomoku and creates ready states that draw in each user's move into the board itself with a white or brown player piece. This also deals with which player whens and loses.
         */
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            int totalWidth = getWidth();
            int totalHeight = getHeight();

            int gridHeight = totalHeight - topMargin - bottomMargin;
            int gridWidth = totalWidth;
            if(gridHeight < gridWidth) {
                panelSize = gridHeight;
                offsetX = (totalWidth-panelSize)/2;
                offsetY = topMargin;
            } else {
                panelSize = gridWidth;
                offsetX = 0;
                offsetY = (gridHeight-panelSize)/2+topMargin;
            }

            gridSize = panelSize/10;//makes a each little box

            g.setColor(Color.white);
            g.fillRect(0,0,getWidth(),getHeight());
            if(!controller.getClient().isConnected() || controller.getClient().getClients() == null)
                return;

            g.setColor(new Color(0xda6d0e));//changed from 0x333333
	    for (int i=0; i<=10; i++){
		g.fillRect(offsetX+gridSize*i-margin,offsetY, margin*2, gridSize*10);
		g.fillRect(offsetX,offsetY+gridSize*i-margin, gridSize*10, margin*2);
	    }

            Graphics2D g2d = (Graphics2D)g;
            g2d.setStroke(new BasicStroke(5));

            for(int i=0;i<10;i++) {
                for(int j=0;j<10;j++) {
                    int pid = grid[i][j];
		    if(pid == 0)
                        continue;

                    if(pid == 1) {
			//black chess for player 1
                        g.setColor(Color.BLACK);
			g.drawOval( offsetX + j * gridSize + 3*margin, offsetY + i * gridSize + 3*margin, gridSize - 6*margin, gridSize - 6*margin);
                        g.fillOval( offsetX + j * gridSize + 3*margin, offsetY + i * gridSize + 3*margin, gridSize - 6*margin, gridSize - 6*margin);
			
                     
                    } else {
			//white chess for player 2
                        g.setColor(Color.BLACK);
                        g.drawOval( offsetX + j * gridSize + 3*margin, offsetY + i * gridSize + 3*margin, gridSize - 6*margin, gridSize - 6*margin);
			g.setColor(Color.WHITE);
			g.fillOval( offsetX + j * gridSize + 3*margin, offsetY + i * gridSize + 3*margin, gridSize - 6*margin, gridSize - 6*margin);
                    }

                }
            }
g2d.setStroke(new BasicStroke(2));
            String readyState = "";
            if(player1 != null) {
		    //Shows up indicating turn with matching symbol
                g.setColor(Color.BLACK);
                g.drawString("Player 1: " + player1.getName(), offsetX, 15);
		g.drawOval( offsetX -23, offsetY - 38, 16, 16);
                g.fillOval( offsetX -23, offsetY - 38, 16, 16);
                
            } else
                readyState = "waiting for players";
            if(player2 != null) {
		    //player 2 name and symbol
                g.setColor(Color.BLACK);
                g.drawString("Player 2: "+ player2.getName(),offsetX,35);
		g.drawOval( offsetX -23, offsetY - 18, 16, 16);
		g.setColor(Color.WHITE);
                g.fillOval( offsetX -23, offsetY - 18, 16, 16);
            } else
                readyState = "waiting for players";

            if(readyState.equals("")) {
                if(winner == 1)
                    readyState = player1.getName()+" wins!";
                else if(winner == 2)
                    readyState = player2.getName()+" wins!";
                else if(turn == 1)
                    readyState = player1.getName()+"'s turn";
                else
                    readyState = player2.getName()+"'s turn";
            }

            g.setColor(new Color(0x222222));
            g.drawString(readyState,offsetX+panelSize/2-45,offsetY+panelSize+20);
        }



	 /**
         *Mouse pressed function indicates whether there was a mouse click in a specific square
         */
        @Override
        public void mousePressed(MouseEvent mouseEvent){
            if(!controller.isPlaying())
                return;

            //To change body of implemented methods use File | Settings | File Templates.
            int mX = mouseEvent.getX();
            int mY = mouseEvent.getY();
            int dX = mX-offsetX;
            int dY = mY-offsetY;
            int cellX = (dX*10/panelSize);
            int cellY = (dY*10/panelSize);
            System.out.println(mX+", "+mY+", "+dX+", "+dY+", "+cellX+", "+cellY);
            if(cellX >= 0 && cellX < 10 && cellY >= 0 && cellY < 10) {
                controller.getClient().sendMessage("MOVE;" + cellX + "," + cellY);
            }
        }//closes overriden method

  /**
                 *Returns what the mouse clicked!
                 */
                @Override
                public void mouseClicked(MouseEvent arg0) {
                        // TODO Auto-generated method stub

                }
                /**
                 *Overriden method, says when mouse has enetered our respected defined region
                 */
                @Override
                public void mouseEntered(MouseEvent arg0) {
                        // TODO Auto-generated method stub

                }
                /**
                 *Overriden method that knows when the user uses the mouse to enter a region of space.
                 */
                @Override
                public void mouseExited(MouseEvent arg0) {
                        // TODO Auto-generated method stub

                }
                /**
                 *Overriden method that knows when the mouse is released off a button by a user
                 */
                @Override
                public void mouseReleased(MouseEvent arg0) {
                        // TODO Auto-generated method stub

                }
    }//closes inner class


     /**
         *Returns player 1
         */
        public ClientModel getPlayer1() {
                return player1;
        }
        /**
         *Resets player 1
         */
        public void setPlayer1(ClientModel player1) {
                this.player1 = player1;
        }
        /**
         *Returns PLayer 2
         */
        public ClientModel getPlayer2() {
                return player2;
        }
        /**
         *Resets Player 2
         */
        public void setPlayer2(ClientModel player2) {
                this.player2 = player2;
        }
        /**
         *Returns our controller created for Gomoku
         */
        public GomokuController getController() {
                return controller;
        }
        /**
         *Resets the controller for Gomoku
         */
        public void setController(GomokuController controller) {
                this.controller = controller;
        }
        /**
         * returns x coordinate based off x-margin
         */
        public int getOffsetX() {
                return offsetX;
        }
 /**
         *Resets OffsetX
         */
        public void setOffsetX(int offsetX) {
                this.offsetX = offsetX;
        }
        /**
         *returns y coordinate based off y-margin
         */
        public int getOffsetY() {
                return offsetY;
        }
        /**
         *Resets OffsetY with its updated value
         */
        public void setOffsetY(int offsetY) {
                this.offsetY = offsetY;
        }
        /**
         *returns margins
         */
        public int getMargin() {
                return margin;
        }
        /**
         *Resets the margins
         */
        public void setMargin(int margin) {
                this.margin = margin;
        }
        /**
         *Getter function returns the Grid Size
         */
        public int getGridSize() {
                return gridSize;
        }
        /**
         *Setter function resets grid size
         */
        public void setGridSize(int gridSize) {
                this.gridSize = gridSize;
        }
 /**
         *Returns our game view panel size for client
         */
        public int getPanelSize() {
                return panelSize;
        }
        /**
         *Resets the game view Panel size for client
         */
        public void setPanelSize(int panelSize) {
                this.panelSize = panelSize;
        }
        /**
         *Returns top margin  - this is what we use to base grid size off and size of each square.
         */
        public int getTopMargin() {
                return topMargin;
        }
        /**
         *Resets top margin
         */
        public void setTopMargin(int topMargin) {
                this.topMargin = topMargin;
        }
        /**
         *Returns bottom margin - this is what we use to calculate each of the squares of our grid
         */
        public int getBottomMargin() {
                return bottomMargin;
        }
        /**
         *Resets Bottom margin
         */
        public void setBottomMargin(int bottomMargin) {
                this.bottomMargin = bottomMargin;
        }
        /**
         *returns our double array that makes up grid
         */
        public int[][] getGrid() {
                return grid;
        }
        /**
         *Resets the double array with our new values based on board position.
         */
        public void setGrid(int[][] grid) {
                this.grid = grid;
        }
         /**
         *Goes to specific location in grid and  marks respected value with user's X or O
         */
        public void setGridSpot(int i, int j, int value) {
                this.grid[i][j] = value;
        }
        /**
         *Returns Winner of Gomoku
         */
        public int getWinner() {
                return winner;
        }
        /**
         *Sets the winner of the Gomoku
         */
        public void setWinner(int winner) {
                this.winner = winner;
        }
        /**
         *Returns which player's turn it is
         */
        public int getTurn() {
                return turn;
        }
        /**
         *Resets which player's turn it is
         */
        public void setTurn(int turn) {
                this.turn = turn;
        }
}




