
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.geometry.Pos;
import javafx.scene.text.*;
import javafx.scene.image.*;

import java.net.InetAddress;
import java.util.HashMap;


public class ServerMain extends Application {

    private Stage welcomeStage, playStage, challengeStage;
    private Text welcomeText, portInputText, ipInputText;
    private Button startButton, checkResult, closeButton;
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
    

    private static final Integer STARTTIME =20;
    private Timeline timeline;
    private Label timeLBL = new Label();
    private Integer secs = 	STARTTIME;
    MediaPlayer mediaPlayer;

	HashMap<String, Scene> sceneMap;

    private Client thisClient;

    @Override
    public void start(Stage primaryStage) throws Exception{

        //////////////////          welcome page            //////////////////
		sceneMap = new HashMap<String,Scene>();

		timeLBL = new Label();      
		timeLBL.setText("countdown: 10");
		timeLBL.setFont(Font.font(30));
		timeLBL.setTextFill(Color.RED);
        welcomeStage = primaryStage;
        welcomeStage.setTitle("This is the client.");

        BorderPane startPane = new BorderPane();
        startPane.prefHeight(500);
        startPane.prefWidth(500);
        checkResult = new Button("result");
        startButton = new Button("start");
        startPane.setCenter(startButton);


        Scene welcomeScene = new Scene(startPane);
        sceneMap.put("start",welcomeScene );
        welcomeStage.setScene(welcomeScene);
        
        startButton.setOnAction(event ->{
        
        	try {
				thisClient= createClient();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	welcomeStage.setScene(sceneMap.get("play"));
        });
        
        
        //////////////////          play page            //////////////////

        BorderPane playPane = new BorderPane();
        Scene playScene = new Scene(playPane, 400,600);
        sceneMap.put("play",playScene );
        
        question = new Text("waitting for question");
        answer1 = new Button();
        answer2 = new Button();
        answer3 = new Button();
        answer4 = new Button();
        
        VBox moves = new VBox(timeLBL, question,answer1,answer2,answer3,answer4,result);
        moves.setSpacing(10);
        moves.setAlignment(Pos.CENTER);

        playPane.setCenter(moves);
        playAgainButton.setVisible(false);

        answer1.setOnAction(event -> {
            thisClient.sendInfo(thisClient.randomized_answers.get(0));
    		answer1.setDisable(true);
    		answer2.setDisable(true);
    		answer3.setDisable(true);
    		answer4.setDisable(true);
    		
//            thisClient.sendInfo(0);
        });

        answer2.setOnAction(event -> {
            thisClient.sendInfo(thisClient.randomized_answers.get(1));
    		answer1.setDisable(true);
    		answer2.setDisable(true);
    		answer3.setDisable(true);
    		answer4.setDisable(true);
//            thisClient.sendInfo(1);
        });

        answer3.setOnAction(event -> {
            thisClient.sendInfo(thisClient.randomized_answers.get(2));
    		answer1.setDisable(true);
    		answer2.setDisable(true);
    		answer3.setDisable(true);
    		answer4.setDisable(true);
//            thisClient.sendInfo(2);
        });

        answer4.setOnAction(event -> {
            thisClient.sendInfo(thisClient.randomized_answers.get(3));
    		answer1.setDisable(true);
    		answer2.setDisable(true);
    		answer3.setDisable(true);
    		answer4.setDisable(true);
//            thisClient.sendInfo(3);
        });
        
//        checkResult.setOnAction(event->{
//        	result.setVisible(true);
//        }
//        );
		answer1.setDisable(true);
		answer2.setDisable(true);
		answer3.setDisable(true);
		answer4.setDisable(true);
		
        welcomeStage.show();
    }
    
    
    private Client createClient() throws Exception {
        return new Client(data -> {
            Platform.runLater(() -> {
                //System.out.println(thisClient.num_players);
                //update question and answers on the GUI
        		timeline  = new Timeline();

            	if(thisClient.num_players ==4) {
            		
            		//here
            		secs = 	STARTTIME;
               // doTime();
            		if(secs >0) {
            		
                	KeyFrame frame  = new KeyFrame(Duration.seconds(2), new EventHandler <ActionEvent>() {
                		
                		@Override
                		public void handle(ActionEvent event) {
                			secs --;
                			if(secs  <= 0) {
                				timeline.stop();
                				//System.out.println("weSHould be here");
                			}
                			else {
                    			timeLBL.setText("coutndown: " +  secs.toString());
                    			
                			}
                		}
                	}, null);
                	
                	timeline.setCycleCount(Timeline.INDEFINITE);
                	timeline.getKeyFrames().add(frame);
                	if (timeline !=null) {
                		timeline.stop();
                	}
            		}
    				timeline.stop();

                	
                	timeline.play();

            	answer1.setDisable(false);
            	answer2.setDisable(false);
            	answer3.setDisable(false);
            	answer4.setDisable(false);
                answer1.setText(thisClient.randomized_answers.get(0));
                answer2.setText(thisClient.randomized_answers.get(1));
                answer3.setText(thisClient.randomized_answers.get(2));
                answer4.setText(thisClient.randomized_answers.get(3));
                result.setText("Player 1: " +thisClient.player_scores.get(0)
                			  +"\n Player 2: " + thisClient.player_scores.get(1)
                			  +"\n Player 3: " + thisClient.player_scores.get(2)
                			  +"\n Player 4: " + thisClient.player_scores.get(3)
                			  +"\n You are: "+ thisClient.playerId);
                question.setText(thisClient.question);
//           	 Thread t = new Thread(new Runnable() {
//
//        		 @Override
//        		 public void run() {
//        			 // TODO Auto-generated method stub
//        			
//
//        		 }
//        		 
//        	 });
//        		t.start();
            	}
            	else {
            		answer1.setDisable(true);
            		answer2.setDisable(true);
            		answer3.setDisable(true);
            		answer4.setDisable(true);
            	}
            	
     			 
            	 
          
            });
        });
    }
    
    
    private void doTime() {
    	timeline  = new Timeline();
    	KeyFrame frame  = new KeyFrame(Duration.seconds(1), new EventHandler <ActionEvent>() {
    		
    		@Override
    		public void handle(ActionEvent event) {
    			secs --;
    			timeLBL.setText("coutndown: " +  secs.toString());
    			if(secs  <= 0) {
    				timeline.stop();
    			}
    		}
    	}, null);
    	
    	timeline.setCycleCount(Timeline.INDEFINITE);
    	timeline.getKeyFrames().add(frame);
    	if (timeline !=null) {
    		timeline.stop();
    	}
    	
    	timeline.play();
    }
//    private void doTime() {
//    	timeline  = new Timeline();
//    	KeyFrame frame  = new KeyFrame(Duration.seconds(1), new EventHandler <ActionEvent>() {
//    		
//    		@Override
//    		public void handle(ActionEvent event) {
//    			secs --;
//    			
//    			//else {
//    			timeLBL.setText("coutndown: " +  secs.toString());
////    			if(secs  <= 0) {
////    				timeline.stop();
////    				thisClient.sendInfo("-99");
////    				
////    			}
//    		//	}
//    		}
//    	}, null);
//    	timeline.setCycleCount(Timeline.INDEFINITE);
//    	timeline.getKeyFrames().add(frame);
//    	if (timeline !=null) {
//    		timeline.stop();
//    	}
//    	
//    	timeline.play();
//    }


    public static void main(String[] args) {
        launch(args);
    }


}