package ch.heigvd.bomberman.client.views.tabs.controllers;

import ch.heigvd.bomberman.client.Client;
import ch.heigvd.bomberman.client.ResponseManager;
import ch.heigvd.bomberman.client.views.render.ArenaRenderer;
import ch.heigvd.bomberman.client.views.tabs.mapEditor.MapEditorController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by matthieu.villard on 03.06.2016.
 */
public class MapsController {

	private ResponseManager rm;
	private Client client;

	@FXML
	private AnchorPane mainPane;

	@FXML
	FlowPane mapsContainer;

	public MapsController(){
		client = Client.getInstance();
	}

	@FXML
	public void initalize() {
		rm = ResponseManager.getInstance();
		rm.arenasRequest(maps -> {
			mapsContainer.getChildren().clear();
			maps.forEach(map -> {
				ArenaRenderer renderer = new ArenaRenderer(map, 225, 225);
				AnchorPane grid = renderer.getView();
				grid.setOnMouseClicked(event -> {
					if (event.getButton().equals(MouseButton.PRIMARY)) {
						// double click
						Stage stage = new Stage();
						AnchorPane pane;
						FXMLLoader loader = new FXMLLoader(
								Client.class.getResource("views/tabs/mapEditor/MapEditor.fxml"));

						stage.setTitle("MapEditor");
						stage.setOnCloseRequest(we -> initalize());
						try {
							pane = loader.load();
							MapEditorController controller = loader.getController();
							controller.loadArena(map);
							stage.initModality(Modality.APPLICATION_MODAL);

							stage.setScene(new Scene(pane));
							stage.showAndWait();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				});
				final ContextMenu contextMenu = new ContextMenu();
				MenuItem delete = new MenuItem("Delete");
				delete.setOnAction(e -> rm.removeArenaRequest(map, message -> initalize()));
				contextMenu.getItems().add(delete);
				renderer.getView()
				        .setOnContextMenuRequested(
						        e -> contextMenu.show(renderer.getView(), e.getScreenX(), e.getScreenY()));

				renderer.getView().getStyleClass().add("map");
				mapsContainer.getChildren().add(renderer.getView());
			});
		});
	}

	public void setClient(Client client) {
		this.client = client;
	}

	@FXML
	private void addArena() {
		// double click
		Stage stage = new Stage();
		AnchorPane pane;
		FXMLLoader loader = new FXMLLoader(Client.class.getResource("views/tabs/mapEditor/MapEditor.fxml"));

		stage.setTitle("MapEditor");
		stage.setOnCloseRequest(we -> initalize());
		try {
			pane = loader.load();
			MapEditorController controller = loader.getController();
			stage.initModality(Modality.APPLICATION_MODAL);

			stage.setScene(new Scene(pane));
			stage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
