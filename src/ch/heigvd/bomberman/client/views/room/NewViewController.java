package ch.heigvd.bomberman.client.views.room;

import ch.heigvd.bomberman.client.Client;
import ch.heigvd.bomberman.client.ResponseManager;
import ch.heigvd.bomberman.client.views.game.ReadyController;
import ch.heigvd.bomberman.client.views.render.ArenaRenderer;
import ch.heigvd.bomberman.client.views.tabs.controllers.RoomsController;
import ch.heigvd.bomberman.common.communication.Message;
import ch.heigvd.bomberman.common.game.Arena;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

/**
 * Created by matthieu.villard on 10.05.2016.
 */
public class NewViewController {
	private List<Arena> arenas;
	private int selected = 0;
	private ResponseManager rm;

	@FXML
	private AnchorPane mainPane;

	@FXML
	private AnchorPane arenaContainer;

	@FXML
	private Label lblRoom;

	@FXML
	private TextField roomName;

	@FXML
	private Spinner<Integer> minPlayer;

	@FXML
	private CheckBox isPrivate;

	@FXML
	private PasswordField password;

	@FXML
	private Label lblPassword;

	@FXML
	private void initialize() throws Exception {
		rm = ResponseManager.getInstance();
		minPlayer.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(2, 4));
		isPrivate.selectedProperty().addListener((obs, oldValue, newValue) -> {
			password.setDisable(!newValue);
			lblPassword.setDisable(!newValue);
		});

		if (rm.isConnected()) {
			rm.arenasRequest(arenas -> {
				this.arenas = arenas;
				refreshArena();
			});
		}
	}

	@FXML
	private void next() {
		if (arenas != null && selected < arenas.size() - 1) {
			selected++;
		} else {
			selected = 0;
		}
		refreshArena();
	}

	@FXML
	private void previous() {
		if (selected > 0) {
			selected--;
		} else if (arenas != null) {
			selected = arenas.size() - 1;
		}
		refreshArena();
	}

	@FXML
	private void create() {
		if (!rm.isConnected()) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText("You are not connected");
			alert.showAndWait();
		} else if (roomName.getText().isEmpty()) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText("The room name is empty");
			alert.showAndWait();
		} else if (minPlayer.getValue() < 2 || minPlayer.getValue() > 4) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText("The min player value must be a number between 2 and 4");
			alert.showAndWait();
		} else if (isPrivate.isSelected() && password.getText().isEmpty()) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText("A private room must have a password");
			alert.showAndWait();
		} else if (arenas != null && !arenas.isEmpty()) {
			String hashPasswd = password.getText();
			String name = roomName.getText();
			rm.createRoomRequest(name, arenas.get(selected).getId(), minPlayer.getValue(), hashPasswd, message -> {
				if (message.state()) {
					createSuccess(name, hashPasswd);
				} else {
					createFailure(message);
				}
			});
		}
	}

	private void createSuccess(String name, String password) {
		rm.joinRoomRequest(name, password, r -> {
			Stage stage = new Stage();
			stage.setTitle("Bomberman");

			stage.setOnCloseRequest(event -> {
				rm.readyRequest(false, null);
				Client.getInstance().getPrimatyStage().show();
			});

			stage.initModality(Modality.APPLICATION_MODAL);

			FXMLLoader loader = new FXMLLoader(Client.class.getResource("views/game/ready.fxml"));
			try {
				Pane pane = loader.load();
				ReadyController controller = loader.getController();
				controller.loadRoom(r);
				RoomsController.getInstance().addObserver(controller);
				stage.setScene(new Scene(pane));

				Client.getInstance().getPrimatyStage().hide();
				stage.showAndWait();

			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		close();
	}

	private void createFailure(Message message) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setContentText(message.getMessage());
		alert.showAndWait();
	}

	@FXML
	private void close() {
		((Stage) mainPane.getScene().getWindow()).close();
	}

	private void refreshArena() {
		if (arenas == null || arenas.isEmpty())
			return;
		arenaContainer.getChildren().clear();
		AnchorPane grid = new ArenaRenderer(arenas.get(selected), 225, 225).getView();
		arenaContainer.getChildren().add(grid);
		arenaContainer.setTopAnchor(grid, 0.0);
		arenaContainer.setLeftAnchor(grid, 0.0);
		arenaContainer.setBottomAnchor(grid, 0.0);
		arenaContainer.setRightAnchor(grid, 0.0);
		lblRoom.setText(selected + 1 + " / " + arenas.size());
	}
}