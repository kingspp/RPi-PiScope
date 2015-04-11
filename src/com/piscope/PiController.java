//Package Declaration
package com.piscope;

//Dependencies Declaration
//--------------------------------------------------------------------------------------------------
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
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
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuBar;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;

import org.gillius.jfxutils.chart.ChartPanManager;
import org.gillius.jfxutils.chart.JFXChartUtil;
import org.gillius.jfxutils.chart.StableTicksAxis;

//----------------------------------------------------------------------------------------------------

public class PiController {

	// Variable Declarations
	// ---------------------------------------------------------------------------------------------------
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

	// Timeline Variable
	private Timeline addDataTimeline;

	// Dialog Variable
	double dialogTimeout = 10;
	int dialogHeight = 400;
	int dialogWidth = 300;

	// Timer Variable
	private long startTime;

	// Sinewave Varialbe
	double sineWave = 0;
	double clearWave = 50000;
	double startWave = 0.0;
	double sineWavesf = 0.1;
	double sinesf = 10;

	// Square Wave Variables
	double squareWave = 0.0;
	double squareTimeWidth = 0.0;
	double squareDefault = 10;
	double squaresf=10;

	// Triangle Wave Variables
	double TriangleArr[] = { -1, 1 };
	int TriangleCount = 1;
	double TriangleTable[] = { 4, 8, 12, 16, 20, 24, 28, 32, 36, 40, 44, 48,
			52, 56, 60, 64, 60, 56, 52, 48, 44, 40, 36, 32, 28, 24, 20, 16, 12,
			8, 4, 0 };
	double trianglesf=10;

	// Sawtooth Wave Variables
	double SawtoothArr[] = { -1, 1 };
	int sawtoothCount = 1;
	double sawtoothsf=10;

	//Noise Wave
	double noisesf=10;

	// File Handling Variables
	boolean WriteEnabled = false;
	boolean StartWrite = false;
	double WriteTimeValue;
	double WriteValue;

	// Frame Variables
	double KeyFrameTime = 150;

	// Test Variables
	int test = 0;
	double sine[];

	// Number axis declaration
	Number xa, ya;

	@FXML
	private Label x_axis;
	@FXML
	private Label y_axis;

	// MenuBar
	@FXML
	private MenuBar menuBar;

	// Status
	@FXML
	private Label piStatus;

	// Image Variable
	String FileName;

	// Waveform Variables:
	String waveType = "sine";
	int initWave = 1;
	@FXML
	private Label waveLabel;

	// PiScope Default Variables
	String PiVersion = "v1.1";

	//F-F Variables
	double fval=0.0;

	//Reader Line Variables
	private static double vol[] = new double [1000];

	private static double time[] = new double [1000];
	private static int i=1;
	static int v=0;
	static int t=0;
	static int sampleSize=0;
	static boolean customCall=false;
	static int customi=0;

	// Instruction Strings
	String In1 = "* Use Start/Stop button to Start/Stop Waveforms";
	String In2 = "* Use auto range button to set Auto";
	String In3 = "* Clear Chart Button is used to clear the existing Waveform on Chart";
	String In4 = "* Use Waveform button to change the type of Wave to be displayed ";
	String In5 = "* Save Image Button saves the currnet Image as a PNG File";
	String In6 = "* Save Value Button saves the X-Y Data in a file for further Analysis";
	String In7 = "* Scroll Mouse to Zoom In/Out";
	String In8 = "* Select the part of waveform using Primary Mouse key";
	String In9 = "* Use seconday Mouse key to shift the waveform";
	String In10 = "* Double click to set statting position for a line";
	String In11 = "* Double click again to end the line";
	String In12 = "* Hover on the line and drag it using left click";
	String In13 = "* Note the the colour of the line changes to red when the line is selected";
	String In14 = "* Delete the line by hovering on it and clicking Secondary mouse key";
	String In15 = "* Status in toolbar indicates the current process";

	// --------------------------------------------------------------------------------------------------

