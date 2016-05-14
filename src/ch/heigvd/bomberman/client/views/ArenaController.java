package ch.heigvd.bomberman.client.views;

import ch.heigvd.bomberman.common.game.Arena.Arena;
import ch.heigvd.bomberman.common.game.Bomberman;
import ch.heigvd.bomberman.common.game.Direction;
import ch.heigvd.bomberman.common.game.Element;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;

import java.util.Observable;
import java.util.Observer;

public class ArenaController implements Observer {
    private Arena arena;
    private Bomberman bomberman;

    @FXML
    private GridPane gridPane;

    public ArenaController(Arena arena) {
        this.arena = arena;
        try {
            this.bomberman = arena.putBomberman();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void initialize() {
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

        //arena.addObserver(this);
        arena.getElements().stream().forEach(element -> {
            displayElement(element);
        });

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

    @Override
    public void update(Observable o, Object arg) {
        //displayElement((Element) o);
    }

    private void displayElement(Element element) {
        if (gridPane.getChildren().contains(element.render())) gridPane.getChildren().remove(element.render());
        ImageView sprite = element.render();
        sprite.setFitHeight(50);
        sprite.setFitWidth(50);
        gridPane.add(sprite, (int) element.getPosition().getX(), (int) element.getPosition().getY());
    }
}