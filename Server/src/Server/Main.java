
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.geometry.Pos;
import javafx.scene.text.*;


//implementation is for Server
public class Main extends Application {

    private Stage welcomeStage;
    private Text welcomeText, portInputText;
    private TextField portRequest;
    private Button onButton, offButton;
    private Text Player1Move, Player2Move;
    private String p1Score, p2Score;
    private String p1Move, p2Move;
    private Text winner = new Text("Winner: ");
    private boolean gameOver = false;
    private int numPlayers = 0;
    private Text totalPlayers = new Text("# Players: " + numPlayers);


    private Integer portNumber = null;

    //this is the server
    private Server gameServer = createServer();

    @Override
    public void start(Stage primaryStage) throws Exception{
////////////////////////////// WELCOME PAGE //////////////////////////////
        //welcome page - creation
        welcomeStage = primaryStage;
        welcomeStage.setTitle("Server - RSPLS");
        BorderPane welcomePane = new BorderPane();
        Scene welcomeScene = new Scene(welcomePane,400,500);

        welcomeText = new Text("Server - RSPLS");
        welcomeText.setFont(Font.font("Verdana",20));

        portRequest = new TextField("Input port and press enter");
        portRequest.setMaxWidth(200);

        onButton = new Button("Turn on the server");
        offButton = new Button("Turn off the server");

        portInputText = new Text("Port number:");

        p1Score = "";
        p2Score = "";
        p1Move = "";
        p2Move = "";

        Player1Move = new Text("Player 1: " + p1Score + "\nMove: " + p1Move);
        Player2Move = new Text("Player 2: " + p2Score + "\nMove: " + p2Move);


        VBox Sproperties = new VBox(welcomeText, onButton, offButton, portInputText);
        Sproperties.setSpacing(10);
        Sproperties.setAlignment(Pos.CENTER);

        VBox gameplay = new VBox(totalPlayers, Player1Move, Player2Move, winner);
        gameplay.setSpacing(10);
        gameplay.setAlignment(Pos.CENTER);
        gameplay.setVisible(false);


        //welcome page - action events
        //makes sure the port entered is a number
        
        
        portInputText.setText("Port number:" + gameServer.getPort()); // do this instantly
        gameServer.turnOnServer(); //will set sefrver to active
        
        
        //set this fixed in server 
//        portRequest.setOnAction(event -> {
//            try {
//                if (!gameServer.active) {
//                    portNumber = Integer.parseInt(portRequest.getText());
//                    portInputText.setText("Port number:" + portRequest.getText());
//                    gameServer.setPort(portNumber);
//                    gameServer.validPort = true;
//                }
//            }
//            catch (Exception e){
//                e.printStackTrace();
//                portInputText.setText("Port number:" + " needs to be a number");
//            }
//        });
         //se this automaticly in server main
        //when port is chosen, start connection on server
//        onButton.setOnAction(event -> {
//            if (gameServer.validPort && !gameServer.active) {
//                gameServer.turnOnServer();
//                gameplay.setVisible(true);
//                onButton.setText("Server is on");
//                offButton.setText("Turn off the server");
//            }
//            else if (gameServer.validPort && gameServer.active) {
//                onButton.setText("Server is already on");
//                portInputText.setText("Port number: " + portNumber);
//            }
//            else {
//                portInputText.setText("Port number: invalid port");
//                System.out.println("Server is either already on, or you have an invalid port");
//            }
//        });

        offButton.setOnAction(event -> {
                try {
                    gameServer.turnOffServer();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                welcomeStage.close();
        });

        //welcome page - structure of page
        welcomePane.setTop(Sproperties);
        welcomePane.setCenter(gameplay);

        welcomeStage.setScene(welcomeScene);
        primaryStage.show();
    }

    //make a new server
    private Server createServer() {
        return new Server(data -> {
            Platform.runLater(() -> {
                numPlayers = gameServer.numPlayers;
                totalPlayers.setText("# Players: " + numPlayers);

                p1Move = gameServer.p1Move;   //tells you if your answer was correct or wrong
                p2Move = gameServer.p2Move;
                p3Move = gameServer.p3Move;
                p4Move = gameServer.p4Move;

                p1Score = Integer.toString(gameServer.p1Score);
                p2Score = Integer.toString(gameServer.p2Score);
                p1Score = Integer.toString(gameServer.p1Score);
                p2Score = Integer.toString(gameServer.p2Score);

                Player1Move.setText("\nPlayer 1: " + p1Score + "\nAnd their answer was: " + p1Move);
                Player2Move.setText("\nPlayer 2: " + p2Score + "\nAnd their answer was: " + p2Move);
                Player3Move.setText("\nPlayer 3: " + p1Score + "\nAnd their answer was:" + p3Move);
                Player4Move.setText("\nPlayer 4: " + p2Score + "\nAnd their answer was: " + p4Move);

                if (gameServer.gameOver) {
                    winner.setText("\nWinner: " + gameServer.winner);
                }

            });
        });
    }



    public static void main(String[] args) {
        launch(args);
    }
}