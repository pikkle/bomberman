package ch.heigvd.bomberman.client.views;

import ch.heigvd.bomberman.common.game.Arena;
import ch.heigvd.bomberman.common.game.Bomberman;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;

import java.awt.*;

public class ArenaController
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
        if(arena != null) {
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
            });

            bomberman.render().setOnKeyPressed(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent key) {
                    if (key.getCode().equals(KeyCode.RIGHT)) {
                        bomberman.move(new Point(bomberman.getPosition().x + 1, bomberman.getPosition().y));
                        gridPane.getChildren().remove(bomberman.render());
                        gridPane.add(bomberman.render(), bomberman.getPosition().x , bomberman.getPosition().y);
                        key.consume();
                    }
                }
            });
            bomberman.render().setFocusTraversable(true);
        }
    }


    private void moveRight(){

    }

    private void installEventHandler(final Node keyNode) {
        // handler for enter key press / release events, other keys are
        // handled by the parent (keyboard) node handler
        final EventHandler<KeyEvent> keyEventHandler =
                new EventHandler<KeyEvent>() {
                    public void handle(final KeyEvent keyEvent) {
                        if (keyEvent.getCode() == KeyCode.RIGHT) {
                            moveRight();
                            keyEvent.consume();
                        }
                    }
                };

        keyNode.setOnKeyPressed(keyEventHandler);
        keyNode.setOnKeyReleased(keyEventHandler);
    }
}
