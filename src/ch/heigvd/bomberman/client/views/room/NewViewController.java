package ch.heigvd.bomberman.client.views.room;

import ch.heigvd.bomberman.client.views.ClientMainController;
import ch.heigvd.bomberman.client.views.render.ArenaRenderer;
import ch.heigvd.bomberman.common.game.Arena.Arena;
import ch.heigvd.bomberman.common.game.Arena.RandomArena;
import ch.heigvd.bomberman.common.game.Arena.SimpleArena;
import ch.heigvd.bomberman.common.game.Room;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
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
    private StackPane arenaContainer;

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
        arenaContainer.getChildren().clear();
        arenaContainer.getChildren().add( new ArenaRenderer(arenas[selected], 225, 225).render());
        lblRoom.setText(selected + 1 + " / " + arenas.length);
    }
}