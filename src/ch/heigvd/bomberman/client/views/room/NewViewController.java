package ch.heigvd.bomberman.client.views.room;

import ch.heigvd.bomberman.client.views.ClientMainController;
import ch.heigvd.bomberman.client.views.render.ArenaRenderer;
import ch.heigvd.bomberman.common.communication.Message;
import ch.heigvd.bomberman.common.game.Arena.Arena;
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
        else {
            String hashPasswd = password.getText();
            mainController.getRm().createRoomRequest(roomName.getText(), arenas.get(selected).getId(), minPlayer.getValue(), hashPasswd, message -> {
                System.out.println("sdd");
                if (message.state()) {
                    createSucces(message);
                } else {
                    createFailure(message);
                }
            });
        }
    }

    private void createSucces(Message message){
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setContentText(message.getMessage());
        alert.getDialogPane().getButtonTypes().add(ButtonType.OK);
        alert.showAndWait();
        ((Stage)mainPane.getScene().getWindow()).close();
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
        arenaContainer.getChildren().clear();
        arenaContainer.getChildren().add( new ArenaRenderer(arenas.get(selected), 225, 225).getView());
        lblRoom.setText(selected + 1 + " / " + arenas.size());
    }
}