package ch.heigvd.bomberman.client.views.game;

import ch.heigvd.bomberman.client.Client;
import ch.heigvd.bomberman.client.ResponseManager;
import ch.heigvd.bomberman.common.game.Room;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by matthieu.villard on 28.05.2016.
 */
public class ReadyController implements Observer {

    private ResponseManager rm;
    private Room room;
    private Client client;

    @FXML
    private Label lblRoom, number;

    @FXML
    private AnchorPane mainPane;

    @FXML
    private void initialize()
    {
        rm = ResponseManager.getInstance();
    }

    public void setClient(Client client){
        this.client = client;
    }

    public void loadRoom(Room room){
        this.room = room;
        lblRoom.setText("Room \"" + room.getName() + "\"");
        number.setText(room.getPlayerNumber() + " player" + (room.getPlayerNumber() > 1 ? "s" : ""));
    }

    @FXML
    private void leave(){
        rm.readyRequest(false, null);
        ((Stage)mainPane.getScene().getWindow()).close();
        client.getPrimatyStage().show();
    }

    @FXML
    private void ready(){
        rm.readyRequest(true, bomberman -> {
            if(bomberman != null){
                GameController controller = new GameController(bomberman, client);
                ((Stage)mainPane.getScene().getWindow()).close();
            }
        });
    }

    @Override
    public void update(Observable o, Object arg) {
        List<Room> rooms = (List<Room>)arg;
        if(rooms.contains(room)) {
            loadRoom(room);
        }
        else if(rooms.stream().filter(r -> r.getName().equals(room.getName())).findFirst().isPresent()){
            loadRoom(rooms.stream().filter(r -> r.getName().equals(room.getName())).findFirst().get());
        }
    }
}
