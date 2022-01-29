// This is the source code for the first screen project when launching the application.
package application;

// Import java and javaFX library
import java.sql.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;


public class FirstScreen implements Initializable{
	
	// Variable declaration
    @FXML
    private Label score;
    
	@FXML
    private Label highScoreName;
	
    @FXML
    private TextField playerName;
	
	@FXML
    private Button closedButton;
	
    @FXML
    private Button buttonPlay;
    
    // This is the method that linking the FXML file to the SceneBuilder
    // This is the button that will appear as 'PLAY' on the first screen
    @FXML
    void ButtonPlay(ActionEvent event) throws Exception{
    	//Add SecondScreen
    	String name = playerName.getText();
    	new SecondScreen(name);	
    }

    // This is the EXIT button that will allow to close the application
    @FXML
    void ButtonExit(ActionEvent event) {
    	// Close stage after button clicked
        Stage stage = (Stage) closedButton.getScene().getWindow();
        stage.close();
    }
    
    // Function that allow user to view the High score when the button is clicked
    @FXML
    void PreviewScore(ActionEvent event) throws ClassNotFoundException{
    	
    	Connection con;
    	ResultSet rs;
    	
    	// Set up the database with mySQL
    	try {
    		Class.forName("com.mysql.cj.jdbc.Driver");
    		// Create a connection with the mysql database from the localhost
    		con = DriverManager.getConnection("jdbc:mysql://localhost/mysql", "root", "");
    		
    		// Static sql statement to create and execute general purpose SQL statement using Java programs
    		Statement stmt = con.createStatement();
    		
    		// SELECT query to view name and score where the score are the maximum
            String q1 = "select name, score from highscore where score = (select max(score) from highscore)";
            rs = stmt.executeQuery(q1);
    		if(rs.next())
    		{
    			highScoreName.setText(rs.getString(1));
    			score.setText(rs.getString(2));
    		}

    	}catch (SQLException e) {System.out.println(e);};
    }
    
    //This is the Guide screen linking from the Main screen
    @FXML
    void ShowGuide(ActionEvent event) throws IOException {
    	
    	// Load the FXML file from MainController.fxml
        FXMLLoader fxmlLoader = new FXMLLoader(HyperContraX.class.getResource("GuideController.fxml"));
        Parent root = fxmlLoader.load();
        
        fxmlLoader.getController();
        Stage stage = new Stage();
        
        // Set the stage with the scene
        stage.setScene(new Scene(root));
        // Set the title of the scene
        stage.setTitle("Game Guide");
        // Show the stage to the viewer
        stage.show();
    }
    
    
    
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// Disable button when the text are empty
		buttonPlay.disableProperty().bind(playerName.textProperty().isEmpty());
		
	}
}