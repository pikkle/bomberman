package ch.heigvd.bomberman.client.views.room;

import ch.heigvd.bomberman.client.ResponseManager;
import ch.heigvd.bomberman.client.views.render.ArenaRenderer;
import ch.heigvd.bomberman.common.communication.Message;
import ch.heigvd.bomberman.common.game.Arena.Arena;
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
    private ResponseManager rm;

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

    @FXML
    private void initialize() throws Exception {
        rm = ResponseManager.getInstance();
        minPlayer.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(2, 4));
        isPrivate.selectedProperty().addListener((obs, oldValue, newValue) -> {
            password.setDisable(!newValue);
            lblPassword.setDisable(!newValue);
        });

        rm.arenasRequest(arenas -> {
            this.arenas = arenas;
            refreshArena();
        });
    }

    @FXML
    private void next()
    {
        if(arenas != null && selected < arenas.size() - 1) {
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
        else if(arenas != null){
            selected = arenas.size() -1;
        }
        refreshArena();
    }

    @FXML
    private void create(){
        if (roomName.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("The room name is empty");
            alert.showAndWait();
        }
        else if (minPlayer.getValue() < 2 || minPlayer.getValue() > 4){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("The min player value must be a number between 2 and 4");
            alert.showAndWait();
        }
        else if (isPrivate.isSelected() && password.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("A private room must have a password");
            alert.showAndWait();
        }
        else if(arenas != null && !arenas.isEmpty()) {
            String hashPasswd = password.getText();
            rm.createRoomRequest(roomName.getText(), arenas.get(selected).getId(), minPlayer.getValue(), hashPasswd, message -> {
                if (message.state()) {
                    ((Stage)mainPane.getScene().getWindow()).close();
                } else {
                    createFailure(message);
                }
            });
        }
    }

    private void createFailure(Message message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message.getMessage());
        alert.showAndWait();
    }

    @FXML
    private void close(){
        ( (Stage)mainPane.getScene().getWindow() ).close();
    }

    private void refreshArena(){
        if(arenas == null || arenas.isEmpty())
            return;
        arenaContainer.getChildren().clear();
        arenaContainer.getChildren().add( new ArenaRenderer(arenas.get(selected), 225, 225).getView());
        lblRoom.setText(selected + 1 + " / " + arenas.size());
    }
}