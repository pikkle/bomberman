package ch.heigvd.bomberman.client.views.render;

import ch.heigvd.bomberman.client.ResponseManager;
import ch.heigvd.bomberman.common.game.Arena.Arena;
import ch.heigvd.bomberman.common.game.Element;
import ch.heigvd.bomberman.common.util.ImageViewPane;
import ch.heigvd.bomberman.common.game.Point;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.layout.*;

import java.util.Observable;
import java.util.Observer;

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

		arena.elements().stream().forEach(this::renderElement);

		BorderPane center = new BorderPane(gridPane);
		AnchorPane.setTopAnchor(center, 0.0);
		AnchorPane.setLeftAnchor(center, 0.0);
		AnchorPane.setRightAnchor(center, 0.0);
		AnchorPane.setBottomAnchor(center, 0.0);
		container.getChildren().add(center);

		//container.setStyle("-fx-background-color: grey;");
	}

	public void renderElement(Element element) {
		ImageViewPane sprite = element.getSprite();
		gridPane.getChildren().remove(sprite);
		if (arena.elements().contains(element)) gridPane.add(sprite, element.x(), element.y());
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
		double width;
		double height;
		for (width = 0; x > width + this.width / arena.width(); width += this.width / arena.width(), xVal++) ;
		for (height = 0; y > height + this.height / arena.height(); height += this.height / arena.height(), yVal++) ;
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
