cs56-games-client-server
========================
A simple client-server program that allows people to play iconic online games like Tic-tac-toe and Gomoku. Chat and observation functions are included.

How to Run
==========
After compiling, use "ant run-client" and "ant run-server" to start a client or a server respectively.
Only 1 server is necessary, but many clients can be created to play and spectate in the server ran lobby.Currently, this project is only runnable on one computer. In the future, we will expand functionality to be able to play on multiple csil computers.

project history
===============
Fall 2017, Hong Wang & David Roster:

V2:A new version of cs56-games-client-server that is identical on the outside, but substantially refactored inside to have better MVC separation.

Refactored JavaClient and other complex classes to create a more easily understood code while maintainingfunction. Javdoc descriptions have been added for every class and nearly every method. These descriptions will provide helpful insight when deciphering this beauty of a beast project.

We also changed the structure of classes by having just a client & Server directory with their respected Views, models, and controller directories inside. THis can be made very clear by typing "tree" into our root directory called v2 (cs56/cs56-games-client-server-v2/src/edu/ucsb/cs56/games/client_server/v2). WE believed this would help with future user understanding.


Tips
==============
The code works great and does it's job to near perfection. However, it can be difficult to understand all the mechanisms of the class due to large complexity and number of classes. The main classes that handle this project are JavaClient (and its respected helper classes recently refactored) and JavaServer (which will be refactored shortly).

Since there is a lot of classes some basic commands provided here can help display useful info.
-> type "tree" into command line and it will illustrate the tree-like pattern of classes from that repo a	nd on
-> type "grep -Irne 'whatever string value you want to search for' " .This command will present all files 	that include that string on your terminal command with line numbers and color coded
-> type "cat 'filename' " to print whatever file you want to on the command line. You can print multiple files as well because cat stands for concatinate. 

Breakdown of Basic Repo Names
=================================
*Both the main client and server repo's have these sub directories because they keep code cleaner and follow the MVC design pattern. Here's a link that will explain more ... https://www.tutorialspoint.com/design_pattern/mvc_pattern.htm

3 main directories:
-games -> controls the game function for our application
-client -> handles the client side 
-server -> handles server side

Controller -> This stands for Logic which means that most of our logic handling 	functions and raw code are in here... have fun

Model -> This is where the data of our actual game is processed and sent between 	controllers for processing.

Views -> This provides both our GUI application for Server and Client. Server is 	able to run on the command line but that's not as cool.  
