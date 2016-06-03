
package ch.heigvd.bomberman.client.views.tabs.controllers;

import ch.heigvd.bomberman.client.Client;
import ch.heigvd.bomberman.client.ResponseManager;
import ch.heigvd.bomberman.client.views.game.ReadyController;
import ch.heigvd.bomberman.client.views.room.PasswordController;
import ch.heigvd.bomberman.common.game.Room;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    private Room room;

    @FXML
    private TableView<Room> roomsTableView;

    @FXML
    private TableColumn<Room, ImageView> isPrivate;

    @FXML
    private TableColumn<Room, ImageView> inRoom;


    @FXML
    private void initialize() throws IOException {
        //roomsTableView.setColumnResizePolicy(param -> false);

        rm = ResponseManager.getInstance();
        roomsTableView.setItems(rooms);
        showRooms();

        inRoom.setCellValueFactory(param -> {
            if (param.getValue().isInRoom())
                return new SimpleObjectProperty<ImageView>(new ImageView(new Image(Client.class.getResourceAsStream("img/ok_sign.png"))));
            else
                return new SimpleObjectProperty<ImageView>(new ImageView(new Image(Client.class.getResourceAsStream("img/ko_sign.png"))));
        });

        isPrivate.setCellValueFactory(param -> {
            if (param.getValue().isPrivate())
                return new SimpleObjectProperty<ImageView>(new ImageView(new Image(Client.class.getResourceAsStream("img/lock.png"))));
            else
                return new SimpleObjectProperty<ImageView>(new ImageView(new Image(Client.class.getResourceAsStream("img/unlock.png"))));

        });
    }

    public void setClient(Client client){
        this.client = client;
    }

    public Room getRoom(){
        return room;
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
        room = roomsTableView.getSelectionModel().getSelectedItem();
        if(room.isPrivate()){
            Stage stage = new Stage();
            stage.setTitle("Join room");

            stage.initModality(Modality.APPLICATION_MODAL);

            FXMLLoader loader = new FXMLLoader(Client.class.getResource("views/room/PasswordView.fxml"));
            try {
                Pane pane = loader.load();
                PasswordController controller = loader.getController();
                controller.setClient(client);
                controller.setRoomsController(this);
                stage.setScene(new Scene(pane));

                stage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            rm.joinRoomRequest(room, r -> {
                Stage stage = new Stage();
                stage.setTitle("Bomberman");

                stage.setOnCloseRequest(event -> {
                    rm.readyRequest(false, null);
                    client.getPrimatyStage().show();
                });

                stage.initModality(Modality.APPLICATION_MODAL);

                FXMLLoader loader = new FXMLLoader(Client.class.getResource("views/game/ready.fxml"));
                try {
                    Pane pane = loader.load();
                    ReadyController controller = loader.getController();
                    controller.setClient(client);
                    controller.loadRoom(r);
                    addObserver(controller);
                    stage.setScene(new Scene(pane));

                    client.getPrimatyStage().hide();
                    stage.showAndWait();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
