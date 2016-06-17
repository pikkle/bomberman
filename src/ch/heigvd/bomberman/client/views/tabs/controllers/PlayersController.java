package ch.heigvd.bomberman.client.views.tabs.controllers;

import ch.heigvd.bomberman.client.Client;
import ch.heigvd.bomberman.client.ResponseManager;
import ch.heigvd.bomberman.common.communication.Message;
import ch.heigvd.bomberman.common.game.Player;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.util.Observable;

/**
 * Created by matthieu.villard on 09.05.2016.
 */

public class PlayersController extends Observable {
	private ResponseManager rm;
	private ObservableList<Player> players = FXCollections.observableArrayList();
	private Player player;

	@FXML
	private TableView<Player> playersTable;

	@FXML
	private TableColumn<Player, ImageView> isAdmin;

	@FXML
	private TableColumn<Player, ImageView> isLocked;

	@FXML
	public void initialize() throws IOException {

		rm = ResponseManager.getInstance();

		isAdmin.setCellValueFactory(param -> {
			if (param.getValue().isAdmin())
				return new SimpleObjectProperty<ImageView>(new ImageView(new Image(Client.class.getResourceAsStream("img/ok_sign.png"))));
			else
				return new SimpleObjectProperty<ImageView>(new ImageView(new Image(Client.class.getResourceAsStream("img/ko_sign.png"))));
		});

		isLocked.setCellValueFactory(param -> {
			if (param.getValue().isLocked())
				return new SimpleObjectProperty<ImageView>(new ImageView(new Image(Client.class.getResourceAsStream("img/lock.png"))));
			else
				return new SimpleObjectProperty<ImageView>(new ImageView(new Image(Client.class.getResourceAsStream("img/unlock.png"))));

		});

		playersTable.setItems(players);
		rm.playersRequest(p -> players.setAll(p));

	}

	@FXML
	private void banish() {
		if (!playersTable.getSelectionModel().isEmpty()) {
			changeState(playersTable.getSelectionModel().getSelectedItem().getId(), true);
		}
	}

	@FXML
	private void reset() {
		if (!playersTable.getSelectionModel().isEmpty()) {
			changeState(playersTable.getSelectionModel().getSelectedItem().getId(), false);
		}
	}

	private void changeState(long id, boolean isLocked) {
		rm.playerStateRequest(id, isLocked, message -> {
			if (message.state()) {
				success(message);
			} else {
				failure(message);
			}
		});
	}

	private void success(Message message) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setContentText(message.getMessage());
		alert.showAndWait();
		rm.playersRequest(p -> players.setAll(p));
	}

	private void failure(Message message) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setContentText(message.getMessage());
		alert.showAndWait();
	}
}
