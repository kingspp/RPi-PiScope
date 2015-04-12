package com.piscope;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class PiPreferenceController {
	 private final String message ;
	    
	    @FXML
	    private Label messageLabel ;
	    
	    @FXML
		void initialize() {
	        messageLabel.setText(message);
	    }
	    
	   
	    
	    public PiPreferenceController(String message) {
	        this.message = message ;
	    }
	    
	   
	    
	    @FXML
	    public void close() {
	        messageLabel.getScene().getWindow().hide();
	    }

}
