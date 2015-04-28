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
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class PiPreferenceController implements Initializable {

	Properties prop = new Properties();
	InputStream input = null;
	OutputStream output = null;
	PiController picontroller;
	private Scene scene;

	// Checkboxes
	@FXML
	private CheckBox HGrid = new CheckBox();
	@FXML
	private CheckBox VGrid = new CheckBox();
	@FXML
	private CheckBox HZero = new CheckBox();
	@FXML
	private CheckBox VZero = new CheckBox();
	@FXML
	private CheckBox PlotH = new CheckBox();
	@FXML
	private CheckBox DropS = new CheckBox();

	// Colour Picker Fields
	@FXML
	private ColorPicker CHGrid = new ColorPicker();
	@FXML
	private ColorPicker CVGrid = new ColorPicker();
	@FXML
	private ColorPicker CHZero = new ColorPicker();
	@FXML
	private ColorPicker CVZero = new ColorPicker();
	@FXML
	private ColorPicker CPlotB = new ColorPicker();
	@FXML
	private ColorPicker CLine = new ColorPicker();
	@FXML
	private ColorPicker CLineS = new ColorPicker();
	@FXML
	private ColorPicker CPlotH = new ColorPicker();
	@FXML
	private ColorPicker CDropS = new ColorPicker();

	// Opacity Variables
	@FXML
	private Slider OHGrid = new Slider();
	@FXML
	private Slider OVGrid = new Slider();
	@FXML
	private Slider OHZero = new Slider();
	@FXML
	private Slider OVZero = new Slider();
	@FXML
	private Slider OLine = new Slider();
	@FXML
	private Slider OLineS = new Slider();
	@FXML
	private Slider OPlotH = new Slider();
	@FXML
	private Slider ODropS = new Slider();

	// Width Variables
	@FXML
	private Slider WHGrid = new Slider();
	@FXML
	private Slider WVGrid = new Slider();
	@FXML
	private Slider WHZero = new Slider();
	@FXML
	private Slider WVZero = new Slider();
	@FXML
	private Slider WLine = new Slider();
	@FXML
	private Slider WLineS = new Slider();
	@FXML
	private Slider WPlotH = new Slider();
	@FXML
	private Slider WDropS = new Slider();

	static// Colour Variables
	int[] rgb = new int[3];

	PiMain main = new PiMain();

	@FXML
	private CheckBox vgrid = new CheckBox();

	@FXML
	private Button prefButton;

	public PiPreferenceController() {
		// TODO Auto-generated constructor stub

	}

	public void writeProp(String key, String value) {
		prop.setProperty(key, value);
	}

	void dialogBuild() throws IOException {
		Stage dialogStage = new Stage();
		FXMLLoader loader = new FXMLLoader(getClass().getResource(
				"PiPreference.fxml"));
		// loader.setController(new PiPreferenceController());
		BorderPane root = (BorderPane) loader.load();
		scene = new Scene(root);
		dialogStage.setScene(scene);
		dialogStage.initStyle(StageStyle.UTILITY);
		dialogStage.showAndWait();

	}

	// This method is used to convert Color object to String
	public static String ColortoHex(Color color) {
		return String.format("%02X%02X%02X", (int) (color.getRed() * 255),
				(int) (color.getGreen() * 255), (int) (color.getBlue() * 255));
	}

	// This method is used to convert Hex String to RGB
	public static int[] HextoRGB(final String hex) {
		// final int[] ret = new int[3];
		for (int i = 0; i < 3; i++) {
			rgb[i] = Integer.parseInt(hex.substring(i * 2, i * 2 + 2), 16);
		}
		return rgb;
	}

	// This method is used to read property
	public void readProp() {
		try {
			input = new FileInputStream("config.properties");
			// load a properties file
			prop.load(input);
			// Grid lines property
			HGrid.setSelected(Boolean.valueOf(prop.getProperty("HGrid")));
			VGrid.setSelected(Boolean.valueOf(prop.getProperty("VGrid")));
			HZero.setSelected(Boolean.valueOf(prop.getProperty("HZero")));
			VZero.setSelected(Boolean.valueOf(prop.getProperty("VZero")));
			PlotH.setSelected(Boolean.valueOf(prop.getProperty("PlotH")));
			DropS.setSelected(Boolean.valueOf(prop.getProperty("DropS")));

			CHGrid.setDisable(!Boolean.valueOf(prop.getProperty("HGrid")));
			CVGrid.setDisable(!Boolean.valueOf(prop.getProperty("VGrid")));
			CHZero.setDisable(!Boolean.valueOf(prop.getProperty("HZero")));
			CVZero.setDisable(!Boolean.valueOf(prop.getProperty("VZero")));

			// Opacity variables
			OHGrid.setDisable(!Boolean.valueOf(prop.getProperty("HGrid")));
			OVGrid.setDisable(!Boolean.valueOf(prop.getProperty("VGrid")));
			OHZero.setDisable(!Boolean.valueOf(prop.getProperty("HZero")));
			OVZero.setDisable(!Boolean.valueOf(prop.getProperty("VZero")));
			OPlotH.setDisable(!Boolean.valueOf(prop.getProperty("PlotH")));
			ODropS.setDisable(!Boolean.valueOf(prop.getProperty("DropS")));

			// Width Variables
			WHGrid.setDisable(!Boolean.valueOf(prop.getProperty("HGrid")));
			WVGrid.setDisable(!Boolean.valueOf(prop.getProperty("VGrid")));
			WHZero.setDisable(!Boolean.valueOf(prop.getProperty("HZero")));
			WVZero.setDisable(!Boolean.valueOf(prop.getProperty("VZero")));
			WPlotH.setDisable(!Boolean.valueOf(prop.getProperty("PlotH")));
			WDropS.setDisable(!Boolean.valueOf(prop.getProperty("DropS")));

			// Grid Colour line Property

			HextoRGB(prop.getProperty("CHGrid"));
			CHGrid.setValue(Color.rgb(rgb[0], rgb[1], rgb[2]));
			HextoRGB(prop.getProperty("CVGrid"));
			CVGrid.setValue(Color.rgb(rgb[0], rgb[1], rgb[2]));
			HextoRGB(prop.getProperty("CHZero"));
			CHZero.setValue(Color.rgb(rgb[0], rgb[1], rgb[2]));
			HextoRGB(prop.getProperty("CVZero"));
			CVZero.setValue(Color.rgb(rgb[0], rgb[1], rgb[2]));
			HextoRGB(prop.getProperty("CPlotB"));
			CPlotB.setValue(Color.rgb(rgb[0], rgb[1], rgb[2]));
			HextoRGB(prop.getProperty("CPlotH"));
			CPlotH.setValue(Color.rgb(rgb[0], rgb[1], rgb[2]));
			HextoRGB(prop.getProperty("CLine"));
			CLine.setValue(Color.rgb(rgb[0], rgb[1], rgb[2]));
			HextoRGB(prop.getProperty("CLineS"));
			CLineS.setValue(Color.rgb(rgb[0], rgb[1], rgb[2]));
			HextoRGB(prop.getProperty("CDropS"));
			CDropS.setValue(Color.rgb(rgb[0], rgb[1], rgb[2]));

			// Opacity set property
			OHGrid.setValue(Double.parseDouble(prop.getProperty("OHGrid")) * 10);
			OVGrid.setValue(Double.parseDouble(prop.getProperty("OVGrid")) * 10);
			OHZero.setValue(Double.parseDouble(prop.getProperty("OHZero")) * 10);
			OVZero.setValue(Double.parseDouble(prop.getProperty("OVZero")) * 10);
			OLine.setValue(Double.parseDouble(prop.getProperty("OLine")) * 10);
			OLineS.setValue(Double.parseDouble(prop.getProperty("OLineS")) * 10);
			OPlotH.setValue(Double.parseDouble(prop.getProperty("OPlotH")) * 10);
			ODropS.setValue(Double.parseDouble(prop.getProperty("ODropS")) * 10);

			// Width set property
			WHGrid.setValue(Double.parseDouble(prop.getProperty("WHGrid")));
			WVGrid.setValue(Double.parseDouble(prop.getProperty("WVGrid")));
			WHZero.setValue(Double.parseDouble(prop.getProperty("WHZero")));
			WVZero.setValue(Double.parseDouble(prop.getProperty("WVZero")));
			WLine.setValue(Double.parseDouble(prop.getProperty("WLine")));
			WLineS.setValue(Double.parseDouble(prop.getProperty("WLineS")));
			WPlotH.setValue(Double.parseDouble(prop.getProperty("WPlotH")));
			WDropS.setValue(Double.parseDouble(prop.getProperty("WDropS")) / 2);

		} catch (IOException e) {
			e.printStackTrace();
		}

		finally {
			if (input != null)
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

		}

	}

	// This method is used to save the preferences
	public void savePref() {
		try {
			output = new FileOutputStream("config.properties");

			// Set properties of Grid lines
			prop.setProperty("HGrid", String.valueOf(HGrid.isSelected()));
			prop.setProperty("VGrid", String.valueOf(VGrid.isSelected()));
			prop.setProperty("HZero", String.valueOf(HZero.isSelected()));
			prop.setProperty("VZero", String.valueOf(VZero.isSelected()));
			prop.setProperty("PlotH", String.valueOf(PlotH.isSelected()));
			prop.setProperty("DropS", String.valueOf(DropS.isSelected()));

			// Set property of colour
			prop.setProperty("CHGrid", ColortoHex(CHGrid.getValue()));
			prop.setProperty("CVGrid", ColortoHex(CVGrid.getValue()));
			prop.setProperty("CHZero", ColortoHex(CHZero.getValue()));
			prop.setProperty("CVZero", ColortoHex(CVZero.getValue()));
			prop.setProperty("CPlotB", ColortoHex(CPlotB.getValue()));
			prop.setProperty("CPlotH", ColortoHex(CPlotH.getValue()));
			prop.setProperty("CLine", ColortoHex(CLine.getValue()));
			prop.setProperty("CLineS", ColortoHex(CLineS.getValue()));
			prop.setProperty("CDropS", ColortoHex(CDropS.getValue()));

			// Opacity
			prop.setProperty("OHGrid", String.valueOf(OHGrid.getValue() / 10));
			prop.setProperty("OVGrid", String.valueOf(OVGrid.getValue() / 10));
			prop.setProperty("OHZero", String.valueOf(OHZero.getValue() / 10));
			prop.setProperty("OVZero", String.valueOf(OVZero.getValue() / 10));
			prop.setProperty("OLine", String.valueOf(OLine.getValue() / 10));
			prop.setProperty("OLineS", String.valueOf(OLineS.getValue() / 10));
			prop.setProperty("OPlotH", String.valueOf(OPlotH.getValue() / 10));
			prop.setProperty("ODropS", String.valueOf(ODropS.getValue() / 10));

			// Width
			prop.setProperty("WHGrid", String.valueOf(WHGrid.getValue()));
			prop.setProperty("WVGrid", String.valueOf(WVGrid.getValue()));
			prop.setProperty("WHZero", String.valueOf(WHZero.getValue()));
			prop.setProperty("WVZero", String.valueOf(WVZero.getValue()));
			prop.setProperty("WLine", String.valueOf(WLine.getValue()));
			prop.setProperty("WLineS", String.valueOf(WLineS.getValue()));
			prop.setProperty("WPlotH", String.valueOf((int)WPlotH.getValue()));
			prop.setProperty("WDropS", String.valueOf(WDropS.getValue() * 2));

			prop.store(output, null);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (output != null)
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

	// This method is used to reset the preference values to default values
	@FXML
	public void reset() {
		try {
			output = new FileOutputStream("config.properties");
			// Set default properties
			prop.setProperty("HGrid", "false");
			prop.setProperty("VGrid", "true");
			prop.setProperty("HZero", "true");
			prop.setProperty("VZero", "false");
			prop.setProperty("CHGrid", "FFFFFF");
			prop.setProperty("CVGrid", "3278fa");
			prop.setProperty("CHZero", "3278fa");
			prop.setProperty("CVZero", "3278fa");
			prop.setProperty("CPlotB", "040603");
			prop.setProperty("CLine", "007701");
			prop.setProperty("CLineS", "A9A9A9");
			prop.setProperty("CPlotH", "56FF6B");
			prop.setProperty("PlotH", "true");
			prop.setProperty("DropS", "true");
			prop.setProperty("OHGrid", "0.3");
			prop.setProperty("OVGrid", "0.3");
			prop.setProperty("OHZero", "0.3");
			prop.setProperty("OVZero", "0.3");
			prop.setProperty("OLine", "1");
			prop.setProperty("OLineS", "1");
			prop.setProperty("OPlotH", "1");
			prop.setProperty("ODropS", "0.1");
			prop.setProperty("WHGrid", "1");
			prop.setProperty("WVGrid", "1");
			prop.setProperty("WHZero", "1");
			prop.setProperty("WVZero", "1");
			prop.setProperty("WLine", "1");
			prop.setProperty("WLineS", "3");
			prop.setProperty("WPlotH", "3");
			prop.setProperty("WDropS", "10");
			prop.setProperty("CDropS", "56FF6B");

			prop.store(output, null);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (output != null)
				try {
					output.close();
					readProp();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}

	}

	// Close Function for button
	@FXML
	public void close() {
		removeListeners();
		savePref();
		prefButton.getScene().getWindow().hide();
	}

	// This function is used to disable the textfield if grid is not enabled.
	void disable(ColorPicker colourPicker, boolean val) {
		colourPicker.setDisable(val);
	}

	// This function is used to disable the textfield if grid is not enabled.
	void disable(Slider slider, boolean val) {
		slider.setDisable(val);
	}

	public void addListeners() {

		// Horizontal Grid Listener
		HGrid.selectedProperty().addListener(new ChangeListener<Boolean>() {
			public void changed(ObservableValue<? extends Boolean> ov,
					Boolean old_val, Boolean new_val) {
				disable(CHGrid, old_val);
				disable(OHGrid, old_val);
				disable(WHGrid, old_val);
			}
		});

		// Vertical Grid Listener
		VGrid.selectedProperty().addListener(new ChangeListener<Boolean>() {
			public void changed(ObservableValue<? extends Boolean> ov,
					Boolean old_val, Boolean new_val) {
				disable(CVGrid, old_val);
				disable(OVGrid, old_val);
				disable(WVGrid, old_val);
			}
		});

		// Horizontal Grid Listener
		HZero.selectedProperty().addListener(new ChangeListener<Boolean>() {
			public void changed(ObservableValue<? extends Boolean> ov,
					Boolean old_val, Boolean new_val) {
				disable(CHZero, old_val);
				disable(OHZero, old_val);
				disable(WHZero, old_val);
			}
		});

		// Horizontal Grid Listener
		VZero.selectedProperty().addListener(new ChangeListener<Boolean>() {
			public void changed(ObservableValue<? extends Boolean> ov,
					Boolean old_val, Boolean new_val) {
				disable(CVZero, old_val);
				disable(OVZero, old_val);
				disable(WVZero, old_val);
			}
		});

		// Plot Highlight Listener
		PlotH.selectedProperty().addListener(new ChangeListener<Boolean>() {
			public void changed(ObservableValue<? extends Boolean> ov,
					Boolean old_val, Boolean new_val) {
				disable(CPlotH, old_val);
				disable(OPlotH, old_val);
				disable(WPlotH, old_val);
			}
		});

		// Drop Shadow Listener
		DropS.selectedProperty().addListener(new ChangeListener<Boolean>() {
			public void changed(ObservableValue<? extends Boolean> ov,
					Boolean old_val, Boolean new_val) {
				disable(CDropS, old_val);
				disable(ODropS, old_val);
				disable(WDropS, old_val);
			}
		});

	}

	// This method is used to remove all the listeners
	public void removeListeners() {
		HGrid.setOnAction(null);
		VGrid.setOnAction(null);
		HZero.setOnAction(null);
		VZero.setOnAction(null);
		PlotH.setOnAction(null);
		DropS.setOnAction(null);
	}

	// Initialization Function
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		addListeners();
		// reset();
		readProp();
	}

}
