package com.piscope;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import org.gillius.jfxutils.JFXUtil;
import org.gillius.jfxutils.chart.ChartPanManager;
import org.gillius.jfxutils.chart.JFXChartUtil;
import org.gillius.jfxutils.chart.StableTicksAxis;



public class PiController {
	
	//LineChart Reference
	@FXML private LineChart<Number, Number> PiChart;
	
	//Axis Reference
	@FXML private StableTicksAxis xAxis;
	@FXML private StableTicksAxis yAxis;
	
	//Bottom label references
	@FXML private Label xyValues;
	@FXML private Label measurement;
	@FXML private Label instructions;
	
	//Bottom List reference
	@FXML private ListView<String> instructionList;
	
	//Instruction Strings
	String In1="* Use Start/Stop button to Start/Stop Waveforms";
	String In2="* Use auto range button to set Auto";
	String In3="* Scroll Mouse to Zoom In/Out";
	String In4="* Select the part of waveform using Primary Mouse key";
	String In5="* Use seconday Mouse key to shift the waveform";
	String In6="* Double click to set statting position for a line";
	String In7="* Double click again to end the line";
	String In8="* Hover on the line and drag it using left click";
	String In9="* Note the the colour of the line changes to red when the line is selected";
	String In10="* Delete the line by hovering on it and clicking Secondary mouse key";	

	//Dialog Variable in seconds
	double dialogTimeout=10;
	
	//Initialization function
	@FXML
	void initialize() {
		
		ObservableList<String> items =FXCollections.observableArrayList (
			    In1, In2, In3, In4, In5, In6, In7, In8, In9, In10);
			instructionList.setItems(items);
			xAxis.setLabel("Time (ms)");
			yAxis.setLabel("Voltage (V)");	
	}
	
	//Save Rendering
	@FXML
	public void saveAsPng() {
		String timeStamp = new SimpleDateFormat("HHmmss_ddMMyyyy").format(Calendar.getInstance().getTime());		
		PiChart.setAnimated(false);				
		System.out.println("Saving . . .");
	    WritableImage image = PiChart.snapshot(new SnapshotParameters(), null);	    
	    File file = new File("chart"+timeStamp+".png");
	    try {
	    	ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
	    } catch (IOException e) {
	    	 Logger.getLogger(PiController.class.getName()).log(Level.SEVERE, null, e);
	    	 System.out.println("Error");
	    }
	}
	
	
	//This snippet is used to build Dialog
	@FXML
	void dialogBuild() throws InterruptedException
	{		
		 final Stage dialog = new Stage();
         dialog.initModality(Modality.APPLICATION_MODAL);         
         VBox dialogVbox = new VBox(20);
         dialogVbox.getChildren().add(new Text("\t\t\t\tAbout PiScope v1.0"));
         dialogVbox.getChildren().add(new Text("\tTeam:\n\t Prathyush\n\t Shshikiran\n\t Vinay\n\t Amaraprabhu"));
         dialogVbox.getChildren().add(new Text("\tProject Guide    :\t Prof MG Srinivas\n\tTechnical Support:\t Chandra Prasad Sir"));         
         Scene dialogScene = new Scene(dialogVbox, 400, 300);
         dialog.getIcons().add( new Image( PiMain.class.getResourceAsStream( "icon.png" )));          
         dialog.setScene(dialogScene);
         dialog.show();         
         PauseTransition pause = new PauseTransition(Duration.seconds(dialogTimeout));
         pause.setOnFinished(e -> dialog.hide());
         pause.play();         
		
	}
	
	
}
