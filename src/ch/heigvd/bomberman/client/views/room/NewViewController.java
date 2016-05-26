package ch.heigvd.bomberman.client.views.room;

import ch.heigvd.bomberman.client.views.ClientMainController;
import ch.heigvd.bomberman.client.views.render.ArenaRenderer;
import ch.heigvd.bomberman.common.game.Arena.Arena;
import ch.heigvd.bomberman.common.game.Room;
import ch.heigvd.bomberman.server.database.DBManager;
import ch.heigvd.bomberman.server.database.arena.ArenaORM;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.List;

/**
 * Created by matthieu.villard on 10.05.2016.
 */
public class NewViewController
{
    private List<Arena> arenas;
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

    @FXML
    private Spinner<Integer> minPlayer;

    @FXML
    private CheckBox isPrivate;

    @FXML
    private PasswordField password;

    @FXML
    private Label lblPassword;

    public NewViewController() throws Exception {
        ArenaORM orm = DBManager.getInstance().getOrm(ArenaORM.class);
        if(orm != null) {
            arenas = orm.findAll();
        }
    }

    public void setMainController(ClientMainController mainController)
    {
        this.mainController = mainController;
    }

    @FXML
    private void initialize() throws Exception {
        minPlayer.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(2, 4));
        isPrivate.selectedProperty().addListener((obs, oldValue, newValue) -> {
            password.setDisable(!newValue);
            lblPassword.setDisable(!newValue);
        });
        refreshArena();
    }

    @FXML
    private void next()
    {
        if(selected < arenas.size() - 1) {
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
            selected = arenas.size() -1;
        }
        refreshArena();
    }

    @FXML
    private void save(){
        Room room;
        if(isPrivate.isSelected())
            room = new Room(roomName.getText(), arenas.get(selected), minPlayer.getValue(), password.getText());
        else
            room = new Room(roomName.getText(), arenas.get(selected), minPlayer.getValue());

        mainController.addRoom(room);
        close();
    }

    @FXML
    private void close(){
        ( (Stage)mainPane.getScene().getWindow() ).close();
    }

    private void refreshArena(){
        arenaContainer.getChildren().clear();
        arenaContainer.getChildren().add( new ArenaRenderer(arenas.get(selected), 225, 225).getView());
        lblRoom.setText(selected + 1 + " / " + arenas.size());
    }
}