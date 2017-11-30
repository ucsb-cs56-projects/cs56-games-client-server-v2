package edu.ucsb.cs56.games.client_server.v2.client.Models;

/**
 * A message object for use in javaclient, rendering message history
 * keeps track of user, makes it easy to determine if the text should be bold or italicised, stores more data than a string could
 *
 * @author David Roster
 * @author Harrison Wang
 * @version for CS56, Spring 2017
 */

public class MessageModel{
    String message;
    String author;
    boolean privateMessage;
    boolean outgoing;
    int style;
    
    //message: the actual message content
    //from: the sender of the message
    //to: if it's private, where it went
    //privateMessage: if the message was sent privately
    //style: 0=plain, 1=bold, 2=italics, 3=both

    /**
     * constructor
     * @param MSG message to print
     * @param AUTHOR author of message
     * @param PRIVATE if message is private
     * @param OUTGOING if message was private, is it outgoing or incoming
     */
    public MessageModel(String MSG, String AUTHOR, boolean PRIVATE, boolean OUTGOING) {
        message = MSG;
        author = AUTHOR;
        privateMessage = PRIVATE;
        outgoing = OUTGOING;
        style = 0;

        //if it's a private message, make it bold
        //if it's a /me, make it italic
        if(privateMessage)
            style=1;
        else if(message.indexOf("/me ") == 0)
            style=2;
    }
    /**
     * We override the toString method for better printing of 
     * messages incoming and outgoing messages
     *@param None
     *PrivateMessage - Font is in bold
     *OwnMessage - Font is italicized 
     */
    @Override
    public String toString() {
        String string = "";
        if(style == 2)
            string += "<i>";
        if(privateMessage) {
            string += "<b>";
            if(outgoing)
                string += "(to "+author+")";
            else
                string += "(from "+author+")";
        } else {
            string += author;
        }
        if(style == 2)
            string += " "+message.substring(4)+"</i>";
        else
            string += ": "+message;
        if(privateMessage)
            string += "</b>";
        return string;
    }
}
