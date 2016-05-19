package ch.heigvd.bomberman.client.views.render;

import ch.heigvd.bomberman.common.game.Arena.Arena;
import ch.heigvd.bomberman.common.game.Element;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by matthieu.villard on 18.05.2016.
 */
public class ArenaRenderer implements Observer
{
	private Arena arena;
	private GridPane gridPane;
	private double cellWidth;
	private double cellHeight;
	private ElementRenderer elementRenderer;

	public ArenaRenderer(Arena arena, double totalWidth, double totalHeight) {
		this.arena = arena;
		arena.addObserver(this);
		cellHeight = totalHeight / arena.getHeight();
		cellWidth = totalWidth / arena.getWidth();
		elementRenderer = new ElementRenderer(this);
	}

	public void renderElement(Element element){
		if(elementRenderer.getSprite(element) != null && gridPane.getChildren().contains(elementRenderer.getSprite(element))){
			gridPane.getChildren().remove(elementRenderer.getSprite(element));
		}
		if(arena.getElements().contains(element)){
			element.accept(elementRenderer);
			ImageView sprite = elementRenderer.getSprite(element);
			sprite.setFitHeight(cellHeight);
			sprite.setFitWidth(cellWidth);
			gridPane.add(sprite, element.x(), (int)element.y());
		}
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

		arena.getElements().stream().forEach(element -> {
			renderElement(element);
		});
		return gridPane;
	}

	@Override
	public void update(Observable o, Object arg) {
		Element element = (Element)arg;
		renderElement(element);
	}
}
