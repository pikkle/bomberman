package ch.heigvd.bomberman.client.views.render;

import ch.heigvd.bomberman.common.game.Arena.Arena;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;

/**
 * Created by matthieu.villard on 18.05.2016.
 */
public class ArenaRenderer
{
	private Arena arena;
	private GridPane gridPane;
	private double cellWidth;
	private double cellHeight;

	public ArenaRenderer(Arena arena, double totalWidth, double totalHeight) {
		this.arena = arena;
		cellHeight = totalHeight / arena.getHeight();
		cellWidth = totalWidth / arena.getWidth();
	}

	public void renderElement(ImageView sprite, int x, int y){
		if (gridPane.getChildren().contains(sprite)) gridPane.getChildren().remove(sprite);
		sprite.setFitHeight(cellHeight);
		sprite.setFitWidth(cellWidth);
		gridPane.add(sprite, x, y);
	}

	public GridPane render(){
		gridPane = new GridPane();
		gridPane.setAlignment(Pos.CENTER);
		gridPane.setStyle("-fx-background-color: #24B846;");
		gridPane.setGridLinesVisible(true);

		for (int i = 0; i < arena.getWidth(); i++) {
			ColumnConstraints column = new ColumnConstraints(cellWidth);
			column.setHgrow(Priority.SOMETIMES);
			gridPane.getColumnConstraints().add(column);
		}
		for (int i = 0; i < arena.getHeight(); i++) {
			RowConstraints rowConstraints = new RowConstraints(cellHeight);
			rowConstraints.setVgrow(Priority.SOMETIMES);
			gridPane.getRowConstraints().add(rowConstraints);
		}

		ElementRenderer elementRenderer = new ElementRenderer(this);
		arena.getElements().stream().forEach(element -> {
			element.addObserver(elementRenderer);
			element.accept(elementRenderer);
		});
		return gridPane;
	}
}
