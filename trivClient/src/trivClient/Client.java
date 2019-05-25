package trivClient;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.Socket;
import java.util.function.Consumer;
import java.util.ArrayList;
import java.util.Random;
import java.util.Collections;


public class Client {

    //gets the local host as the ip address
    private int Port = 5555;   
    private InetAddress IP ;

    Connection connection = new Connection();
    private Consumer<Serializable> callback;
    boolean connected = false;
    ArrayList<Integer> player_scores = new ArrayList<Integer>();
    int num_players;
    boolean gameOver;

    //string for question, arraylist<string> for answers
    String question;
    ArrayList<String> answers = new ArrayList<String>();
    ArrayList<String> randomized_answers = new ArrayList<String>();

    //default constructor
    public Client (Consumer<Serializable> callback) throws Exception {
    	try{
    	 IP =  InetAddress.getByName("127.0.0.1");
    	}
    	catch(Exception e){
    		
    	}
        this.callback = callback;
        num_players= 0;
        startConnection();
        
        for(int i = 0; i < 4; i++) {
        	player_scores.add(0);
        }
        
    }


    //send data, expecting to send one of the answers based on button clicked
    public void sendInfo(Serializable data) {
        try {
            connection.output.writeObject(data);
        }
        catch (Exception e) {
//            e.printStackTrace();
            System.out.println("Issue writing out of client");
        }
    }

    //start connection
    public void startConnection() throws Exception {
        connection.start();
        System.out.println("start");
        randomized_answers.add("1");
        randomized_answers.add("2");
        randomized_answers.add("3");
        randomized_answers.add("4");

        
        connected = true;
    }

    //stop connection
    public void stopConnection() throws Exception {
        this.connected = false;
        if (connection.s!= null) {
            if (connection.input != null) {
                connection.input.close();
            }
            if (connection.output != null) {
                connection.output.close();
            }
            connection.s.close();
        }
    }




    //inner class
    class Connection extends Thread {
        Socket s;
        ObjectInputStream input;
        ObjectOutputStream output;

        public void run() {
            try {
                s = new Socket(IP,Port);

                output = new ObjectOutputStream(s.getOutputStream());
                input = new ObjectInputStream(s.getInputStream());
                s.setTcpNoDelay(true);

                System.out.println("New connection client created.");

                //take in input
                while(connected) {

                    //will receive num players to check if there are 4 players
                    //will receive player scores as an arraylist<int>
                	Serializable newPlayers;
                	Serializable new_scores;

                    if (num_players < 4) {
                        //the # of players
                         newPlayers = (Serializable) input.readObject();
                         num_players = (int) newPlayers;
                        //this is the set of scores for each player (set by indices,
                        //so like P1 should always be index 0, P2 should be index 1, etc...
                        for(int i = 0; i < 4; i++) {
                            new_scores = (Serializable) input.readObject();
                        	player_scores.set(i, (int) new_scores );
                        }
                        callback.accept("less than 4 players");
                    }
                    //will receive question as string
                    //will receive answers as arraylist<string>
                    if (num_players == 4) {
                        //this is the question picked by the server
                        Serializable new_question = (Serializable) input.readObject();
                        question = (String)new_question;

                        //this is the answer related to the question
                        Serializable new_answers = (Serializable) input.readObject();
                        answers = (ArrayList)new_answers;

                        setRandomized_answers();
                        callback.accept("question and answers received from server");
                        

                        //this is going to be where we do a SendInfo call, but this needs to be related to
                        //a button click. after, then we calculate round from Server side
                        //and this will push the num_players and player_scores


                        //the # of players
                         newPlayers = (Serializable) input.readObject();
                        num_players = (int)newPlayers;
                        
                        //this is the set of scores for each player (set by indices,
                        //so like P1 should always be index 0, P2 should be index 1, etc...
                        for(int i = 0; i < 4; i++) {
                            new_scores = (Serializable) input.readObject();
                        	player_scores.set(i, (int) new_scores );
                        }
                        
                    }

                    callback.accept("scores received");
                }


            }
            catch (Exception e) {
                if (s !=null) {
                    System.out.println("Client was closed.");
                    e.printStackTrace();
                }
                else {
                    
                    System.out.println("Client could not find server and was closed.");
                }

            }//end of try/catch
           

        }//end of run

    }//end of connection class


    //this will randomize the answers received from server and put into randomized_answers
    public void setRandomized_answers() {

        ArrayList<String> shuffled_answers = new ArrayList<String>();

        Collections.shuffle(answers);
        shuffled_answers = (ArrayList<String>) answers.clone();
        
        for(int i =0; i<shuffled_answers.size();i++) {
        	randomized_answers.set(i, shuffled_answers.get(i)); 
        }
        
//        for (int i=0; i<shuffled_answers.size();i++) {
//            System.out.println(shuffled_answers.get(i));
//        }



    }

    //clear out the randomized answers
    public void clearRandomized_answers() {
        randomized_answers.clear();
    }


    //checks if the game is over. game is over if someone has 10 or more points
    public void gameOver() {
        for (int i=0; i <  player_scores.size(); i++) {
            if (player_scores.get(i) >= 10) {
                gameOver = true;
                return;
            }
        }
        gameOver = false;
    }
}