package ch.heigvd.bomberman.client.views.room;

import ch.heigvd.bomberman.common.game.Arena;
import ch.heigvd.bomberman.common.game.RandomArena;
import ch.heigvd.bomberman.common.game.SimpleArena;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

/**
 * Created by matthieu.villard on 10.05.2016.
 */
public class NewViewController
{
    private Arena[] arenas;
    private int selected = 0;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private GridPane gridPane;

    public NewViewController() throws Exception {
        arenas = new Arena[]{new SimpleArena(), new RandomArena()};
    }

    @FXML
    private void initialize() throws Exception {
        refreshArena();
    }

    @FXML
    private void next()
    {
        if(selected < arenas.length - 1) {
            selected++;
            refreshArena();
        }
    }

    @FXML
    private void previous()
    {
        if(selected > 0) {
            selected--;
            refreshArena();
        }
    }

    private void refreshArena(){
        gridPane.getChildren().clear();
        gridPane.getColumnConstraints().clear();
        gridPane.getRowConstraints().clear();

        for (int i = 0; i < arenas[selected].getWidth(); i++) {
            ColumnConstraints column = new ColumnConstraints(15);
            column.setHgrow(Priority.SOMETIMES);
            gridPane.getColumnConstraints().add(column);
        }
        for (int i = 0; i < arenas[selected].getHeight(); i++) {
            RowConstraints rowConstraints = new RowConstraints(15);
            rowConstraints.setVgrow(Priority.SOMETIMES);
            gridPane.getRowConstraints().add(rowConstraints);
        }

        arenas[selected].getElements().stream().forEach(element -> {
            ImageView sprite = element.render();
            sprite.setFitHeight(15);
            sprite.setFitWidth(15);
            gridPane.add(sprite, element.getPosition().x, element.getPosition().y);
        });
    }
}
