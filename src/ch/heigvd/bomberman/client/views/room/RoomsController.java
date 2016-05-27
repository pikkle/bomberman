package ch.heigvd.bomberman.client.views.room;

import ch.heigvd.bomberman.client.ResponseManager;
import ch.heigvd.bomberman.client.views.render.ArenaRenderer;
import ch.heigvd.bomberman.common.communication.Message;
import ch.heigvd.bomberman.common.game.Room;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

/**
 * Created by matthieu.villard on 09.05.2016.
 */
public class RoomsController
{
    private ResponseManager rm;
    private ObservableList<Room> rooms = FXCollections.observableArrayList();

    @FXML
    private TableView roomsTableView;

    @FXML
    private void initialize() throws IOException {
        rm = ResponseManager.getInstance();
        roomsTableView.setItems(rooms);

      /*  rm.lookForRooms(r ->{
            rooms.clear();
            rooms.addAll(r);
        });*/

    }

    @FXML
    public void showRooms(){
        rm.roomsRequest();
    }

    @FXML
    public void join(){
        Room room = (Room) roomsTableView.getSelectionModel().getSelectedItem();
        rm.joinRoomRequest(room, message -> {
            if (message.state()) {
                joinSucces(message);
            } else {
                joinFailure(message);
            }
        });
    }

    private void joinSucces(Message message){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText(message.getMessage());
        ButtonType buttonTypeOne = new ButtonType("Yes");
        ButtonType buttonTypeTwo = new ButtonType("No");
        alert.getDialogPane().getButtonTypes().addAll(ButtonType.YES, ButtonType.APPLY.NO);
        Optional<ButtonType> result = alert.showAndWait();

        rm.readyRequest(result.get() == ButtonType.YES, bomberman -> {
            if(bomberman != null){
                Stage stage = new Stage();
                stage.setTitle("Bomberman");

                stage.initModality(Modality.APPLICATION_MODAL);

                try {
                    Scene scene = new Scene(new ArenaRenderer(bomberman.getArena(), bomberman, 750, 750).getView());
                    stage.setScene(scene);
                    bomberman.getArena().start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                stage.showAndWait();
            }
        });
    }

    private void joinFailure(Message message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message.getMessage());
        alert.showAndWait();
    }

}
