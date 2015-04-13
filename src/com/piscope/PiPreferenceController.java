package com.piscope;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.Window;

public class PiPreferenceController implements Initializable {

	Properties prop = new Properties();
	InputStream input = null;
	OutputStream output = null;
	PiController picontroller;
	private Scene scene;
	
	
	PiMain main= new PiMain();

	@FXML
	private CheckBox hgrid=new CheckBox();
	
	@FXML
	private CheckBox vgrid=new CheckBox();

	@FXML
	private Button prefButton;

	public PiPreferenceController() {
		// TODO Auto-generated constructor stub	
		

	}
	
	
	

	public void readProp() {
		try {
			input = new FileInputStream("config.properties");
			// load a properties file
			prop.load(input);
			
			String sprop=prop.getProperty("HGrid");
			System.out.println(sprop);
			
			if(sprop.equals("true")){
				hgrid.setSelected(true);
				System.out.println("Hello1");
				//String theme1Url = getClass().getResource("css/theme.css").toExternalForm();
				
				//scene.getStylesheets().add(getClass().getResource("css/theme.css").toExternalForm());
			}
			if(sprop.equals("false")){
				System.out.println("Hello2");			
				hgrid.setSelected(false);
			}
			
			
				
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
		 scene= new Scene(root);		
		dialogStage.setScene(scene);		
		dialogStage.show();	
	}

	@FXML
	public void close() {
		// save properties to project root folder
		try {
			output = new FileOutputStream("config.properties");
			if (hgrid.isSelected())
				writeProp("HGrid", "true");
			else if (!hgrid.isSelected())
				writeProp("HGrid", "false");
			

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

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		picontroller = new PiController();
		
		hgrid.setSelected(false);
		readProp();
		
	}
}
