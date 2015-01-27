package com.piscope;

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
}
