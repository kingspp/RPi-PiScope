package com.piscope;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class PiPreferenceController {

	Properties prop = new Properties();
	InputStream input = null;
	OutputStream output = null;

	@FXML
	final CheckBox HGrid=new CheckBox();

	@FXML
	private Button prefButton;

	public PiPreferenceController() {
		// TODO Auto-generated constructor stub
		
		
		HGrid.setSelected(true);
		
		readProp();

	}

	public void readProp() {
		try {
			input = new FileInputStream("config.properties");
			// load a properties file
			prop.load(input);
			System.out.println(prop.getProperty("HGrid"));
			
			
			//if(prop.getProperty("HGrid")=="true")
			
				
		} 
		catch (IOException e) {e.printStackTrace();} 
		
		finally {
			if (input != null) {
				try {
					input.close();
				} 
				catch (IOException e) {e.printStackTrace();}
			}

		}

	}

	public void writeProp(String key, String value) {
		prop.setProperty(key, value);
	}

	void dialogBuild() throws IOException {
		
		Stage dialogStage = new Stage();
		FXMLLoader loader = new FXMLLoader(getClass().getResource(
				"PiPreference.fxml"));

		loader.setController(new PiPreferenceController());
		

		BorderPane root = (BorderPane) loader.load();
		
		// PiPreferenceController controller = (PiPreferenceController)
		// loader.getController();
		Scene scene = new Scene(root);		
		dialogStage.setScene(scene);
		
		dialogStage.show();
		
		
		
	}

	@FXML
	public void close() {
		// save properties to project root folder
		try {
			output = new FileOutputStream("config.properties");
			if (HGrid.isSelected())
				writeProp("HGrid", "true");
			else if (!HGrid.isSelected())
				writeProp("HGrid", "fasle");
			

			prop.store(output, null);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
		prefButton.getScene().getWindow().hide();

	}
}
