package ch.heigvd.bomberman.client.views;

import ch.heigvd.bomberman.common.game.Arena;
import ch.heigvd.bomberman.common.game.Bomberman;
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

    public ArenaController(Arena arena, Bomberman bomberman) {
        this.arena = arena;
        this.bomberman = bomberman;
        arena.addPlayer(bomberman);
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
            ImageView sprite = element.render();
            sprite.setFitHeight(50);
            sprite.setFitWidth(50);
            gridPane.add(sprite, element.getPosition().x, element.getPosition().y);
            element.addObserver(this);
        });

        bomberman.render().setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent key) {
                Point position = (Point)bomberman.getPosition().clone();
                if (key.getCode().equals(KeyCode.RIGHT) && bomberman.getPosition().x < arena.getWidth()) {
                    position.x = position.x + 1;
                    key.consume();
                }
                else if(key.getCode().equals(KeyCode.LEFT) && bomberman.getPosition().x > 0){
                    position.x = position.x - 1;
                    key.consume();
                }
                else if(key.getCode().equals(KeyCode.DOWN) && bomberman.getPosition().y < arena.getHeight()){
                    position.y = position.y + 1;
                    key.consume();
                }
                else if(key.getCode().equals(KeyCode.UP) && bomberman.getPosition().y > 0){
                    position.y = position.y - 1;
                    key.consume();
                }
                if(!position.equals(bomberman.getPosition()) && arena.isEmpty(position))
                    bomberman.move(position);
            }
        });
        bomberman.render().setFocusTraversable(true);
    }

    @Override
    public void update(Observable o, Object arg) {
        Element element = (Element)o;
        gridPane.getChildren().remove(element.render());
        gridPane.add(element.render(),element.getPosition().x , element.getPosition().y);
    }
}
