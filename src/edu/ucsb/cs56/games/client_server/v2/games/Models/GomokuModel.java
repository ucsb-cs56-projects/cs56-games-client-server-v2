/*
 * 3 Main Methods:
 *
 *checkWinner()
 *-> Return whether a player has won the game in the current turn yet or not
  -> @return has someone won the game
 *
 * SetState(String State)
 * -> Is responsible for updating the model as to what is going on.  Usually it is a wise idea
 *     to pass in some string that is parsable by your setState() function to update the variables
       in the model.
 * -> @param state the state of the game
 *
 * String getState()
 * -> Is responsible for getting information about the instance variables in the game using some form
       of string parser.  Similar to setState, but is meant to be handled by the Server Controller for
       the specified game.
 * -> @return the current state of the instance variables for the game
*/
  
package edu.ucsb.cs56.games.client_server.v2.games.Models;

import edu.ucsb.cs56.games.client_server.v2.client.Models.ClientModel;

import java.lang.Math;


/**
 * gomokugame is a gomoku game object that stores data about a gomoku game, such as placement of Xste and brown user pieces,
 * and possibly the winner
 *
 * @author David Roster
 * @author Harrison Wang
 * @version for CS56, Spring 2017
 */
public class GomokuModel implements TwoPlayerGameModel {
    public ClientModel player1, player2;

    public int[][] grid;
    public int turn;
    public int winner;
    private int lastrow;
    private int lastcol;

    public GomokuModel() {
        init();
    }
/**
 *Initializes the state of our grid board for the server. That way the server can also follow the game closely and make announcements off the flow of the game.
 */
    public void init() {
        grid = new int[10][10];
        for(int i=0;i<10;i++)
            for(int j=0;j<10;j++)
                grid[i][j] = 0;

        turn = 1;
        winner = 0;
	lastrow = 0;
	lastcol = 0;
    }

    
/**
 *Checks to see who won by comparing five of a pattern whether diagonal, vertical, or horizontal. Only returns true/false since this is a helper method but can print "no winner found" if the game is still contuing.
 */
    public boolean checkWinner() {
	int row = lastrow;
	int col = lastcol;
	int player = grid[row][col];
	if (player==0) {
	    System.out.println("no winner found");
	    return false;
	}
	int count =0;
	//Checks vertical direction for win
	for(int i=0; i<10; i++)
	    {
		if(grid[i][col] == player)
		    count++;
		else
		    count = 0;
		if (count >=5) {
		    winner = player;
		    return true;
		}
	    }
	count = 0;
	//checks Horizontal direction for win
	 for(int i=0; i<10; i++)
	    {
		if(grid[row][i] == player)
		    count++;
		else
		    count = 0;
		if (count >=5) {
		    winner = player;
		    return true;
		}
	    }
	count = 0;
	//Now check for diagonal (1,1) from player's piece position
	int min = Math.min(col, row);
	int dif;
	if (min == row) {
	    dif = col-row;
	    for(int i=0; i<=9-dif; i++)
	    {
		if(grid[i][i+dif] == player)
		    count++;
		else
		    count = 0;
		if (count >=5) {
		    winner = player;
		    return true;
		}
	    }
	}
	else {
	    dif = row - col;
	    for(int i=0; i<=9-dif; i++)
	    {
		if(grid[i+dif][i] == player)
		    count++;
		else
		    count = 0;
		if (count >=5) {
		    winner = player;
		    return true;
		}
	    }
	}
	count = 0;

	//check for diagonal (1,-1) from player's piece position
	int sum = col + row;
	if (sum >= 9) {
	    for(int i=sum-9; i<=9; i++)
	    {
		if(grid[sum-i][i] == player)
		    count++;
		else
		    count = 0;
		if (count >=5) {
		    winner = player;
		    return true;
		}
	    }
	}
	else {
	    for(int i=0; i<=sum; i++)
	    {
		if(grid[sum-i][i] == player)
		    count++;
		else
		    count = 0;
		if (count >=5) {
		    winner = player;
		    return true;
		}
	    }
	}
		
        System.out.println("no winner found");
	return false;
    }

    public void setLastRow(int a) {
	lastrow = a;
    }

    public void setLastCol(int a) {
	lastcol = a;
    }
    
        /**
     *Parses our incoming string into an array and defines the new state of our Gomoku grid.
     */
    public void setState(String data) {
        String[] info = data.substring(6).split("]");
        int turnInfo = Integer.parseInt(info[0]);
        if(turnInfo == 0)
            checkWinner();
        else {
            turn = turnInfo;
            winner = 0;
        }
        String[] rows = info[1].split(";");
        for(int i=0;i<10;i++) {
            String[] cols = rows[i].split(",");
            for(int j=0;j<10;j++) {
                grid[i][j] = Integer.parseInt(cols[j]);
            }
        }
        checkWinner();
    }
   /**
     *Sees the current state of the board, and analyzes it to see if their is a current winner by calling the checkWinner() method.
     */

    public String getState() {
        String state = "STATE[";
        if(winner == 0)
            state += turn+"]";
        else
            state += "0]";
        for(int i=0;i<10;i++) {
            for(int j=0;j<10;j++) {
                state += grid[i][j];
                if(j < 9)
                    state +=",";
            }
            if(i<9)
                state += ";";
        }
        return state;
    }
}
                                                                                    

