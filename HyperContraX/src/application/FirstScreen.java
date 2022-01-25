package application;

// Import java and javaFX library
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;


import javafx.event.ActionEvent;

public class FirstScreen implements Initializable{

    @FXML
    private TextField Player_Name;
	
	@FXML
    private Button closedButton;
	
    @FXML
    private Button Button_Play;
    

    @FXML
    void ButtonPlay(ActionEvent event) throws Exception{
    	//Add SecondScreen
    }

    @FXML
    void ButtonExit(ActionEvent event) {
    	// Close stage after button clicked
        Stage stage = (Stage) closedButton.getScene().getWindow();
        stage.close();
    }
    

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// Disable button when the text are empty
		Button_Play.disableProperty().bind(Player_Name.textProperty().isEmpty());
		
	}
}



