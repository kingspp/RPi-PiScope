package com.piscope;
	


import application.Main;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
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
			//Create a Stage and a Scene
			FXMLLoader fxmlLoader = new FXMLLoader();			
			BorderPane root = fxmlLoader.load(getClass().getResource("Sample.fxml").openStream());
			piController = (PiController) fxmlLoader.getController();			
			Scene PiScene = new Scene(root,1150,720);
			
			//Add Mouse Handler to the Scene
			PiMain.MouseHandler mouseHandler = new PiMain.MouseHandler( root );
			
			//Associate Handler to various Mouse Events	
			root.setOnMouseClicked( mouseHandler );
		    root.setOnMouseDragged(mouseHandler);
		    root.setOnMousePressed(mouseHandler);
		    root.setOnMouseReleased(mouseHandler);
		    root.setOnMouseMoved( mouseHandler );
		    root.setOnMouseDragEntered(mouseHandler);
		    
		    // Add Path for the line
		    linePath = new Path();
		    linePath.setStrokeWidth(1.5);
		    linePath.setStroke(Color.BLACK);		    
		    root.getChildren().add(linePath);
			
			PiScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(PiScene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
