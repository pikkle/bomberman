package ch.heigvd.bomberman.client.views;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;

/**
 * Created by matthieu.villard on 15.06.2016.
 */
public class AboutController {

	@FXML
	private TextArea description;

	@FXML
	private Pane mainPane;

	/**
	 * First method call when FXML loaded.
	 * Used to disable mouse action on the description textArea.
	 */
	@FXML
	private void initialize() {
		description.setFocusTraversable(false);
		description.setMouseTransparent(true);
	}
}
