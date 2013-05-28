package edu.ucsb.cs56.games.client.Models;

/**
 * Username class stores information about a user, such as name, location, etc
 * rendered by the user list in javaclient
 *
 * @author Joseph Colicchio
 * @author Adam Ehrlich
 * @version for CS56, Spring 2013
 */

public class UsernameModel{
	
    private String name;
    private String location;
    private int style;
    
    public UsernameModel(String name, String location, int style) {
        this.name = name;
        this.location = location;
        this.style = style;
    }
    
    @Override
    public String toString() {
        if(location == null)
            return getName();
        return getName()+location;
    }
    
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
    
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public int getStyle() {
		return style;
	}

	public void setStyle(int style) {
		this.style = style;
	}
}
