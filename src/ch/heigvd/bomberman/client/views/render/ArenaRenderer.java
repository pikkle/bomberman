package ch.heigvd.bomberman.client.views.render;

import ch.heigvd.bomberman.common.game.Arena.Arena;
import ch.heigvd.bomberman.common.game.Bomberman;
import ch.heigvd.bomberman.common.game.Direction;
import ch.heigvd.bomberman.common.game.Element;
import ch.heigvd.bomberman.common.game.Point;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.layout.*;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by matthieu.villard on 18.05.2016.
 */
public class ArenaRenderer implements Observer
{
	private Arena arena;
	private GridPane gridPane;
	AnchorPane container;
	private double width;
	private double height;
	private ElementRenderer elementRenderer;

	public ArenaRenderer(Arena arena, double width, double height) {
		this.arena = arena;
		arena.addObserver(this);
		this.width = width;
		this.height = height;
		elementRenderer = new ElementRenderer();

		container = new AnchorPane();
		gridPane = new GridPane();
		gridPane.setAlignment(Pos.CENTER);
		gridPane.setStyle("-fx-background-color: #24B846;");
		gridPane.setGridLinesVisible(true);

		resize(width, height);

		container.widthProperty().addListener(new ChangeListener<Number>() {
			@Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
				resize();
			}
		});

		container.heightProperty().addListener(new ChangeListener<Number>() {
			@Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
				resize();
			}
		});

		arena.getElements().stream().forEach(element -> {
			renderElement(element);
		});

		BorderPane center = new BorderPane(gridPane);
		container.setTopAnchor(center, 0.0);
		container.setLeftAnchor(center, 0.0);
		container.setRightAnchor(center, 0.0);
		container.setBottomAnchor(center, 0.0);
		container.getChildren().add(center);

		container.setStyle("-fx-background-color: grey;");
	}

	public ArenaRenderer(Arena arena, Bomberman bomberman, double width, double height) {
		this(arena, width, height);
		arena.remove(arena.getElements(bomberman.position()).stream().findFirst().get());
		arena.add(bomberman);
		renderElement(bomberman);
		elementRenderer.getSprite(bomberman).setOnKeyPressed(key -> {
			switch (key.getCode()) {
				case RIGHT:
					bomberman.move(Direction.RIGHT);
					break;
				case LEFT:
					bomberman.move(Direction.LEFT);
					break;
				case DOWN:
					bomberman.move(Direction.DOWN);
					break;
				case UP:
					bomberman.move(Direction.UP);
					break;
				case SPACE:
					bomberman.dropBomb();
					break;
				default:
					return;
			}
			key.consume();
		});
		elementRenderer.getSprite(bomberman).setFocusTraversable(true);
	}

	public void renderElement(Element element){
		if(elementRenderer.getSprite(element) != null && gridPane.getChildren().contains(elementRenderer.getSprite(element))){
			gridPane.getChildren().remove(elementRenderer.getSprite(element));
		}
		if(arena.getElements().contains(element)){
			element.accept(elementRenderer);
			ImageViewPane container = elementRenderer.getSprite(element);
			if(container != null) {
				gridPane.add(container, element.x(), element.y());
			}
		}
	}

	public ImageViewPane getSprite(Element element){
		return elementRenderer.getSprite(element);
	}

	public Arena getArena(){
		return arena;
	}

	public AnchorPane getView(){
		return container;
	}

	public GridPane getGrid(){
		return gridPane;
	}

	public double getWidth(){
		return width;
	}

	public double getHeight(){
		return height;
	}

	public double getCellWidth(){
		return width / arena.getWidth();
	}

	public double getCellHeight(){
		return height / arena.getHeight();
	}

	public Point getCell(double x, double y){
		int xVal = 0;
		int yVal = 0;
		double width = 0;
		double height = 0;
		for(width = 0; x > width + this.width / arena.getWidth(); width += this.width / arena.getWidth(), xVal++);
		for(height = 0; y > height + this.height / arena.getHeight(); height += this.height / arena.getHeight(), yVal++);
		return new Point(Math.min(xVal, arena.getWidth() - 1), Math.min(yVal, arena.getHeight() - 1));
	}

	@Override
	public void update(Observable o, Object arg) {
		Element element = (Element)arg;
		renderElement(element);
	}

	public void resize(double width, double height){
		double size = Math.min(height / arena.getHeight(), width / arena.getWidth());
		this.width = arena.getWidth() * size;
		this.height = arena.getHeight() * size;
		gridPane.setMaxWidth(this.width);
		gridPane.setMaxHeight(this.height);
		gridPane.getRowConstraints().clear();
		gridPane.getColumnConstraints().clear();

		for (int i = 0; i < arena.getHeight(); i++) {
			RowConstraints rowConstraints = new RowConstraints();
			rowConstraints.setVgrow(Priority.NEVER);
			rowConstraints.setFillHeight(true);
			rowConstraints.setMinHeight(10);
			rowConstraints.setValignment(VPos.CENTER);
			rowConstraints.setPrefHeight(size);
			gridPane.getRowConstraints().add(rowConstraints);
		}
		for (int i = 0; i < arena.getWidth(); i++) {
			ColumnConstraints column = new ColumnConstraints();
			column.setHgrow(Priority.NEVER);
			column.setFillWidth(true);
			column.setMinWidth(10);
			column.setHalignment(HPos.CENTER);
			column.setPrefWidth(size);
			gridPane.getColumnConstraints().add(column);
		}
	}

	private void resize(){
		resize(container.getWidth(), container.getHeight());
	}
}
