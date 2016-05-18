package ch.heigvd.bomberman.client.views;

import ch.heigvd.bomberman.common.game.Arena.Arena;
import ch.heigvd.bomberman.common.game.Bomberman;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;

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

    }

    @Override
    public void update(Observable o, Object arg) {
        System.out.println("knj");

    }
}