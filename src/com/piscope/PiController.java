package com.piscope;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

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
	
	
	@FXML
	void initialize() {
		
		ObservableList<String> items =FXCollections.observableArrayList (
			    In1, In2, In3, In4, In5, In6, In7, In8, In9, In10);
			instructionList.setItems(items);
	}
	
	
	
}
