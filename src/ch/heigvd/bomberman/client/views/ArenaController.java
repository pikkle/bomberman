package ch.heigvd.bomberman.client.views;

import ch.heigvd.bomberman.common.game.Arena;
import ch.heigvd.bomberman.common.game.Bomberman;
import ch.heigvd.bomberman.common.game.Direction;
import ch.heigvd.bomberman.common.game.Element;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;

import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class ArenaController implements Observer
{
    private Arena arena;
    private Bomberman bomberman;

    @FXML
    private GridPane gridPane;

    public ArenaController(Arena arena) {
        this.arena = arena;
        this.bomberman = arena.addPlayer();
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

        arena.getElements().stream().forEach(element -> {
            displayElement(element);
            element.addObserver(this);
        });

        bomberman.render().setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent key) {
                Point position = (Point)bomberman.getPosition().clone();
                if (key.getCode().equals(KeyCode.RIGHT)) {
                    bomberman.move(Direction.RIGHT);
                    key.consume();
                }
                else if(key.getCode().equals(KeyCode.LEFT)){
                    bomberman.move(Direction.LEFT);
                    key.consume();
                }
                else if(key.getCode().equals(KeyCode.DOWN)){
                    bomberman.move(Direction.DOWN);
                    key.consume();
                }
                else if(key.getCode().equals(KeyCode.UP)){
                    bomberman.move(Direction.UP);
                    key.consume();
                }
                else if(key.getCode().equals(KeyCode.SPACE)){
                    bomberman.dropBomb();
                    displayElement(bomberman.getBomb());
                }
            }
        });
        bomberman.render().setFocusTraversable(true);
    }

    @Override
    public void update(Observable o, Object arg) {
        displayElement((Element)o);
    }

    private void displayElement(Element element){
        if(gridPane.getChildren().contains(element.render()))
            gridPane.getChildren().remove(element.render());
        ImageView sprite = element.render();
        sprite.setFitHeight(50);
        sprite.setFitWidth(50);
        gridPane.add(sprite, element.getPosition().x, element.getPosition().y);
    }
}
