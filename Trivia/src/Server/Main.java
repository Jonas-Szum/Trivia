package Server;


import java.util.HashMap;
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
	HashMap<String, Scene> sceneMap;
    Scene welcomeScene;
    BorderPane startPane;
    private Server gameServer ;

    @Override
    public void start(Stage primaryStage) throws Exception{
    	
    	sceneMap = new HashMap<String,Scene>();

        startPane = new BorderPane();
        startPane.prefHeight(500);
        startPane.prefWidth(500);        
        welcomeStage = primaryStage;
        welcomeStage.setTitle("This is the Server.");

    	onButton = new Button("turn on Server");
        startPane.setCenter(onButton);

        onButton.setOnAction(event ->{
        	gameServer= createServer();
        	onButton.setDisable(true);
        	//welcomeStage.setScene(sceneMap.get("play"));
        });
        
        
        welcomeScene = new Scene(startPane);
        sceneMap.put("start",welcomeScene );
        welcomeStage.setScene(welcomeScene);
    	
        welcomeStage.show();

    }
    
    
    //make a new server
    private Server createServer() {
        return new Server(data -> {
            Platform.runLater(() -> {
               
            });
        });
    }



    public static void main(String[] args) {
        launch(args);
    }
}