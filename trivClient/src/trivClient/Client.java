package trivClient;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.Socket;
import java.util.function.Consumer;
import java.util.ArrayList;
import java.util.Random;


public class Client {

    //gets the local host as the ip address
    private int Port = 5555;
    private InetAddress IP = InetAddress.getLocalHost();
    Connection connection = new Connection();
    private Consumer<Serializable> callback;
    boolean connected = false;
    ArrayList<Integer> player_scores = new ArrayList<Integer>();

    //string for question, arraylist<string> for answers
    String question;
    ArrayList<String> answers = new ArrayList<String>();

    ArrayList<String> randomized_answers = new ArrayList<String>();

    //default constructor
    public Client(Consumer<Serializable> callback) {
        this.callback = callback;
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
                s = new Socket(getIP(),getPort());

                output = new ObjectOutputStream(s.getOutputStream());
                input = new ObjectInputStream(s.getInputStream());
                s.setTcpNoDelay(true);

                System.out.println("New connection client created.");

                //take in input
                while(connected) {
                    //will receive question as string
                    //will receive answers as arraylist<string>
                    //will receive player scores as an arraylist<int>

                    //this is the question picked by the server
                    Serializable new_question = (Serializable) input.readObject();
                    question = new_question;

                    //this is the answer related to the question
                    Serializable new_answers = (Serializable) input.readObject();
                    answers = new_answers;

                    //this is the set of scores for each player (set by indices,
                    //so like P1 should always be index 0, P2 should be index 1, etc...
                    Serializable new_scores = (Serializable) input.readObject();
                    player_scores = new_scores;

                    callback.accept("question and answers received from server");
                    activePlayers.clear();
                }


            }
            catch (Exception e) {
                if (s !=null) {
                    System.out.println("Client was closed.");
                }
                else {
//                    e.printStackTrace();
                    System.out.println("Client could not find server and was closed.");
                }

            }//end of try/catch


        }//end of run

    }//end of connection class


    //this will randomize the answers received from server and put into randomized_answers
    public void setRandomized_answers() {

        Random random = new Random();
        int answer_index_size = answers.size();

        for (int i=answer_index_size; i>0; i--) {
            int randAns = random.nextInt(i);
            randomized_answers.add(answers.get(i));
        }
    }

    //clear out the randomized answers
    public void clearRandomized_answers() {
        randomized_answers.clear();
    }
}