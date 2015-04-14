package com.piscope;



import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class PiPreferenceController implements Initializable {

	Properties prop = new Properties();
	InputStream input = null;
	OutputStream output = null;
	PiController picontroller;
	private Scene scene;
	
	
	//Checkboxes
	@FXML private CheckBox HGrid=new CheckBox();
	@FXML private CheckBox VGrid=new CheckBox();
	@FXML private CheckBox HZero=new CheckBox();
	@FXML private CheckBox VZero=new CheckBox();
	
	
	
	@FXML
	Rectangle rect;
	
	@FXML
	private TextField textF;
	
	PiMain main= new PiMain();

	
	
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
			HGrid.setSelected(Boolean.valueOf(prop.getProperty("HGrid")));
			VGrid.setSelected(Boolean.valueOf(prop.getProperty("VGrid")));
			HZero.setSelected(Boolean.valueOf(prop.getProperty("HZero")));
			VZero.setSelected(Boolean.valueOf(prop.getProperty("VZero")));				
		} 
		catch (IOException e) {e.printStackTrace();} 
		
		finally {
			if (input != null) 
				try {
					input.close();
				} 
				catch (IOException e) {e.printStackTrace();}		

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
		scene= new Scene(root);		
		dialogStage.setScene(scene);		
		dialogStage.show();	
	}
	
	public void savePref(){
		try {
			output = new FileOutputStream("config.properties");			
			
			
				prop.setProperty("HGrid", String.valueOf(HGrid.isSelected()));
				prop.setProperty("VGrid", String.valueOf(VGrid.isSelected()));
				prop.setProperty("HZero", String.valueOf(HZero.isSelected()));
				prop.setProperty("VZero", String.valueOf(VZero.isSelected()));				
			

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
		
		
	}

	@FXML
	public void close() {
		// save properties to project root folder
		savePref();
		//System.out.println(s);
		prefButton.getScene().getWindow().hide();

	}
	
	

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		//picontroller = new PiController();
		//textF=new TextField();
		//hgrid.setSelected(false);
		//textF.setText("Hello");
		
		textF.textProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(final ObservableValue<? extends String> observable, final String oldValue, final String newValue) {
		        // this will run whenever text is changed		    	
		    	if(textF.lengthProperty().get()==3 || textF.lengthProperty().get()==6)
		    		rect.setFill(Color.valueOf("#"+textF.getText()));
		    }
		});
		
		readProp();
		
	}
}
