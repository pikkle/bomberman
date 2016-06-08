package ch.heigvd.bomberman.client.views.render;

import ch.heigvd.bomberman.client.ResponseManager;
import ch.heigvd.bomberman.common.game.Arena.Arena;
import ch.heigvd.bomberman.common.game.Bomberman;
import ch.heigvd.bomberman.common.game.Direction;
import ch.heigvd.bomberman.common.game.Element;
import ch.heigvd.bomberman.common.game.Point;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.layout.*;

import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.stream.Collectors;

/**
 * Created by matthieu.villard on 18.05.2016.
 */
public class ArenaRenderer implements Observer {
	private Arena arena;
	private GridPane gridPane;
	private AnchorPane container;
	private double width;
	private double height;
	private ResponseManager rm = ResponseManager.getInstance();

	public ArenaRenderer(Arena arena, double width, double height) {
		this.arena = arena;
		arena.addObserver(this);
		this.width = width;
		this.height = height;

		container = new AnchorPane();
		gridPane = new GridPane();
		gridPane.setAlignment(Pos.CENTER);
		gridPane.setStyle("-fx-background-color: #24B846;");
		gridPane.setGridLinesVisible(true);

		resize(width, height);

		container.widthProperty().addListener((observableValue, oldSceneWidth, newSceneWidth) -> {
			resize();
		});

		container.heightProperty().addListener((observableValue, oldSceneWidth, newSceneWidth) -> {
			resize();
		});

		arena.getElements().stream().forEach(this::renderElement);

		BorderPane center = new BorderPane(gridPane);
		AnchorPane.setTopAnchor(center, 0.0);
		AnchorPane.setLeftAnchor(center, 0.0);
		AnchorPane.setRightAnchor(center, 0.0);
		AnchorPane.setBottomAnchor(center, 0.0);
		container.getChildren().add(center);

		//container.setStyle("-fx-background-color: grey;");
	}

	public ArenaRenderer(Arena arena, Bomberman bomberman, double width, double height) {
		this(arena, width, height);
		if (bomberman.getSprite() != null) {
			bomberman.getSprite().setOnKeyPressed(key -> {
				switch (key.getCode()) {
					case RIGHT:
						rm.moveRequest(Direction.RIGHT, null);
						break;
					case LEFT:
						rm.moveRequest(Direction.LEFT, null);
						break;
					case DOWN:
						rm.moveRequest(Direction.DOWN, null);
						break;
					case UP:
						rm.moveRequest(Direction.UP, null);
						break;
					case SPACE:
						rm.dropBombRequest();
						break;
					default:
						return;
				}
				key.consume();
			});
			bomberman.getSprite().setFocusTraversable(true);
		}
	}

	public void renderElement(Element element) {
		ImageViewPane sprite = element.getSprite();
		if (sprite != null && gridPane.getChildren().contains(sprite)) {
			gridPane.getChildren().remove(sprite);
		}
		if (arena.getElements().contains(element)) {
			// display elements, behind existing ones
			List<Node> mem = gridPane.getChildren()
			                         .filtered(child -> child instanceof ImageViewPane &&
			                                            GridPane.getRowIndex(child) == element.y() &&
			                                            GridPane.getColumnIndex(child) == element.x())
			                         .stream()
			                         .collect(Collectors.toList());

			mem.forEach(node -> gridPane.getChildren().remove(node));

			if (sprite != null) {
				gridPane.add(sprite, element.x(), element.y());
			}

			mem.forEach(node -> gridPane.add(node, element.x(), element.y()));
		}
	}

	public Arena getArena() {
		return arena;
	}

	public AnchorPane getView() {
		return container;
	}

	public GridPane getGrid() {
		return gridPane;
	}

	public double getWidth() {
		return width;
	}

	public double getHeight() {
		return height;
	}

	public double getCellWidth() {
		return width / arena.width();
	}

	public double getCellHeight() {
		return height / arena.height();
	}

	public Point getCell(double x, double y) {
		int xVal = 0;
		int yVal = 0;
		double width = 0;
		double height = 0;
		for (width = 0; x > width + this.width / arena.width(); width += this.width / arena.width(), xVal++) ;
		for (height = 0;
		     y > height + this.height / arena.height(); height += this.height / arena.height(), yVal++)
			;
		return new Point(Math.min(xVal, arena.width() - 1), Math.min(yVal, arena.height() - 1));
	}

	@Override
	public void update(Observable o, Object arg) {
		Element element = (Element) arg;
		renderElement(element);
	}

	public void resize(double width, double height) {
		double size = Math.min(height / arena.height(), width / arena.width());
		this.width = arena.width() * size;
		this.height = arena.height() * size;
		gridPane.setMaxWidth(this.width);
		gridPane.setMaxHeight(this.height);
		gridPane.getRowConstraints().clear();
		gridPane.getColumnConstraints().clear();

		for (int i = 0; i < arena.height(); i++) {
			RowConstraints rowConstraints = new RowConstraints();
			rowConstraints.setVgrow(Priority.NEVER);
			rowConstraints.setFillHeight(true);
			rowConstraints.setMinHeight(10);
			rowConstraints.setValignment(VPos.CENTER);
			rowConstraints.setPrefHeight(size);
			gridPane.getRowConstraints().add(rowConstraints);
		}
		for (int i = 0; i < arena.width(); i++) {
			ColumnConstraints column = new ColumnConstraints();
			column.setHgrow(Priority.NEVER);
			column.setFillWidth(true);
			column.setMinWidth(10);
			column.setHalignment(HPos.CENTER);
			column.setPrefWidth(size);
			gridPane.getColumnConstraints().add(column);
		}
	}

	private void resize() {
		resize(container.getWidth(), container.getHeight());
	}
}
