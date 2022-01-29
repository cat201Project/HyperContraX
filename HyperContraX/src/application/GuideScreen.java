//Guide Screen source code
package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class GuideScreen {

	// Button to main
	@FXML
    private Button buttonMain;
	
	// When clicked, return back to main screen
    @FXML
    void exitGuide(ActionEvent event) {
    	// Close stage after button clicked
        Stage stage = (Stage) buttonMain.getScene().getWindow();
        stage.close();
    }

}