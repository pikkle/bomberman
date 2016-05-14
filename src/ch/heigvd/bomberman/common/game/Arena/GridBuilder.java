package ch.heigvd.bomberman.common.game.Arena;

import ch.heigvd.bomberman.common.game.Bomberman;
import ch.heigvd.bomberman.common.game.Direction;
import ch.heigvd.bomberman.common.game.Element;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by matthieu.villard on 12.05.2016.
 */
public class GridBuilder implements Observer
{
	private GridPane gridPane;
	private Arena arena;

	public GridBuilder(Arena arena){
		this.arena = arena;
		gridPane = new GridPane();
		gridPane.setStyle("-fx-background-color: #24B846;");
		gridPane.setGridLinesVisible(true);
		buildGrid(arena);
	}

	public void setBomberman(Bomberman bomberman) {
		bomberman.render().setOnKeyPressed(key -> {
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
					bomberman.dropBomb().ifPresent(this::displayElement);
					break;
				default:
					return;
			}
			key.consume();
		});
		bomberman.render().setFocusTraversable(true);
	}

	public GridPane getGrid(){
		return gridPane;
	}

	@Override
	public void update(Observable o, Object arg) {
		//displayElement((Element)o);
	}

	private void buildGrid(Arena arena){
		for (int i = 0; i < arena.getWidth(); i++) {
			ColumnConstraints column = new ColumnConstraints(50);
			column.setHgrow(Priority.SOMETIMES);
			gridPane.getColumnConstraints().add(column);
		}
		for (int i = 0; i < arena.getHeight(); i++) {
			RowConstraints rowConstraints = new RowConstraints(50);
			rowConstraints.setVgrow(Priority.SOMETIMES);
			gridPane.getRowConstraints().add(rowConstraints);
		}

		arena.getElements().stream().forEach(element -> {
			displayElement(element);
			//element.addObserver(this);
		});
	}

	private void displayElement(Element element){
		if(gridPane.getChildren().contains(element.render()))
			gridPane.getChildren().remove(element.render());
		ImageView sprite = element.render();
		sprite.setFitHeight(50);
		sprite.setFitWidth(50);
		gridPane.add(sprite, (int)element.getPosition().getX(), (int)element.getPosition().getY());
	}
}
