package ch.heigvd.bomberman.server.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;

/**
 * Created by matthieu.villard on 14.06.2016.
 */
public class ConsoleController {

	@FXML
	private AnchorPane mainPane;

	@FXML
	private void initialize() {

	}

	public void setConsole(TextArea console) {
		console.setEditable(false);
		mainPane.getChildren().clear();
		mainPane.getChildren().add(console);
		mainPane.setTopAnchor(console, 0.0);
		mainPane.setLeftAnchor(console, 0.0);
		mainPane.setBottomAnchor(console, 0.0);
		mainPane.setRightAnchor(console, 0.0);
	}
}
