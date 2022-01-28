//This is the source code for the first screen project when launching the application.
package application;

// Import java and javaFX library
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

//import java.io.IOException;

import javafx.event.ActionEvent;


public class FirstScreen implements Initializable{
	// Variable Declaration
	@FXML
    private Label HighScore;
	
    @FXML
    private TextField Player_Name;
	
	@FXML
    private Button closedButton;
	
    @FXML
    private Button Button_Play;
    
    // This is the method that linking the FXML file to the SceneBuilder
    //This is the button that will appear as 'PLAY' on the first screen
    @FXML
    void ButtonPlay(ActionEvent event) throws Exception{
    	//Add SecondScreen
    	String name = Player_Name.getText();
    	new SecondScreen(name);	
    }

    //This is the EXIT button that will allow to close the application
    @FXML
    void ButtonExit(ActionEvent event) {
    	// Close stage after button clicked
        Stage stage = (Stage) closedButton.getScene().getWindow();
        stage.close();
    }
    
    // This linking to the button that allow us to view the High score
    @FXML
    void PreviewHighScore(ActionEvent event) throws FileNotFoundException{
    	

    	// pass the path to the file as a parameter
        File file = new File("C:\\Users\\nmnor\\eclipse-workspace\\NameScore.txt");
        try (Scanner sc = new Scanner(file)) {
			while (sc.hasNextLine())
			  HighScore.setText(sc.nextLine());
		}
    }
    
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// Disable button when the text are empty
		Button_Play.disableProperty().bind(Player_Name.textProperty().isEmpty());
		
	}
}