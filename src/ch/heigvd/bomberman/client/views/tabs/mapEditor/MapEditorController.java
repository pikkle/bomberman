package ch.heigvd.bomberman.client.views.tabs.mapEditor;

import ch.heigvd.bomberman.client.ResponseManager;
import ch.heigvd.bomberman.client.views.render.ArenaRenderer;
import ch.heigvd.bomberman.client.views.render.ImageViewPane;
import ch.heigvd.bomberman.common.communication.Message;
import ch.heigvd.bomberman.common.game.Arena.Arena;
import ch.heigvd.bomberman.common.game.*;
import ch.heigvd.bomberman.common.game.powerups.AddBombPowerUp;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.WindowEvent;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by matthieu.villard on 20.05.2016.
 */
public class MapEditorController implements Observer {

	@FXML private AnchorPane mainPane;

	@FXML private Spinner<Integer> width;

	@FXML private Spinner<Integer> height;

	@FXML private AnchorPane gridContainer;

	@FXML private VBox elementsContainer;

	@FXML private ImageView wall;

	@FXML private ImageView box;

	@FXML private ImageView startPoint;

	@FXML private ImageView addBombPowerUp;

	@FXML private ImageView trash;

	private ResponseManager rm;
	private Arena arena;
	private ArenaRenderer renderer;
	private HashMap<ImageView, Class> elements = new HashMap<>();
	private InsertHandler insertHandler;
	private DeleteHandler deleteHandler;

	@FXML
	private void initialize() {
		rm = ResponseManager.getInstance();
		width.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE));
		height.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE));
		width.valueProperty().addListener((obs, oldValue, newValue) -> changeDimension(newValue, height.getValue()));
		height.valueProperty().addListener((obs, oldValue, newValue) -> changeDimension(width.getValue(), newValue));

		elements.put(wall, Wall.class);
		elements.put(box, Box.class);
		elements.put(startPoint, StartPoint.class);
		elements.put(addBombPowerUp, AddBombPowerUp.class);
	}

	public void loadArena(Arena arena) {
		this.arena = arena;
		changeDimension(arena.getWidth(), arena.getHeight());
	}

	private void changeDimension(int width, int height) {
		if (width < 2 || height < 2) {
			this.width.getValueFactory().setValue(Math.max(width, 2));
			this.height.getValueFactory().setValue(Math.max(height, 2));
		}
		try {
			Arena oldArena = arena;
			arena = new Arena(width, height, arena);
			renderer = new ArenaRenderer(arena, 750, 750);
			if (oldArena != null) {
				for (int x = 1; x < oldArena.getWidth() - 1 && x < arena.getWidth() - 1; x++) {
					for (int y = 1; y < oldArena.getHeight() - 1 && y < arena.getHeight() - 1; y++) {
						oldArena.getElements(new Point(x, y)).forEach(element -> {
							if (arena.isEmpty(element.position())) {
								element.setArena(arena);
								registerDeleteDragEvents(element);
							}
						});
					}
				}
			}

			AnchorPane grid = renderer.getView();
			//registerDropEvent(renderer.getGrid());
			gridContainer.getChildren().clear();
			gridContainer.getChildren().add(grid);
			gridContainer.setTopAnchor(grid, 0.0);
			gridContainer.setLeftAnchor(grid, 0.0);
			gridContainer.setBottomAnchor(grid, 0.0);
			gridContainer.setRightAnchor(grid, 0.0);
			if (arena.getHeight() >= 1 && arena.getWidth() >= 1) registerAddDragEvents();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	private void registerAddDragEvents() {
		Iterator<ImageView> iterator = elements.keySet().iterator();
		while (iterator.hasNext()) {
			ImageView imageView = iterator.next();

			imageView.setOnDragDetected(t -> {
				insertHandler = new InsertHandler(renderer, imageView, renderer.getGrid(), elements.get(imageView));
				insertHandler.addObserver(MapEditorController.this);
				insertHandler.start(t);
			});
		}
	}


	@Override
	public void update(Observable o, Object arg) {
		insertHandler.deleteObserver(this);
		registerDeleteDragEvents((Element) arg);
	}

	private void registerDeleteDragEvents(Element element) {
		ImageViewPane sprite = element.getSprite();

		sprite.setOnDragDetected(t -> {
			deleteHandler = new DeleteHandler(renderer, sprite, trash, element);
			deleteHandler.start(t);
		});
	}

	@FXML
	private void confirm() {
		rm.saveArenaRequest(arena, message -> {
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
		cancel();
	}

	private void failure(Message message) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setContentText(message.getMessage());
		alert.showAndWait();
	}

	@FXML
	private void cancel() {
		mainPane.getScene()
		        .getWindow()
		        .fireEvent(new WindowEvent(mainPane.getScene().getWindow(), WindowEvent.WINDOW_CLOSE_REQUEST));
	}
}
