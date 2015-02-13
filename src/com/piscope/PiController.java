//Package Declaration
package com.piscope;

//Dependencies Declaration
//--------------------------------------------------------------------------------------------------
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import javax.imageio.ImageIO;

import org.gillius.jfxutils.JFXUtil;
import org.gillius.jfxutils.chart.ChartPanManager;
import org.gillius.jfxutils.chart.JFXChartUtil;
import org.gillius.jfxutils.chart.StableTicksAxis;

//----------------------------------------------------------------------------------------------------

public class PiController {

//Variable Declarations
//---------------------------------------------------------------------------------------------------
	// LineChart Reference
	@FXML
	private LineChart<Number, Number> PiChart;

	// XYChart Series declaration
	private XYChart.Series<Number, Number> PiSeries;

	// Axis Reference
	@FXML
	private StableTicksAxis xAxis;
	@FXML
	private StableTicksAxis yAxis;

	// Bottom label references
	@FXML
	private Label xyValues;
	@FXML
	private Label measurement;
	@FXML
	private Label instructions;

	// Bottom List reference
	@FXML
	private ListView<String> instructionList;
	
	private StackPane PiRoot;
	
	
	//Timeline Variable 
	private Timeline addDataTimeline;

	// Dialog Variable
	double dialogTimeout = 10;
	int dialogHeight=400;
	int dialogWidth=300;

	// Timer Variable
	private long startTime;	
	
	//Sinewave Varialbe
	double sineWave=0;
	double clearWave=50000;
	double startWave=0.0;
	double sineWavesf=0.1;
	
	//File Handling Variables
	boolean WriteEnabled=false;
	boolean StartWrite=false;
	double WriteTimeValue;
	double WriteValue;
	
	//Frame Variables
	double KeyFrameTime=150;
	
	//Test Variables
	int test=0;
	double sine[];
	
	
	//Number axis declaration
	Number xa,ya;
	
	// Instruction Strings
	String In1 = "* Use Start/Stop button to Start/Stop Waveforms";
	String In2 = "* Use auto range button to set Auto";
	String In3 = "* Scroll Mouse to Zoom In/Out";
	String In4 = "* Select the part of waveform using Primary Mouse key";
	String In5 = "* Use seconday Mouse key to shift the waveform";
	String In6 = "* Double click to set statting position for a line";
	String In7 = "* Double click again to end the line";
	String In8 = "* Hover on the line and drag it using left click";
	String In9 = "* Note the the colour of the line changes to red when the line is selected";
	String In10 = "* Delete the line by hovering on it and clicking Secondary mouse key";
	
//--------------------------------------------------------------------------------------------------
	
	// Initialization function
	@FXML
	void initialize() {

		ObservableList<String> items = FXCollections.observableArrayList(In1,
				In2, In3, In4, In5, In6, In7, In8, In9, In10);
		instructionList.setItems(items);

		// Set Chart Properties
		xAxis.setForceZeroInRange(false);
		
		//Get Current Time
		startTime = System.currentTimeMillis();

		// Set Labels
		xAxis.setLabel("Time (ms)");
		
		yAxis.setLabel("Voltage (V)");

		// Add series
		PiSeries = new XYChart.Series<Number, Number>();
		PiSeries.setName("Data");
		PiChart.getData().add(PiSeries);
		
		//Add a Timeline to the Chart
		addDataTimeline = new Timeline( new KeyFrame(
				Duration.millis(KeyFrameTime)
				,
				new EventHandler<ActionEvent>() {
					@Override
					public void handle( ActionEvent actionEvent ) {
						addSample();
					}
				}
		));
		
		//Set Cycle count to be Indefinite
		addDataTimeline.setCycleCount( Animation.INDEFINITE );
		
		//Used to display the values pointed by the mouse
		PiChart.setOnMouseMoved( new EventHandler<MouseEvent>() {
			@Override
			public void handle( MouseEvent mouseEvent ) {				
				//System.out.println(yAxis.getValueForDisplay(mouseEvent.getY()));
				xa=yAxis.getValueForDisplay(mouseEvent.getY());
				ya=xAxis.getValueForDisplay(mouseEvent.getX());
				measurement.setText(String.format("Measured Value: %.02f V , %.02f ms",xa,ya));				
			}
		} );
		
		//This function is used for Panning
		ChartPanManager panner = new ChartPanManager( PiChart );
		
		panner.setMouseFilter( new EventHandler<MouseEvent>() {
			@Override
			public void handle( MouseEvent mouseEvent ) {
				if ( mouseEvent.getButton() == MouseButton.SECONDARY ||
						 ( mouseEvent.getButton() == MouseButton.PRIMARY &&
						   mouseEvent.isShortcutDown() ) ) {
					//let it through
				} else {
					mouseEvent.consume();
					
				}
			}
		} );
		panner.start();
		
		//This method is used for Zooming 
		JFXChartUtil.setupZooming( PiChart, new EventHandler<MouseEvent>() {
			@Override
			public void handle( MouseEvent mouseEvent ) {
				if ( mouseEvent.getButton() != MouseButton.PRIMARY ||
				     mouseEvent.isShortcutDown() )
					mouseEvent.consume();
			}
		} );
	}
	
