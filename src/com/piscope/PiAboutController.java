package com.piscope;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class PiAboutController {
	// About Variables
	@FXML
	Button aboutOk;

	public PiAboutController() {
		// TODO Auto-generated constructor stub
	}

	public void dialogBuild() throws IOException {
		Stage dialogStage = new Stage();
		FXMLLoader loader = new FXMLLoader(getClass().getResource(
				"PiAbout.fxml"));
		loader.setController(new PiAboutController());
		BorderPane root = (BorderPane) loader.load();

		// PiPreferenceController controller = (PiPreferenceController)
		// loader.getController();
		Scene scene = new Scene(root);
		dialogStage.setScene(scene);
		dialogStage.showAndWait();
	}

	@FXML
	void aboutClose() {
		aboutOk.getScene().getWindow().hide();
	}

}
