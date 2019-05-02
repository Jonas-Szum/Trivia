package Server;

import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Consumer;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;

public class Server {

    private int port;
    private ServerSocket server_socket;
    boolean active = false;
    boolean validPort = false;
    volatile ArrayList<Integer> playerScores;
    volatile ArrayList<String>  playerMoves;
    Consumer<Serializable> callback;
    boolean gameOver = false;
    String winner;
    int numPlayers = 0;
    int maxPlayers = 4;
    int clientIDs = 1;
    volatile ArrayList<Integer> answerOrder;
    volatile boolean calculated = false;
    volatile QnA myTrivia;
    volatile String question = "";
    volatile ArrayList<String> answers;

    //store the active connections
    ArrayList<Connection> connectionList = new ArrayList<Connection>();

    public Server(Consumer<Serializable> callback) {
        this.callback = callback;
        playerScores = new ArrayList<Integer>(); //can now handle any amount of players
        playerMoves = new ArrayList<String>();
        for(int i = 0; i < maxPlayers; i++)
        {
            playerScores.add(0);
            playerMoves.add("");
            myTrivia = new QnA();
            myTrivia.roll_question();
            question = myTrivia.get_question();
            answers = myTrivia.get_answers();
        }
    }


    //get the port of the server
    int getPort() {
        return port;
    }

    //set the port of the server
    void setPort(int port) {
        if (!active) {
            this.port = port;
        }
        else {
            System.out.println("Cannot change port because server is already on");
        }
    }

    //get status of server
    boolean getActive() {
        return active;
    }

    //starts looking for connections via Client subclass
    void turnOnServer() {
        this.active = true;

        try {server_socket = new ServerSocket(port); }
        catch (Exception e) { e.printStackTrace(); }

        //creates four players
        for (int i=0; i < maxPlayers; i++) {
            Connection newClient = new Connection();
            newClient.playerID = clientIDs;
            clientIDs++;
            newClient.start();
            connectionList.add(newClient);
        }
    }

    //close all of the existing threads connected to the server
    void turnOffServer() throws Exception {
        for (int i = 0; i < connectionList.size(); i++) {
            if (connectionList.get(i).s != null) {
                if (connectionList.get(i).output !=null && connectionList.get(i).input.read() != -1) {
                    connectionList.get(i).output.close();
                }
                if (connectionList.get(i).input !=null && connectionList.get(i).input.read() != -1) {
                    connectionList.get(i).input.close();
                }
                connectionList.get(i).s.close();
            }
        }
        if (server_socket !=null ) {
            server_socket.close();
        }
        this.active = false;
        System.out.println("All connections closed.");
    }

    //helper method to get a socket;
    private Socket receiveClient() {
        try {
            return server_socket.accept();
        }
        catch (Exception e) {
            System.out.println("Server socket closed before able to accept.");
        }
        return null;
    }

    //inner class for connecting server with threads
    class Connection extends Thread {
        Socket s;
        ObjectInputStream input;
        ObjectOutputStream output;
        String ClientMove;
        boolean madeMove = false;
        int playerScore = 0;
        int playerID;
        int currentOpponent = -1;
        volatile boolean alreadyInGame = false;
        public void run() {
            try {
                //get a socket from server.accept()
                s = receiveClient();

                //update the input and output streams
                if (s != null) {
                    ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
                    ObjectInputStream in = new ObjectInputStream(s.getInputStream());
                    this.output = out;
                    this.input = in;
                    numPlayers++;
                    updateClients();
                    callback.accept("Found connection");

                    while(true) {
                    	out.writeObject(question);
                    	out.writeObject(answers);
                        //send the question, answer combo
                    	//wait to recieve answer
                    	Serializable playerAnswer = (Serializable)in.readObject();
                    	calculated = false; //after first player answers again, calculated is false
                    	answerOrder.add(playerID);
                    	if(answerOrder.size() == 4)
                    		calculateRound(); //if you are not versing anyone, currentOpponent is -1, go to calculateRound to see why
                    	else
                    		while(calculated == false) {}
                    }
                } //end of if not null

            } //end of try
            catch (Exception e) {
                System.out.println("A player has disappeared");
                s = null;

                //numPlayers = -1; //holder for if we were to lose a player, then we are going to end game
                numPlayers = -1; //new to this version
                callback.accept("Not enough players to play.");
                updateClients();

            } //end of catch
        } //end of run
    } //end of connection method

    //calculates who won the round, and the game
    private synchronized void calculateRound() {
    //send information to all 4 players
    
    myTrivia.roll_question();
    question = myTrivia.get_question();
    answers = myTrivia.get_answers();
    answerOrder.clear();
    calculated = true;
    } //end of calculateround


    //send the new scores out to the 2 clients
    private synchronized void updateClients() {
        try {
            for (Connection conn : connectionList) { //update everyone
                if (conn != null) {
                    if (conn.s != null) {
                        String playerInformation = "Not yet Implemented";
                        conn.output.writeObject(numPlayers);		//send how many players are connected
                        for(int i = 0; i < numPlayers; i++)
                        {
                            conn.output.writeObject(connectionList.get(i).alreadyInGame); //send all users information regarding whether or not a user is in a game already
                        }
                        conn.output.writeObject(conn.playerID); //write the connection's playerID
                        conn.output.writeObject(playerInformation); //send all user turns to clients
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("A client has closed.");
        }
    } //end of updateClients
}