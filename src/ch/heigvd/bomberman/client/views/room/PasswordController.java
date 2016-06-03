package ch.heigvd.bomberman.client.views.room;

import ch.heigvd.bomberman.client.Client;
import ch.heigvd.bomberman.client.ResponseManager;
import ch.heigvd.bomberman.client.views.game.ReadyController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by matthieu.villard on 02.06.2016.
 */
public class PasswordController {

	private ResponseManager rm;
	private Client client;
	private RoomsController roomsController;

	@FXML private AnchorPane mainPane;

	@FXML private PasswordField pwd;


	@FXML
	private void initialize() throws IOException {
		rm = ResponseManager.getInstance();
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public void setRoomsController(RoomsController roomsController) {
		this.roomsController = roomsController;
	}

	@FXML
	private void close() {
		((Stage) mainPane.getScene().getWindow()).close();
	}

	@FXML
	public void join() {
		rm.joinRoomRequest(roomsController.getRoom(), pwd.getText(), r -> {
			Stage stage = new Stage();
			stage.setTitle("Bomberman");

			stage.setOnCloseRequest(event -> {
				rm.readyRequest(false, null);
				client.getPrimatyStage().show();
			});

			stage.initModality(Modality.APPLICATION_MODAL);

			FXMLLoader loader = new FXMLLoader(Client.class.getResource("views/game/ready.fxml"));
			try {
				Pane pane = loader.load();
				ReadyController controller = loader.getController();
				controller.setClient(client);
				controller.loadRoom(r);
				roomsController.addObserver(controller);
				stage.setScene(new Scene(pane));

				client.getPrimatyStage().hide();
				stage.showAndWait();

			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		close();
	}
}