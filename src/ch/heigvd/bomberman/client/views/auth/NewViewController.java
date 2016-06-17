package ch.heigvd.bomberman.client.views.auth;

import ch.heigvd.bomberman.client.ResponseManager;
import ch.heigvd.bomberman.common.communication.Message;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Created by julien on 08.05.16.
 */
public class NewViewController {

	private ResponseManager rm;

	@FXML
	private Pane mainPane;

	@FXML
	private TextField userId;
	@FXML
	private PasswordField pwd, pwdc;

	@FXML
	private void initialize() {
		rm = ResponseManager.getInstance();
	}

	@FXML
	private void confirm() {
		if (!rm.isConnected()) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText("You are not connected");
			alert.showAndWait();
		} else if (userId.getText().isEmpty()) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText("The user name is empty");
			alert.showAndWait();
		} else if (pwd.getText().isEmpty()) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText("The password name is empty");
			alert.showAndWait();
		} else if (!pwd.getText().equals(pwdc.getText())) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText("The two password are not the same");
			alert.showAndWait();
		} else {
			rm.newAccountRequest(userId.getText(), pwd.getText(), message -> {
				if (message.state()) {
					creationSucces(message);
				} else {
					creationFailure(message);
				}
			});
		}
	}

	private void creationSucces(Message message) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setContentText(message.getMessage());
		alert.showAndWait();
		((Stage) mainPane.getScene().getWindow()).close();
	}

	private void creationFailure(Message message) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setContentText(message.getMessage());
		alert.showAndWait();
	}

	@FXML
	private void cancel() {
		((Stage) mainPane.getScene().getWindow()).close();
	}

}