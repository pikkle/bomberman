package ch.heigvd.bomberman.client.views.room;

import ch.heigvd.bomberman.client.views.ClientMainController;
import ch.heigvd.bomberman.common.game.Arena;
import ch.heigvd.bomberman.common.game.RandomArena;
import ch.heigvd.bomberman.common.game.Room;
import ch.heigvd.bomberman.common.game.SimpleArena;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * Created by matthieu.villard on 10.05.2016.
 */
public class NewViewController
{
    private Arena[] arenas;
    private int selected = 0;
    private ClientMainController mainController;

    @FXML
    private AnchorPane mainPane;

    @FXML
    private GridPane gridPane;

    @FXML
    private Label lblRoom;

    @FXML
    private TextField roomName;

    public NewViewController() throws Exception {
        arenas = new Arena[]{new SimpleArena(), new RandomArena()};
    }

    public void setMainController(ClientMainController mainController)
    {
        this.mainController = mainController;
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
        }
        else{
            selected = 0;
        }
        refreshArena();
    }

    @FXML
    private void previous()
    {
        if(selected > 0) {
            selected--;
        }
        else{
            selected = arenas.length -1;
        }
        refreshArena();
    }

    @FXML
    private void save(){
        mainController.addRoom(new Room(roomName.getText(), arenas[selected]));
        close();
    }

    @FXML
    private void close(){
        ( (Stage)mainPane.getScene().getWindow() ).close();
    }

    private void refreshArena(){
        gridPane.getChildren().clear();
        gridPane.getColumnConstraints().clear();
        gridPane.getRowConstraints().clear();

        lblRoom.setText(selected + 1 + " / " + arenas.length);

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
            gridPane.add(sprite, (int)element.getPosition().getX(), (int)element.getPosition().getY());
        });
    }
}
