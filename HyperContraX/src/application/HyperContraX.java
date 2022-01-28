package application;

// Import java and javaFX library
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

// main class
public class HyperContraX extends Application{
	
	@Override
	public void start(Stage stage) throws IOException {
		
		// Load the FXML file from MainController.fxml
        FXMLLoader fxmlLoader = new FXMLLoader(HyperContraX.class.getResource("MainController.fxml"));
        // Set the scene
        Scene scene = new Scene(fxmlLoader.load());
        // Set the title of the scene
        stage.setTitle("Hyper Contra X");
        // Set the stage with the scene
        stage.setScene(scene);
        // Show the stage to the viewer
        stage.show();
    }
    
	// To launch to the first screen.	
	public static void main(String[] args) {
		launch();
	}
	
}