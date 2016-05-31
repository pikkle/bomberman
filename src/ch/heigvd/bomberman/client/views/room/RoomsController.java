
package ch.heigvd.bomberman.client.views.room;

import ch.heigvd.bomberman.client.Client;
import ch.heigvd.bomberman.client.ResponseManager;
import ch.heigvd.bomberman.client.views.game.ReadyController;
import ch.heigvd.bomberman.common.game.Room;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Observable;

/**
 * Created by matthieu.villard on 09.05.2016.
 */
public class RoomsController extends Observable
{
    private ResponseManager rm;
    private ObservableList<Room> rooms = FXCollections.observableArrayList();
    private Client client;

    @FXML
    private TableView roomsTableView;


    @FXML
    private void initialize() throws IOException {
        rm = ResponseManager.getInstance();
        roomsTableView.setItems(rooms);
        showRooms();
    }

    public void setClient(Client client){
        this.client = client;
    }

    @FXML
    public void showRooms(){
        if(rm.isConnected()) {
            rm.roomsRequest(r -> {
                rooms.clear();
                rooms.addAll(r);
                setChanged();
                notifyObservers(r);
            });
        }
    }

    @FXML
    public void join(){
        Room room = (Room) roomsTableView.getSelectionModel().getSelectedItem();
        rm.joinRoomRequest(room, r -> {
            Stage stage = new Stage();
            stage.setTitle("Bomberman");

            stage.setOnCloseRequest(event -> {
                rm.readyRequest(false, null);
                client.getPrimatyStage().show();
            });

            stage.initModality(Modality.APPLICATION_MODAL);

            FXMLLoader loader = new FXMLLoader(Client.class.getResource("views/game/ready.fxml"));
            try
            {
                Pane pane = loader.load();
                ReadyController controller = loader.getController();
                controller.setClient(client);
                controller.loadRoom(r);
                addObserver(controller);
                stage.setScene(new Scene(pane));

                client.getPrimatyStage().hide();
                stage.showAndWait();

            } catch (IOException e)
            {
                e.printStackTrace();
            }
        });
    }
}
