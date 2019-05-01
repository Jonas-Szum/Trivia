package trivClient;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.Socket;
import java.util.function.Consumer;
import java.util.ArrayList;


public class Client {

    private int port;
    private InetAddress IP;
    Connection connection = new Connection();
    private boolean validSettings = false;
    private Consumer<Serializable> callback;
    boolean connected = false;
    int p1Score, p2Score, p3Score;
    int numPlayers;
    int myPlayerID;
    String p1Move, p2Move, p3Move;
    String returnThisString;
    ArrayList activePlayers = new ArrayList();

    //default constructor
    public Client(Consumer<Serializable> callback) {
        this.callback = callback;
    }

    //get and set of port
    public int getPort() {
        return port;
    }
    public void setPort(int newPort) {
        this.port = newPort;
    }

    //get and set of IP
    public InetAddress getIP() {
        return IP;
    }
    public void setIP(InetAddress newIP) {
        this.IP = newIP;
    }

    //get and set of validsettings
    public boolean isValid() {
        return validSettings;
    }
    public void setValid(boolean valid) {
        this.validSettings = valid;
    }

    //send data
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
                    //will receive current number of players
                    //will receive own player iD
                    //will receive a string full of all the information and will put into GUI

                    Serializable players = (Serializable) input.readObject();
                    numPlayers = (Integer) players;

                    for (int i=0; i < numPlayers; i++) {
                        Serializable areTheyPlaying = (Serializable) input.readObject();
                        activePlayers.add(areTheyPlaying);
                    }

                    Serializable myID = (Serializable) input.readObject();
                    myPlayerID = (Integer) myID;

                    Serializable playerInfo = (Serializable) input.readObject();
                    returnThisString = (String) playerInfo;

                    callback.accept("Changes made");
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
}