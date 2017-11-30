package edu.ucsb.cs56.games.client_server.v2.client.Models;

/**
 * Username class stores information about a user, such as name, location, etc
 * rendered by the user list in javaclient
 *
 * @author David Roster
 * @author Harrison Wang
 * @version for CS56, Spring 2017
 */

public class UsernameModel{
	
    private String name;
    private String location;
    private int style;
    
    /**
     *UsernameModel Constructor takes in the parameters name, location, and style to 
     create a categorizing class of the user's personal info 
     *@param name the respected name of each client
     *@param location location of the client on the server
     *@param style generates the correct style of font for the client
     */
    public UsernameModel(String name, String location, int style) {
        this.name = name;
        this.location = location;
        this.style = style;
    }
    
    /**
     *Overrides the toString to provide better functionality when inquiring about location
     *@param None
     *@return This method will either return getName() -if location is null- 
     * or getName()+location
     */
    @Override
    public String toString() {
        if(location == null)
            return getName();
        return getName()+location;
    }
    
    /**
     *Overrides the equals method to compare against pre-existing usernames
     *@param o - of type object
     *@return The method returns true if the style, location, and name do not match the 
     * values passed in through constructor
     *
     */
    @Override
    public boolean equals(Object o) {
    	if (o == null)
    		return false;
    	
    	if (getClass() != o.getClass())
    		  return false;
    	
    	UsernameModel user = (UsernameModel)o;
    	
    	if (user.getName() != name)
    		return false;
    	if (user.getStyle() != style)
    		return false;
    	if (user.getLocation() != location)
    		return false;
    	
    	return true;
    }
    
    /**
     *Returns user's name
     *@param None
     *
     */
	public String getName() {
		return name;
	}
	/**
	 *Resets the name of the user if already taken
	 *@param name The parameter is of type string
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 *Returns the location of the client
	 *@param NONE
	 */
	public String getLocation() {
		return location;
	}
	/**
	 *Resets the location of the client if its already in use
	 *@param location The data type is a string
	 */
	public void setLocation(String location) {
		this.location = location;
	}
	/**
	 *Returns the style of font of the Client
	 *@param NONE
	 */
	public int getStyle() {
		return style;
	}
	/**
	 *Resets the style of font
	 @param style The data type is string
	 */
	public void setStyle(int style) {
		this.style = style;
	}
}
