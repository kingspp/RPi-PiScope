package com.piscope;

import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;

public class PiSplashController {

	@FXML
	ProgressBar progressBar;

	public void updatePB() throws InterruptedException {
		progressBar = new ProgressBar(0.6);
		Thread.sleep(3000);

	}

}