	// This is a Start Function (Used to set the Stage)
	public void start( Stage PiStage ) throws Exception {
		FXMLLoader PiLoader = new FXMLLoader( getClass().getResource( "PiView.fxml" ) );
		Region contentRootRegion = (Region) PiLoader.load();

		PiRoot = JFXUtil.createScalePane( contentRootRegion, 960, 540, false );
		Scene scene = new Scene( PiRoot, PiRoot.getPrefWidth(), PiRoot.getPrefHeight() );
		PiStage.setScene( scene );
		PiStage.setTitle( "Charting Example" );
		PiStage.show();
	}
	
	
	//This function generates the series
	@FXML
	void addSample() {	
		
		//Generate a sample Sine Wave
		sineWave+=sineWavesf;
		
	
		PiSeries.getData().add( new XYChart.Data<Number, Number>( (WriteTimeValue=(System.currentTimeMillis())- startTime),
				                                                       WriteValue=Math.sin(sineWave) ) );
		
		//To do : Get rid of manual setting
		if(xAxis.getUpperBound() > startWave+clearWave){
			startWave=xAxis.getUpperBound();			
			PiSeries.getData().clear();
		}
		
		if (WriteEnabled==true && StartWrite==true)
		{
			System.out.println(WriteTimeValue+":"+WriteValue);		
		}
		
	}
	
	
	
	
	
	//Add series to the Chart
	@FXML
	void toggleAdd() {
		switch ( addDataTimeline.getStatus() ) {
		
		//Start Case
		case PAUSED:
		case STOPPED:
			addDataTimeline.play();
			PiChart.getXAxis().setAutoRanging( true );
			PiChart.getYAxis().setAutoRanging( true );			
			//Animation looks horrible if we're updating a lot
			PiChart.setAnimated( false );
			PiChart.getXAxis().setAnimated( false );
			PiChart.getYAxis().setAnimated( false );
			StartWrite=true;			
			break;
		
		//Stop Case
		case RUNNING:
			addDataTimeline.stop();
			//Return the animation since we're not updating a lot
			PiChart.setAnimated( true );
			PiChart.getXAxis().setAnimated( true );
			PiChart.getYAxis().setAnimated( true );
			//panner.start();
			WriteEnabled=false;
			StartWrite=false;			
			break;
			
		default:
			throw new AssertionError( "Unknown status" );
		}
	}
	
	//This Function is used for AutoZoom
	@FXML
	void autoZoom() {
		PiChart.getXAxis().setAutoRanging( true );
		PiChart.getYAxis().setAutoRanging( true );
		//There seems to be some bug, even with the default NumberAxis, that simply setting the
		//auto ranging does not recompute the ranges. So we clear all chart data then re-add it.
		//Hopefully I find a more proper way for this, unless it's really bug, in which case I hope
		//it gets fixed.
		ObservableList<XYChart.Series<Number,Number>> data = PiChart.getData();
		PiChart.setData( FXCollections.<XYChart.Series<Number, Number>>emptyObservableList() );
		PiChart.setData( data );
	}
		
		

	// Save Rendering
	@FXML
	public void saveAsPng() {
		String timeStamp = new SimpleDateFormat("HHmmss_ddMMyyyy")
				.format(Calendar.getInstance().getTime());
		PiChart.setAnimated(false);
		System.out.println("Saving . . .");
		WritableImage image = PiChart.snapshot(new SnapshotParameters(), null);
		File file = new File("chart" + timeStamp + ".png");
		try {
			ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
		} catch (IOException e) {
			Logger.getLogger(PiController.class.getName()).log(Level.SEVERE,
					null, e);
			System.out.println("Error");
		}
	}
	
	//This method defines Write Value
	@FXML
	public void saveValue()
	{
		WriteEnabled=true;
		
	}

	// This snippet is used to build Dialog
	@FXML
	void dialogBuild() throws InterruptedException {
		final Stage dialog = new Stage();
		dialog.initModality(Modality.APPLICATION_MODAL);
		VBox dialogVbox = new VBox(20);
		dialogVbox.getChildren().add(new Text("\t\t\t\tAbout PiScope v1.5"));
		dialogVbox
				.getChildren()
				.add(new Text(
						"\tTeam:\n\t Prathyush\n\t Shshikiran\n\t Vinay\n\t Amaraprabhu"));
		dialogVbox
				.getChildren()
				.add(new Text(
						"\tProject Guide    :\t Prof MG Srinivas\n\tTechnical Support:\t Chandra Prasad Sir"));
		Scene dialogScene = new Scene(dialogVbox, dialogHeight, dialogWidth);
		dialog.getIcons().add(
				new Image(PiMain.class.getResourceAsStream("icon.png")));
		//dialog.initStyle(StageStyle.UNDECORATED);
		dialog.setScene(dialogScene);
		dialog.show();
		
		//Pause function acts as a Timeout Function
		PauseTransition pause = new PauseTransition(
				Duration.seconds(dialogTimeout));
		pause.setOnFinished(e -> dialog.hide());
		pause.play();
	}
	
	
	//This method gets the value of X Axis
	double getxAxis(double axis)
	{			
		axis=(double) xa;
		return axis;
	}
	
	//This method gets the value of Y Axis
	double getyAxis(double axis)
	{			
		axis=(double) ya;
		return axis;
	}
	
	//This method updates the Measurement String
	void update(String str)
	{
		xyValues.setText(str);
	}
	
	
	//This method is used to clear the chart
	@FXML
	public void clearChart() 
	{
		PiSeries.getData().clear();		
	}
	
	
	
	@FXML
	//This method is used to Exit the application
	public void SystemExit()
	{
		System.exit(0);
	}
	
	
}
