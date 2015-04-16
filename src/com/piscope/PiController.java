//Package Declaration
package com.piscope;

//Dependencies Declaration
//--------------------------------------------------------------------------------------------------
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Slider;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
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

	PiMain main;
	// LineChart Reference
	@FXML BorderPane piView;

	@FXML
	private LineChart<Number, Number> PiChart;

	// XYChart Series declaration
	private XYChart.Series<Number, Number> PiSeries;

	@FXML
	Slider slider;

	@FXML
	private Label bufferLabel;

	// Pane centerChart;
	@FXML
	AnchorPane centerChart;

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
	double squaresf = 10;

	// Triangle Wave Variables
	double TriangleArr[] = { -1, 1 };
	int TriangleCount = 1;
	double trianglesf = 10;

	// Sawtooth Wave Variables
	double SawtoothArr[] = { -1, 1 };
	int sawtoothCount = 1;
	double sawtoothsf = 10;

	// Noise Wave
	double noisesf = 10;

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
	Label piStatus;

	// Image Variable
	String FileName;

	// Waveform Variables:
	String waveType = "sine";
	int initWave = 1;
	@FXML
	private Label waveLabel;

	// PiScope Default Variables
	String PiVersion = PiMain.PiVersion;

	// F-F Variables
	double fval = 0.0;

	// Reader Line Variables
	private static double vol[] = new double[1000];

	private static double time[] = new double[1000];
	private static int i = 1;
	static int v = 0;
	static int t = 0;
	static int sampleSize = 0;
	static boolean customCall = false;
	static int customi = 0;

	Properties prop = new Properties();
	InputStream input = null;

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

						if (customCall == true) {
							toggleAdd();
							autoZoom();
							piStatus("Waveform stopped");
							customCall = false;
						}

					}

				}));

		// Set Cycle count to be Indefinite
		addDataTimeline.setCycleCount(Animation.INDEFINITE);

		// Used to display the values pointed by the mouse
		PiChart.setOnMouseMoved(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				// System.out.println(yAxis.getValueForDisplay(mouseEvent.getY()));
				ya = yAxis.getValueForDisplay(mouseEvent.getY());
				xa = xAxis.getValueForDisplay(mouseEvent.getX());
				y_axis.setText(String.format("%.02f V", ya));
				x_axis.setText(String.format("%.02f s", xa));
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
		vol[0] = 9999;// Check if file is imported for "custom" waveType
		main = new PiMain();
		checkProperties();

	}
	
	public void checkProperties(){
		FileReader r = null;
		
			try {
				r = new FileReader("config.properties");
			} catch (FileNotFoundException e) {
				PiPreferenceController prefConroller=new PiPreferenceController();
				prefConroller.reset();				
			}
			finally{
				try {
					r = new FileReader("config.properties");
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					r.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}		
	}

	@FXML
	public void slider() {
		clearWave = slider.getValue();
		String value = String.format("%.02f", slider.getValue());
		piStatus("Clear Wave buffer at " + value + " s intervals");
		bufferLabel.setText(value + " s");
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
							WriteValue = squareWave * squaresf));
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
							.currentTimeMillis() - startTime) * 2, WriteValue
							* trianglesf));
			break;

			// Sawtooth Wave
		case "sawtooth":
			WriteValue = SawtoothArr[sawtoothCount++];
			if (sawtoothCount > 1) {
				sawtoothCount = 0;
				WriteTimeValue = (System.currentTimeMillis() - startTime) * 2;
			}

			PiSeries.getData().add(
					new XYChart.Data<Number, Number>(WriteTimeValue, WriteValue
							* sawtoothsf));
			break;

			// Noise Wave
		case "noise":
			PiSeries.getData().add(
					new XYChart.Data<Number, Number>((WriteTimeValue = (System
							.currentTimeMillis()) - startTime) * 2,
							WriteValue = Math.random() * noisesf));
			break;

		case "custom":

			for (int i = 0; i < sampleSize; i++) {
				/*
				 * if(customi>=sampleSize){ customi=0; toggleAdd(); }
				 */
				PiSeries.getData().add(
						new XYChart.Data<Number, Number>(
								WriteTimeValue = time[i], WriteValue = vol[i]));
			}

			customCall = true;

			break;

		}

		if (xAxis.getUpperBound() > startWave + clearWave) {
			startWave = xAxis.getUpperBound();
			PiSeries.getData().clear();
		}

		if (WriteEnabled == true && StartWrite == true) {
			// System.out.println(WriteTimeValue + ":" + WriteValue);
			String content = WriteTimeValue + ":" + WriteValue + "\n";
			try {
				WriteFile(content, false);
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
			// To do custom
			if (waveType == "custom") {
				clearChart();
				if (vol[0] == 9999)
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
			// calcFreq();
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
	void aboutDialog() throws InterruptedException, IOException {
		piView.setDisable(true);
		PiAboutController aboutController = new PiAboutController();
		aboutController.dialogBuild();
		piView.setDisable(false);
	}

	// This method is used to build Preferences Dialog
	@FXML
	void preferenceDialog() throws IOException {		
		piView.setDisable(true);		
		PiPreferenceController preferenceController = new PiPreferenceController();
		preferenceController.dialogBuild();
		piView.setDisable(false);
		readProp();
	}

	public void readProp() {
		File preferenceCss = new File("preferences.css");		
		try {
			input = new FileInputStream("config.properties");
			// load a properties file
			prop.load(input);

			// String path =
			// PiController.class.getProtectionDomain().getCodeSource().getLocation().getPath();

			FileWriter fileWritter = new FileWriter(preferenceCss, false);
			BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
			String content = "";

			if (!Boolean.valueOf(prop.getProperty("HGrid"))) {
				content = ".chart-horizontal-grid-lines { -fx-stroke:transparent}\n";
			} else if (Boolean.valueOf(prop.getProperty("HGrid"))) {
				content = ".chart-horizontal-grid-lines {-fx-stroke: #"
						+ prop.getProperty("CHGrid") + "; -fx-opacity:"+ prop.getProperty("OHGrid")+"; -fx-stroke-width:"+prop.getProperty("WHGrid")+"}\n";
			}
			if (!Boolean.valueOf(prop.getProperty("VGrid"))) {
				content += ".chart-vertical-grid-lines { -fx-stroke:transparent}\n";
			} else if (Boolean.valueOf(prop.getProperty("VGrid"))) {
				content += ".chart-vertical-grid-lines {-fx-stroke: #"
						+ prop.getProperty("CVGrid") + "; -fx-opacity:"+ prop.getProperty("OVGrid")+"; -fx-stroke-width:"+prop.getProperty("WVGrid")+"}\n";
			}
			if (!Boolean.valueOf(prop.getProperty("HZero"))) {
				content += ".chart-horizontal-zero-line { -fx-stroke:transparent}\n";
			} else if (Boolean.valueOf(prop.getProperty("VGrid"))) {
				content += ".chart-horizontal-zero-line {-fx-stroke: #"
						+ prop.getProperty("CHZero") + "; -fx-opacity:"+ prop.getProperty("OHZero")+"; -fx-stroke-width:"+prop.getProperty("WHZero")+"}\n";
			}
			if (!Boolean.valueOf(prop.getProperty("VZero"))) {
				content += ".chart-vertical-zero-line { -fx-stroke:transparent}\n";
			} else if (Boolean.valueOf(prop.getProperty("VGrid"))) {
				content += ".chart-vertical-zero-line {-fx-stroke: #"
						+ prop.getProperty("CVZero") + "; -fx-opacity:"+ prop.getProperty("OVZero")+"; -fx-stroke-width:"+prop.getProperty("WVZero")+"}\n";
			}
			content += ".chart-plot-background {-fx-background-color: #"
					+ prop.getProperty("CPlotB") + ";}\n";
			
			content += ".default-color0.chart-series-line {-fx-stroke: #"
					+ prop.getProperty("CLine")
					+ ";-fx-stroke-width:"+prop.getProperty("WLine")+"; -fx-opacity:"+ prop.getProperty("OLine")+";}\n";
			
			if (Boolean.valueOf(prop.getProperty("DropS")))
				content+=".default-color0.chart-series-line {-fx-effect: dropshadow(gaussian, #"+prop.getProperty("CDropS")+","+ prop.getProperty("WDropS")+","+prop.getProperty("ODropS")+", 0,2 ); }";
			else
				content+=".default-color0.chart-series-line {-fx-effect: null; }";
			
			PiMain.PiLineDefColour = Paint.valueOf("#"+prop.getProperty("CLineS"));			
			bufferWritter.write(content);
			bufferWritter.close();
			PiChart.getScene().getStylesheets().clear();			
			PiChart.getScene()
			.getStylesheets()
			.add(this.getClass().getResource("application.css")
					.toExternalForm());			
			PiChart.getScene()
			.getStylesheets()
			.add("file:///"
					+ preferenceCss.getAbsolutePath()
					.replace("\\", "/"));

		} catch (IOException e) {
			e.printStackTrace();
		} finally {

			if (input != null) {
				try {
					input.close();
					// preferenceCss.delete();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}

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

	public void customCall() {
		clearChart();
		sampleSize = vol.length;
		waveType = "custom";
		toggleAdd();
	}

	// This method calculate the frequency of the waveform
	public void calcFreq() {
		double vol[] = new double[PiSeries.getData().size()];
		double time[] = new double[PiSeries.getData().size()];

		for (int i = 0; i < PiSeries.getData().size(); i++) {
			vol[i] = (double) PiSeries.getData().get(i).getYValue();
			time[i] = (double) PiSeries.getData().get(i).getXValue();
		}

		double max = 0;
		double timeDif[] = new double[2];

		int c = 0;
		for (int i = 0; i < vol.length; i++) {
			if (max < vol[i]) {
				max = vol[i];
			}
		}
		for (int i = 0; i < vol.length; i++) {
			if (vol[i] == max)
				timeDif[c++] = time[i];
			if (c > 1)
				break;

		}
		/*
		 * timeDif[c++]=time[i]; if(c>1) break;
		 */
		System.out.println(max);
		System.out.println(time[1]);
		System.out.println(time[0]);
		System.out.println("Hello");

	}

	// This method is used to clear the chart
	@FXML
	public void clearChart() {
		PiSeries.getData().clear();
		xAxis.setLowerBound(0);
		startTime = System.currentTimeMillis();
		autoZoom();
		piStatus("Chart Cleared");
		if (waveType == "sawtooth") {
			sleep(250);
			autoZoom();
			sleep(250);
			PiSeries.getData().clear();
		}
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
			waveType = "custom";
			break;

		default:
			waveType = "sine";
			break;
		}
		waveLabel.setText(waveType);
		piStatus(waveType + " wave selected");
		if (waveType == "custom")
			if (vol[0] == 9999)
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
	public String size() {
		String MaxSize;
		MaxSize = "Total no of Samples analyzed: " + PiSeries.getData().size();
		piStatus(MaxSize);
		return MaxSize;
	}

	// This method is used to calculate the Maximum value in a sample
	@FXML
	public String MaxVal() {
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
		fval = MaxValy;
		MaxVal = String.format("Peak Value at: X: %.02f ms Y: %.02f V ",
				MaxValx, MaxValy);
		piStatus(MaxVal);
		return MaxVal;
	}

	@FXML
	// This method is used to calculate RMS Value
	public String RMSVal() {
		double RMSVali = 0.0;
		String RMSVal;
		MaxVal();
		RMSVali = fval / Math.sqrt(2);
		RMSVal = String.format("RMS Value: %.02f V", RMSVali);
		piStatus(RMSVal);
		return RMSVal;
	}

	// This method is used to calculate the Minimum value in a sample
	@FXML
	public String MinVal() {
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
		return MinVal;
	}

	// This method is used to calculate the Average value in a sample
	@FXML
	public String AvgVal() {
		String AvgVal;
		double avg = 0.0;
		int i = 0;
		for (i = 0; i < PiSeries.getData().size(); i++)
			avg += (double) PiSeries.getData().get(i).getYValue();
		avg = avg / (i + 1);
		AvgVal = String.format("Average Value: %.02f V ", avg);
		piStatus(AvgVal);
		return AvgVal;
	}

	// FFT
	@FXML
	public void fft() {
		if (waveType == "custom" && vol[0] == 9999)
			fileImport();
		double real[] = new double[PiSeries.getData().size()];
		double img[] = new double[PiSeries.getData().size()];
		double temp[] = new double[PiSeries.getData().size()];
		for (i = 0; i < PiSeries.getData().size(); i++) {
			real[i] = (double) PiSeries.getData().get(i).getYValue();
			img[i] = (double) PiSeries.getData().get(i).getXValue();
			// System.out.println(real[i]);
		}

		PiComp.transform(real, temp);
		// PiComp.transform(img, temp);
		// for(int i=0;i<PiSeries.getData().size();i++)
		// System.out.println(real[i]);
		vol = new double[real.length];
		time = new double[img.length];
		vol = real;
		time[0] = 0;
		for (int i = 1; i < img.length; i++)
			time[i] = 1 / img[i];
		customCall();
		System.out.println("Hello");
		piStatus("Calculating FFT . . .");
	}

	// This method is used to calculate PSD of the waveform
	@FXML
	public void psd() {
		PiComp.psd(vol, time);
		customCall();
	}

	// This method is used to import a file
	@FXML
	public void fileImport() {
		vol = new double[10000];
		time = new double[10000];
		JFileChooser chooser = new JFileChooser();
		int choice = chooser.showOpenDialog(chooser);
		if (choice != JFileChooser.APPROVE_OPTION)
			return;
		File chosenFile = chooser.getSelectedFile();
		for (int i = 0; i < vol.length; i++) {
			vol[i] = 9999;
			time[i] = 9999;
		}

		try (BufferedReader br = new BufferedReader(new FileReader(chosenFile))) {

			String sCurrentLine;
			int i = 1;
			while ((sCurrentLine = br.readLine()) != null) {
				// System.out.println(sCurrentLine);

				if (i++ % 2 == 0)
					time[t++] = Double.parseDouble(sCurrentLine);
				else
					vol[v++] = Double.parseDouble(sCurrentLine);

			}
			i = v = t = sampleSize = 0;

			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (int i = 0; vol[i] != 9999; i++)
			sampleSize++;
		System.out.println(sampleSize);

		// System.out.println(chosenFile);
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
	public String WriteFile(String content, boolean fileOption)
			throws IOException {
		String timeStamp = new SimpleDateFormat("HH_ddMMyyyy").format(Calendar
				.getInstance().getTime());
		FileName = "File-" + timeStamp + ".txt";
		File file = new File(FileName);
		String introText = null;
		boolean newFile = fileOption;

		// if file doesn't exists, then create it
		if (!file.exists() || newFile == true) {
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

		FileWriter fileWritter = new FileWriter(file.getName(), !newFile);
		BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
		if (newFile == true)
			bufferWritter.write(introText + content);
		else
			bufferWritter.write(content);
		bufferWritter.close();
		return FileName;
	}

	// THis method is uesd for wait/sleep
	void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// This method is used to Exit the application
	@FXML
	public void SystemExit() {
		System.exit(0);
	}

	// This method computes Total values
	@FXML
	public void totalComputation() throws IOException {
		String content =MaxVal() + "\n";
		content += MinVal() + "\n";
		content += RMSVal() + "\n";
		content += AvgVal() + "\n";
		content += size() + "\n";
		content = WriteFile(content, true);
		
		piStatus("Computing . . File saved as " + content);
	}

}
