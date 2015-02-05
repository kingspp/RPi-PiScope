package com.piscope;
	
import application.SampleController;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Path;
import javafx.fxml.FXMLLoader;


public class PiMain extends Application {
	
	//Variable declarations
	
	//Line Path Declaratrions
	Path linePath;
	PiController piController ;
	String label;
	
	//Axis Declarations
	double xa1,xa2,ya1,ya2,diff;
	
	
	@Override
	public void start(Stage primaryStage) {
		try {
			BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("PiView.fxml"));
			Scene scene = new Scene(root,1150,650);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
