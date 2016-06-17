package ch.heigvd.bomberman.client.views.game;

import ch.heigvd.bomberman.client.Client;
import ch.heigvd.bomberman.client.ResponseManager;
import ch.heigvd.bomberman.common.game.Room;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by matthieu.villard on 28.05.2016.
 */
public class ReadyController implements Observer {

	private ResponseManager rm;
	private Room room;
	private Client client;

	@FXML
	private Label lblRoom, number;

	@FXML
	private AnchorPane mainPane;

	public ReadyController() {
		client = Client.getInstance();
	}

	@FXML
	private void initialize() {
		rm = ResponseManager.getInstance();
	}

	public void loadRoom(Room room) {
		this.room = room;
		lblRoom.setText("Room \"" + room.getName() + "\"");
		number.setText(room.getPlayers().size() + " player" + (room.getPlayers().size() > 1 ? "s" : ""));
	}

	@FXML
	private void leave() {
		rm.readyRequest(false, null);
		((Stage) mainPane.getScene().getWindow()).close();
		client.getPrimatyStage().show();
	}

	@FXML
	private void ready() {
		rm.readyRequest(true, bomberman -> {
			if (bomberman != null) {
				Stage stage = new Stage();
				AnchorPane pane;
				GameController controller;
				FXMLLoader loader = new FXMLLoader(Client.class.getResource("views/game/GameView.fxml"));

				stage.setTitle("Bomberman");

				stage.setOnCloseRequest(event -> {
					rm.readyRequest(false, null);
					event.consume();
				});

				try {
					pane = loader.load();

					controller = loader.getController();
					controller.loadGame(bomberman, room);

					stage.initModality(Modality.APPLICATION_MODAL);

					stage.setScene(new Scene(pane));

					((Stage) mainPane.getScene().getWindow()).close();
					stage.show();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public void update(Observable o, Object arg) {
		List<Room> rooms = (List<Room>) arg;
		if (rooms.contains(room)) {
			loadRoom(room);
		} else if (rooms.stream().filter(r -> r.getName().equals(room.getName())).findFirst().isPresent()) {
			loadRoom(rooms.stream().filter(r -> r.getName().equals(room.getName())).findFirst().get());
		}
	}
}
