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


/**
 * gomokugame is a gomoku game object that stores data about a gomoku game, such as placement of Xs and Os,
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
    }
/**
 *Checks to see who won by comparing three of a pattern whether diagonal, vertical, or horizontal. Only returns true/false since this is a helper method but can print "no winner found" if the game is still contuing.
 */
public boolean checkWinner() {
	//FIND BETTER ALGORITHM FOR CHECKING WINNER, DOESNT NECCESARILY NEED TO GO BY 3X3 OR 10X10
       /* for(int j=1;j<3;j++) {
            for(int i=0;i<3;i++) {
                if(grid[0][i] == j && grid[1][i] == j && grid[2][i] == j) {
                    winner = j;
                    return true;
                }
                if(grid[i][0] == j && grid[i][1] == j && grid[i][2] == j) {
                    winner = j;
                    return true;
                }
            }
            if(grid[0][0] == j && grid[1][1] == j && grid[2][2] == j) {
                winner = j;
                return true;
            }
            if(grid[0][2] == j && grid[1][1] == j && grid[2][0] == j) {
                winner = j;
                return true;
            }
        }
        System.out.println("no winner found");
        return false;
	*/
	return false;
    }
        /**
     *Parses our incoming string into an array and defines the new state of our Tic Tac Toe grid.
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
                                                                                    

