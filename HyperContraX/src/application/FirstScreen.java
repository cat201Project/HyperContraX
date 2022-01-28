//This is the source code for the first screen project when launching the application.
package application;

// Import java and javaFX library
import java.sql.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;


public class FirstScreen implements Initializable{
	
	// Variable Declaration
    @FXML
    private Label Score;
    
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
    
    // This linking to the button that allow user to view the High score
    @FXML
    void previewScore(ActionEvent event) throws ClassNotFoundException{
    	
    	Connection con;
    	ResultSet rs;
    	
    	// Set up the database with mySQL
    	try {
    		Class.forName("com.mysql.cj.jdbc.Driver");
    		con = DriverManager.getConnection("jdbc:mysql://localhost/mysql", "root", "");
    		Statement stmt = con.createStatement();
    		
    		// SELECT query
            String q1 = "select name, score from highscore where score = (select max(score) from highscore)";
            rs = stmt.executeQuery(q1);
    		if(rs.next())
    		{
    			HighScore.setText(rs.getString(1));
    			Score.setText(rs.getString(2));
    		}

    	}catch (SQLException e) {System.out.println(e);};
    }
    
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// Disable button when the text are empty
		Button_Play.disableProperty().bind(Player_Name.textProperty().isEmpty());
		
	}
}