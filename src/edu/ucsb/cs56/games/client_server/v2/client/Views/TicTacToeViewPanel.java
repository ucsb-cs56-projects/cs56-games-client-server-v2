package edu.ucsb.cs56.games.client_server.v2.client.Views;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.GeneralPath;

import javax.swing.JPanel;

import edu.ucsb.cs56.games.client_server.v2.Controllers.JavaClient;
import edu.ucsb.cs56.games.client_server.v2.Models.ClientModel;
import edu.ucsb.cs56.games.client_server.v2.client.Controllers.TicTacToeController;


/**
 * tictactoepanel allows user to interface with server while playing tic tac toe game, accepts input and draws board and
 * current player's turn on the screen
 *
 * @author Joseph Colicchio
 * @author Adam Ehrlich
 * @version for CS56, Spring 2013
 */

public class TicTacToeViewPanel extends TwoPlayerGameViewPanel {
	private ClientModel player1, player2;
	private TicTacToeController controller;

    private int offsetX, offsetY;
    private int margin = 3;
    private int gridSize;
    private int panelSize;
    private int topMargin = 40;
    private int bottomMargin = 30;
    private int [][] grid;
    private int winner;
    private int turn;

    public TicTacToeViewPanel(TicTacToeController controller) {
    	super();
        
    	this.controller = controller;
        TicTacToeCanvas canvas = new TicTacToeCanvas();
        canvas.addMouseListener(canvas);
        add(BorderLayout.CENTER, canvas);
        
        grid = new int[3][3];
        for (int i = 0; i < 3; ++i) {
        	for (int j = 0; j < 3; ++j) {
        		grid[i][j] = 0;
        	}
        }
    	
    	turn = 1;
        winner = 0;
    }
    
    public void init() {
    	// This is redundant, will probably remove
    	for (int i = 0; i < 3; ++i) {
        	for (int j = 0; j < 3; ++j) {
        		grid[i][j] = 0;
        	}
        }
    	
    	turn = 1;
        winner = 0;
    }
    
    class TicTacToeCanvas extends JPanel implements MouseListener {

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

            gridSize = panelSize/3;

            g.setColor(Color.white);
            g.fillRect(0,0,getWidth(),getHeight());
            if(!controller.getClient().isConnected() || controller.getClient().getClients() == null)
                return;

            g.setColor(new Color(0x333333));
            g.fillRect(offsetX+gridSize-margin,offsetY, margin*2, gridSize*3);
            g.fillRect(offsetX+gridSize*2-margin,offsetY, margin*2, gridSize*3);
            g.fillRect(offsetX,offsetY+gridSize-margin, gridSize*3, margin*2);
            g.fillRect(offsetX,offsetY+gridSize*2-margin, gridSize*3, margin*2);


            Graphics2D g2d = (Graphics2D)g;
            g2d.setStroke(new BasicStroke(5));
            
            for(int i=0;i<3;i++) {
                for(int j=0;j<3;j++) {
                    int pid = grid[i][j];
                    if(pid == 0)
                        continue;

                    if(pid == 1) {
                        g2d.setColor(Color.RED);
                        GeneralPath path = new GeneralPath();
                        path.moveTo(offsetX + j * gridSize + 3*margin, offsetY + i * gridSize + 3*margin);
                        path.lineTo(offsetX+(j+1)*gridSize - 3*margin, offsetY+(i+1)*gridSize - 3*margin);
                        path.moveTo(offsetX+(j+1)*gridSize - 3*margin, offsetY + i * gridSize + 3*margin);
                        path.lineTo(offsetX + j * gridSize + 3*margin, offsetY+(i+1)*gridSize - 3*margin);
                        g2d.draw(path);
                    } else {
                        g.setColor(Color.BLUE);
                        g.drawOval( offsetX + j * gridSize + 3*margin, offsetY + i * gridSize + 3*margin,
                                gridSize - 6*margin, gridSize - 6*margin);
                    }

                }
            }

            g2d.setStroke(new BasicStroke(2));
            String readyState = "";
            if(player1 != null) {
                g.setColor(Color.red);
                g.drawString("Player 1: " + player1.getName(), offsetX, 15);
                GeneralPath path = new GeneralPath();
                path.moveTo(offsetX-23,offsetY-38);
                path.lineTo(offsetX-7, offsetY-22);
                path.moveTo(offsetX-7,offsetY-38);
                path.lineTo(offsetX-23, offsetY-22);
                g2d.draw(path);
            } else
                readyState = "waiting for players";
            if(player2 != null) {
                g.setColor(Color.blue);
                g.drawString("Player 2: "+ player2.getName(),offsetX,35);
                g.drawOval( offsetX -23, offsetY - 18,
                        16, 16);
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
        @Override
        public void mousePressed(MouseEvent mouseEvent){
            if(!controller.isPlaying())
                return;

            //To change body of implemented methods use File | Settings | File Templates.
            int mX = mouseEvent.getX();
            int mY = mouseEvent.getY();
            int dX = mX-offsetX;
            int dY = mY-offsetY;
            int cellX = (dX*3/panelSize);
            int cellY = (dY*3/panelSize);
            System.out.println(mX+", "+mY+", "+dX+", "+dY+", "+cellX+", "+cellY);
            if(cellX >= 0 && cellX < 3 && cellY >= 0 && cellY < 3) {
            	controller.getClient().sendMessage("MOVE;" + cellX + "," + cellY);
            }
        }
		@Override
		public void mouseClicked(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
    }

	public ClientModel getPlayer1() {
		return player1;
	}

	public void setPlayer1(ClientModel player1) {
		this.player1 = player1;
	}

	public ClientModel getPlayer2() {
		return player2;
	}

	public void setPlayer2(ClientModel player2) {
		this.player2 = player2;
	}

	public TicTacToeController getController() {
		return controller;
	}

	public void setController(TicTacToeController controller) {
		this.controller = controller;
	}

	public int getOffsetX() {
		return offsetX;
	}

	public void setOffsetX(int offsetX) {
		this.offsetX = offsetX;
	}

	public int getOffsetY() {
		return offsetY;
	}

	public void setOffsetY(int offsetY) {
		this.offsetY = offsetY;
	}

	public int getMargin() {
		return margin;
	}

	public void setMargin(int margin) {
		this.margin = margin;
	}

	public int getGridSize() {
		return gridSize;
	}

	public void setGridSize(int gridSize) {
		this.gridSize = gridSize;
	}

	public int getPanelSize() {
		return panelSize;
	}

	public void setPanelSize(int panelSize) {
		this.panelSize = panelSize;
	}

	public int getTopMargin() {
		return topMargin;
	}

	public void setTopMargin(int topMargin) {
		this.topMargin = topMargin;
	}

	public int getBottomMargin() {
		return bottomMargin;
	}

	public void setBottomMargin(int bottomMargin) {
		this.bottomMargin = bottomMargin;
	}

	public int[][] getGrid() {
		return grid;
	}

	public void setGrid(int[][] grid) {
		this.grid = grid;
	}
	
	public void setGridSpot(int i, int j, int value) {
		this.grid[i][j] = value;
	}

	public int getWinner() {
		return winner;
	}

	public void setWinner(int winner) {
		this.winner = winner;
	}

	public int getTurn() {
		return turn;
	}

	public void setTurn(int turn) {
		this.turn = turn;
	}
}
