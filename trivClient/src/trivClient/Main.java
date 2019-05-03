package trivClient;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.geometry.Pos;
import javafx.scene.text.*;
import javafx.scene.image.*;

import java.net.InetAddress;


public class Main extends Application {

    private Stage welcomeStage, playStage, challengeStage;
    private Text welcomeText, portInputText, ipInputText;
    private Button systemsButton, playButton, closeButton;
    private Button answer1, answer2, answer3, answer4;
    private TextField portRequest, ipRequest;
    private Text question;
    //    private Text selfScoreText, theirScoreText;
    //private TextField challengePlayer = new TextField("Input player ID and enter to challenge");
    private Text gameState = new Text("");
    private Text currentPlayers = new Text("Current # Players:");
    private Text myPlayerID = new Text("You are player:");
    private int portNumber;
    private InetAddress ipNumber;
    private Text result = new Text();
    private Text lonePlayer = new Text("Someone has left. Close client.");
    private Button playAgainButton = new Button("Play again");
    private Text players = new Text("Num Players:");


    private Client thisClient = createClient();

    @Override
    public void start(Stage primaryStage) throws Exception{

        //////////////////          welcome page            //////////////////

        welcomeStage = primaryStage;
        welcomeStage.setTitle("This is the client.");
        BorderPane welcomePane = new BorderPane();
        Scene welcomeScene = new Scene(welcomePane, 400,600);

        welcomeText = new Text("Client - RSPLS");
        welcomeText.setFont(Font.font("Verdana", 20));

        portRequest = new TextField("Input desired port here");
        ipRequest = new TextField("Input desired IP here");
        portRequest.setMaxWidth(200);
        ipRequest.setMaxWidth(200);

        portInputText = new Text("Port: ");
        ipInputText = new Text("IP: ");

        systemsButton = new Button("Confirm settings");
        playButton = new Button("Let's play!");
        closeButton = new Button("End client");

        VBox Cproperties = new VBox(welcomeText, portRequest,ipRequest, systemsButton, playButton, portInputText, ipInputText, closeButton);
        Cproperties.setSpacing(10);
        Cproperties.setAlignment(Pos.CENTER);

        welcomePane.setTop(Cproperties);
        welcomeStage.setScene(welcomeScene);

        //////////////////          play page            //////////////////
        playStage = new Stage();
        playStage.setTitle("Game in progress...");
        BorderPane playPane = new BorderPane();
        Scene playScene = new Scene(playPane, 400,600);

        Answer1 = new Button();
        Answer2 = new Button();
        Answer3 = new Button();
        Answer4 = new Button();
        

        // Rock.setGraphic(new ImageView(rock));
        // Paper.setGraphic(new ImageView(paper));
        // Scissors.setGraphic(new ImageView(scissors));
        // Lizard.setGraphic(new ImageView(lizard));
        // Spock.setGraphic(new ImageView(spock));


//        VBox moves = new VBox(Rock,Paper,Scissors,Lizard,Spock, theirMove, selfScoreText,theirScoreText, winner, closeButton, playAgainButton);
        VBox moves = new VBox(question,answer1,answer2,answer3,answer4,result);
        moves.setSpacing(10);
        moves.setAlignment(Pos.CENTER);

        playPane.setCenter(moves);
        playAgainButton.setVisible(false);

        //TODO: add a scene to wait for players to start the game

        //////////////////          challenge page            ///////////////////
        // challengeStage = new Stage();
        // challengeStage.setTitle("Choose your challenger");
        // BorderPane challengePane = new BorderPane();
        // Scene challengeScene = new Scene(challengePane,400,600);
        // challengePlayer.setMaxWidth(300);

        // VBox challenger = new VBox(myPlayerID, currentPlayers, challengePlayer, closeButton);
        // challenger.setSpacing(20);
        // challenger.setAlignment(Pos.CENTER);

        // challengePane.setCenter(challenger);

        //sets the buttons for the client
        systemsButton.setOnAction(event -> {
            try {
                portNumber = Integer.parseInt(portRequest.getText());
                ipNumber = InetAddress.getByName(ipRequest.getText());

                portInputText.setText("Port: " + portNumber);
                ipInputText.setText("IP: " + ipNumber);

                thisClient.setPort(portNumber);
                thisClient.setIP(ipNumber);


                thisClient.setValid(true);
                System.out.println("Port: " + thisClient.getPort() + " IP: " + thisClient.getIP());
            }
            catch (Exception e) {
                portInputText.setText("Port: " + " needs to be a number");
                ipInputText.setText("IP: " + "needs to be an address");
            }
        });

        playButton.setOnAction(event -> {
            if(thisClient.isValid()) {
                try {
                    thisClient.startConnection();
                    thisClient.connected = true;
                    primaryStage.setScene(playPane);
                }
                catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Issue starting the connection. Valid settings.");
                }
            }
        });

        closeButton.setOnAction(event -> {
            try {
                thisClient.stopConnection();
                primaryStage.close();
            }
            catch (Exception e) {
                e.printStackTrace();
                System.out.println("Issue with closing the connection of the client.");
            }
        });


        // challengePlayer.setOnAction(e -> {
        //     try {
        //         Integer i = Integer.parseInt(challengePlayer.getText());
        //         thisClient.sendInfo(i);
        //     }
        //     catch (Exception a){
        //         a.printStackTrace();
        //         challengePlayer.setText("Needs to be a number");
        //     }

        // });

        answer1.setOnAction(event -> {
            thisClient.sendInfo("Answer");
            thisClient.sendInfo(0);
        });

        answer2.setOnAction(event -> {
            thisClient.sendInfo("Answer");
            thisClient.sendInfo(1);
        });

        answer3.setOnAction(event -> {
            thisClient.sendInfo("Answer");
            thisClient.sendInfo(2);
        });

        answer4.setOnAction(event -> {
            thisClient.sendInfo("Answer");
            thisClient.sendInfo(3);
        });

        // Spock.setOnAction(event -> {
        //     thisClient.sendInfo("Spock");
        // });

        playAgainButton.setOnAction(event -> {
            thisClient.sendInfo("Play");

            winner.setText("Winner: ");
            primaryStage.setScene(challengeScene);

            playAgainButton.setVisible(false);
        });


        primaryStage.show();
    }

    private Client createClient() {
        return new Client(data -> {
            Platform.runLater(() -> {
                
                //update question and answers on the GUI
                answer1.setText(thisClient.answers.get(1));
                answer2.setText(thisClient.answers.get(2));
                answer3.setText(thisClient.answers.get(3));
                answer4.setText(thisClient.answers.get(4));

                question.setText(thisClient.question);

                // //returns the whole string of players' scores and their moves
                // gameState.setText(thisClient.returnThisString);

                // //return the number of players
                // currentPlayers.setText("Current # Players:" + thisClient.numPlayers);

                //return your ID so you cannot challenge yourself
                myPlayerID.setText("You are player: " + thisClient.myPlayerID);

                if (thisClient.numPlayers == -1) {
                    winner.setText("A player left. Please close client.");
                }

//                if (selfScore == 3) {
//                    winner.setText("Winner: Me!!");
//                    playAgainButton.setVisible(true);
//                }
//                else if (theirScore == 3) {
//                    winner.setText("Winner: Other guy...");
//                    playAgainButton.setVisible(true);
//                }
            });
        });
    }


    public static void main(String[] args) {
        launch(args);
    }


}