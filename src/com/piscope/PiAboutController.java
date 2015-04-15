package com.piscope;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class PiAboutController implements Initializable {
	// About Variables
	@FXML
	Button aboutOk;
	
	@FXML
	Label piVersion=new Label();

	public PiAboutController() {
		// TODO Auto-generated constructor stub
		System.out.println("Hello");
		
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
		dialogStage.initStyle(StageStyle.UTILITY);
		dialogStage.showAndWait();
		
	}

	@FXML
	void aboutClose() {
		aboutOk.getScene().getWindow().hide();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		piVersion.setText(PiMain.PiVersion);
		
	}

}