	// Initialization function
	@FXML
	void initialize() {

		ObservableList<String> items = FXCollections.observableArrayList(In1,
				In2, In3, In4, In5, In6, In7, In8, In9, In10, In11, In12, In13,
				In14, In15);
		instructionList.setItems(items);

		// Set Chart Properties
		xAxis.setForceZeroInRange(false);

		// Get Current Time
		startTime = System.currentTimeMillis();

		// Set Labels
		xAxis.setLabel("Time (ms)");

		yAxis.setLabel("Voltage (V)");

		// Add series
		PiSeries = new XYChart.Series<Number, Number>();
		PiSeries.setName("Data");
		PiChart.getData().add(PiSeries);

		// Add a Timeline to the Chart
		addDataTimeline = new Timeline(new KeyFrame(
				Duration.millis(KeyFrameTime), new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent actionEvent) {
						addSample();
						
						if(customCall==true){							
							toggleAdd();
							autoZoom();
							piStatus("Waveform stopped");
							customCall=false;}
							
						}	
										
					
				}));

		// Set Cycle count to be Indefinite
		addDataTimeline.setCycleCount(Animation.INDEFINITE);

		// Used to display the values pointed by the mouse
		PiChart.setOnMouseMoved(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				// System.out.println(yAxis.getValueForDisplay(mouseEvent.getY()));
				xa = yAxis.getValueForDisplay(mouseEvent.getY());
				ya = xAxis.getValueForDisplay(mouseEvent.getX());
				x_axis.setText(String.format("%.02f V", xa));
				y_axis.setText(String.format("%.02f ms", ya));
			}
		});

		// This function is used for Panning
		ChartPanManager panner = new ChartPanManager(PiChart);

		panner.setMouseFilter(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				if (mouseEvent.getButton() == MouseButton.SECONDARY
						|| (mouseEvent.getButton() == MouseButton.PRIMARY && mouseEvent
						.isShortcutDown())) {
					// let it through
				} else {
					mouseEvent.consume();

				}
			}
		});
		panner.start();

		// This method is used for Zooming
		JFXChartUtil.setupZooming(PiChart, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				if (mouseEvent.getButton() != MouseButton.PRIMARY
						|| mouseEvent.isShortcutDown())
					mouseEvent.consume();
			}
		});
		vol[0]=9999;//Check if file is imported for "custom" waveType

	}

	// This function generates the series
	@FXML
	void addSample() {

		switch (waveType) {
		// Sine Wave
		case "sine":
			// Generate a sample Sine Wave
			sineWave += sineWavesf;
			PiSeries.getData().add(
					new XYChart.Data<Number, Number>((WriteTimeValue = (System
							.currentTimeMillis()) - startTime),
							WriteValue = Math.sin(sineWave) * sinesf));
			break;

			// Square Wave
		case "square":
			if ((squareTimeWidth++) == 5) {
				if (squareWave == -1) {
					squareWave = 1;
					WriteTimeValue = squareDefault;
				}

				else {
					squareWave = -1;
					WriteTimeValue = squareDefault;
				}
				squareTimeWidth = 0;
			} else
				WriteTimeValue = squareDefault = (System.currentTimeMillis() - startTime) * 2;

			PiSeries.getData().add(
					new XYChart.Data<Number, Number>(WriteTimeValue,
							WriteValue = squareWave*squaresf));
			break;

			/*
			 * //Triangular Wave case "triangle": PiSeries.getData().add( new
			 * XYChart.Data<Number, Number>((WriteTimeValue = (System
			 * .currentTimeMillis()) - startTime)*2, WriteValue =
			 * TriangleTable[TriangleCount++])); if(TriangleCount>31)
			 * TriangleCount=0; break;
			 */
		case "triangle":
			WriteValue = TriangleArr[TriangleCount++];
			if (TriangleCount > 1)
				TriangleCount = 0;
			PiSeries.getData().add(
					new XYChart.Data<Number, Number>(WriteTimeValue = (System
							.currentTimeMillis() - startTime) * 2, WriteValue*trianglesf));
			break;

			// Sawtooth Wave
		case "sawtooth":
			WriteValue = SawtoothArr[sawtoothCount++];
			if (sawtoothCount > 1) {
				sawtoothCount = 0;
				WriteTimeValue = (System.currentTimeMillis() - startTime) * 2;
			}

			PiSeries.getData()
			.add(new XYChart.Data<Number, Number>(WriteTimeValue,
					WriteValue*sawtoothsf));
			break;

			// Noise Wave
		case "noise":
			PiSeries.getData().add(
					new XYChart.Data<Number, Number>((WriteTimeValue = (System
							.currentTimeMillis()) - startTime) * 2,
							WriteValue = Math.random()*noisesf));
			break;

		case "custom":

			for(int i=0;i<sampleSize;i++){
			/*
			if(customi>=sampleSize){
				customi=0;
				toggleAdd();
			}
			*/
				PiSeries.getData().add(
						new XYChart.Data<Number, Number>(WriteTimeValue = time[i],WriteValue = vol[i]));}
			
			customCall=true;
			
			break;

		}

		// To do : Get rid of manual setting
		if (xAxis.getUpperBound() > startWave + clearWave) {
			startWave = xAxis.getUpperBound();
			PiSeries.getData().clear();
		}

		if (WriteEnabled == true && StartWrite == true) {
			// System.out.println(WriteTimeValue + ":" + WriteValue);
			String content = WriteTimeValue + ":" + WriteValue + "\n";
			try {
				WriteFile(content);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	// Add series to the Chart
	@FXML
	void toggleAdd() {
		switch (addDataTimeline.getStatus()) {

		// Start Case
		case PAUSED:
		case STOPPED:
			addDataTimeline.play();
			PiChart.getXAxis().setAutoRanging(true);
			PiChart.getYAxis().setAutoRanging(true);
			// Animation looks horrible if we're updating a lot
			PiChart.setAnimated(false);
			PiChart.getXAxis().setAnimated(false);
			PiChart.getYAxis().setAnimated(false);
			StartWrite = true;
			piStatus("Waveform Started");
			//To do custom
			if(waveType=="custom"){
				clearChart();
				if(vol[0]==9999)				
					fileImport();
			}
			break;

			// Stop Case
		case RUNNING:
			addDataTimeline.stop();
			// Return the animation since we're not updating a lot
			PiChart.setAnimated(true);
			PiChart.getXAxis().setAnimated(true);
			PiChart.getYAxis().setAnimated(true);
			// panner.start();
			WriteEnabled = false;
			StartWrite = false;
			piStatus("Waveform Stopped");
			break;

		default:
			throw new AssertionError("Unknown status");
		}
	}

	// This Function is used for AutoZoom
	@FXML
	void autoZoom() {
		PiChart.getXAxis().setAutoRanging(true);
		PiChart.getYAxis().setAutoRanging(true);
		// There seems to be some bug, even with the default NumberAxis, that
		// simply setting the
		// auto ranging does not recompute the ranges. So we clear all chart
		// data then re-add it.
		// Hopefully I find a more proper way for this, unless it's really bug,
		// in which case I hope
		// it gets fixed.
		ObservableList<XYChart.Series<Number, Number>> data = PiChart.getData();
		PiChart.setData(FXCollections
				.<XYChart.Series<Number, Number>> emptyObservableList());
		PiChart.setData(data);
		piStatus("Auto Zoom Selected");
	}

	// Save Rendering
	@FXML
	public void saveAsPng() {
		String timeStamp = new SimpleDateFormat("HHmmss_ddMMyyyy")
		.format(Calendar.getInstance().getTime());
		PiChart.setAnimated(false);
		WritableImage image = PiChart.snapshot(new SnapshotParameters(), null);
		FileName = "Chart-" + timeStamp + ".png";
		File file = new File(FileName);
		try {
			ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
			piStatus(FileName + " saved");
		} catch (IOException e) {
			Logger.getLogger(PiController.class.getName()).log(Level.SEVERE,
					null, e);
			piStatus("Error saving " + FileName);
		}

	}

	// This method defines Write Value
	@FXML
	public void saveValue() {
		WriteEnabled = true;
		piStatus("Saving data to a file . . .");

	}

	// This snippet is used to build Dialog
	@FXML
	void dialogBuild() throws InterruptedException {
		final Stage dialog = new Stage();
		dialog.initModality(Modality.APPLICATION_MODAL);
		VBox dialogVbox = new VBox(20);
		dialogVbox.getChildren().add(
				new Text("\t\t\t\tAbout PiScope " + PiVersion));
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
		// dialog.initStyle(StageStyle.UNDECORATED);
		dialog.setScene(dialogScene);
		dialog.show();

		// Pause function acts as a Timeout Function
		PauseTransition pause = new PauseTransition(
				Duration.seconds(dialogTimeout));
		pause.setOnFinished(e -> dialog.hide());
		pause.play();
	}

	// This method gets the value of X Axis
	double getxAxis(double axis) {
		axis = (double) xa;
		return axis;
	}

	// This method gets the value of Y Axis
	double getyAxis(double axis) {
		axis = (double) ya;
		return axis;
	}

	// This method updates the Measurement String
	void update(String str) {
		xyValues.setText(str);
	}

	// This method is used to clear the chart
	@FXML
	public void clearChart() {
		PiSeries.getData().clear();
		// xAxis.setLowerBound(0);
		// startTime=System.currentTimeMillis();
		autoZoom();
		piStatus("Chart Cleared");
	}

	// This method is used to change waveform type
	@FXML
	public void waveType() {

		switch (initWave++) {
		case 0:
			waveType = "sine";
			break;

		case 1:
			waveType = "square";
			break;

		case 2:
			waveType = "triangle";
			break;

		case 3:
			waveType = "sawtooth";
			break;

		case 4:
			waveType = "noise";
			break;

		case 5: 
			waveType="custom";
			break;

		default:
			waveType = "sine";
			break;
		}
		waveLabel.setText(waveType);
		piStatus(waveType + " wave selected");
		if(waveType=="custom")
			if(vol[0]==9999)			
				piStatus("Please select the data file");	

		if (initWave > 5)
			initWave = 0;
	}

	// This method is used to set Status Label
	public void piStatus(String status) {
		piStatus.setText(status);
	}

	// This method is used to Calculate the Maximum size of Samples
	@FXML
	public void size() {
		String MaxSize;
		MaxSize = "Total no of Samples analyzed: " + PiSeries.getData().size();
		piStatus(MaxSize);
	}

	// This method is used to calculate the Maximum value in a sample
	@FXML
	public void MaxVal() {
		String MaxVal;
		double temp = 0.0;
		double MaxValx = 0.0;
		double MaxValy = 0.0;
		for (int i = 0; i < PiSeries.getData().size(); i++) {
			temp = (double) PiSeries.getData().get(i).getYValue();
			if (temp > MaxValy) {
				MaxValy = temp;
				MaxValx = (double) PiSeries.getData().get(i).getXValue();
			}
		}
		fval=MaxValy;
		MaxVal = String.format("Peak Value at: X: %.02f ms Y: %.02f V ",
				MaxValx, MaxValy);
		piStatus(MaxVal);
	}

	@FXML
	//This method is used to calculate RMS Value
	public void RMSVal()
	{
		double RMSVali=0.0;
		String RMSVal;
		MaxVal();
		RMSVali=fval/Math.sqrt(2);
		RMSVal=String.format("RMS Value: %.02f V",RMSVali);
		piStatus(RMSVal);		
	}

	// This method is used to calculate the Minimum value in a sample
	@FXML
	public void MinVal() {
		String MinVal;
		double temp = 0.0;
		double MinValx = 0.0;
		double MinValy = 100000;
		for (int i = 0; i < PiSeries.getData().size(); i++) {
			temp = (double) PiSeries.getData().get(i).getYValue();
			if (temp < MinValy) {
				MinValy = temp;
				MinValx = (double) PiSeries.getData().get(i).getXValue();
			}
		}
		MinVal = String.format("Minimum Value at: X: %.02f ms Y: %.02f V ",
				MinValx, MinValy);
		piStatus(MinVal);
	}

	// This method is used to calculate the Average value in a sample
	@FXML
	public void AvgVal() {
		String AvgVal;
		double avg = 0.0;
		int i = 0;
		for (i = 0; i < PiSeries.getData().size(); i++)
			avg += (double) PiSeries.getData().get(i).getYValue();
		avg = avg / (i + 1);
		AvgVal = String.format("Average Value: %.02f V ", avg);
		piStatus(AvgVal);
	}


	//FFT
	@FXML
	public void fft()
	{
		if(vol[0]==9999)
			fileImport();
		double real[]=new double[PiSeries.getData().size()];
		double img[]=new double[PiSeries.getData().size()];
		double temp[]=new double[PiSeries.getData().size()];
		for (i = 0; i < PiSeries.getData().size(); i++){
			real[i]= (double) PiSeries.getData().get(i).getYValue();
			img[i]= (double) PiSeries.getData().get(i).getXValue();
			//System.out.println(real[i]);
		}
		
		PiComp.transform(real, temp);
		//for(int i=0;i<PiSeries.getData().size();i++)
			//System.out.println(real[i]);
		vol=new double[real.length];
		time=new double[img.length];
		vol=real;
		time=img;
		clearChart();
		sampleSize=vol.length;
		waveType="custom";		
		toggleAdd();
		System.out.println("Hello");
		piStatus("Calculating FFT . . .");
		
		
	}
	
	// This method is used to import a file
	@FXML
	public void fileImport() {
		vol=new double[10000];
		time=new double[10000];
		JFileChooser chooser = new JFileChooser();
		int choice = chooser.showOpenDialog(chooser);
		if (choice != JFileChooser.APPROVE_OPTION)
			return;
		File chosenFile = chooser.getSelectedFile();
		for(int i=0;i<vol.length;i++){
			vol[i]=9999;
			time[i]=9999;
		}

		try (BufferedReader br = new BufferedReader(new FileReader(chosenFile)))
		{

			String sCurrentLine;
			int i = 1;
			while ((sCurrentLine = br.readLine()) != null) {
				//System.out.println(sCurrentLine);

				if(i++%2==0)
					time[t++]=Double.parseDouble(sCurrentLine);
				else
					vol[v++]=Double.parseDouble(sCurrentLine);

			}
			i=v=t=sampleSize=0;
			
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		for(int i=0;vol[i]!=9999;i++)
			sampleSize++;
		System.out.println(sampleSize);
		
	
		//System.out.println(chosenFile);
	}

	// This method is used to export to a file
	@FXML
	public void fileExport() throws IOException {
		JFileChooser chooser = new JFileChooser();
		int choice = chooser.showSaveDialog(chooser);
		if (choice != JFileChooser.APPROVE_OPTION)
			return;
		// File filename = chooser.setName(name);
		// System.out.println(filename);
		// filename.createNewFile();
		// FileWriter fileWritter = new FileWriter(filename.getName(), true);
		FileWriter fileWritter = new FileWriter(chooser.getSelectedFile(), true);
		BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
		String date = new SimpleDateFormat("dd/MM/yyyy").format(Calendar
				.getInstance().getTime());
		String time = new SimpleDateFormat("HH:mm:ss").format(Calendar
				.getInstance().getTime());
		String introText = "///////////////////////////////////////////////////////////////////\n"
				+ "PiScope "
				+ PiVersion
				+ "\n"
				+ "Date: "
				+ date
				+ "\n"
				+ "Time: "
				+ time
				+ "\n"
				+ "//////////////////////////////////////////////////////////////////\n\n";
		bufferWritter.write(introText);
		String content;
		double xVal = 0.0;
		double yVal = 0.0;
		for (int i = 0; i < PiSeries.getData().size(); i++) {
			xVal = (double) PiSeries.getData().get(i).getXValue();
			yVal = (double) PiSeries.getData().get(i).getYValue();
			content = String.format("%f : %f\n", xVal, yVal);
			bufferWritter.write(content);
			System.out.println(content);
		}
		bufferWritter.close();

	}

	// This method is used to write to a file
	public void WriteFile(String content) throws IOException {
		String timeStamp = new SimpleDateFormat("HH_ddMMyyyy").format(Calendar
				.getInstance().getTime());
		FileName = "File-" + timeStamp + ".txt";
		File file = new File(FileName);
		String introText = null;
		boolean newFile = false;

		// if file doesn't exists, then create it
		if (!file.exists()) {
			file.createNewFile();
			String date = new SimpleDateFormat("dd/MM/yyyy").format(Calendar
					.getInstance().getTime());
			String time = new SimpleDateFormat("HH:mm:ss").format(Calendar
					.getInstance().getTime());
			introText = "///////////////////////////////////////////////////////////////////\n"
					+ "PiScope "
					+ PiVersion
					+ "\n"
					+ "Date: "
					+ date
					+ "\n"
					+ "Time: "
					+ time
					+ "\n"
					+ "//////////////////////////////////////////////////////////////////\n\n";
			newFile = true;
		}

		FileWriter fileWritter = new FileWriter(file.getName(), true);
		BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
		if (newFile == true)
			bufferWritter.write(introText + content);
		else
			bufferWritter.write(content);
		bufferWritter.close();
	}
	
	// This method is used to Exit the application
	@FXML
	public void SystemExit() {
		System.exit(0);
	}

}
