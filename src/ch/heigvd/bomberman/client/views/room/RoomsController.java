package ch.heigvd.bomberman.client.views.room;

import ch.heigvd.bomberman.client.Client;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.io.IOException;

/**
 * Created by matthieu.villard on 09.05.2016.
 */
public class RoomsController
{
    private int id = 0;

    @FXML
    private GridPane gridPane;

    @FXML
    private void initialize() throws IOException {
        for(int i = 0; i < 5; i++)
            addRoom();
    }

    private void addRoom() throws IOException {
        FXMLLoader loader = new FXMLLoader(Client.class.getResource("views/room/RoomView.fxml"));
        //loader.setRoot(this);
        //loader.setController(this);
        AnchorPane children = loader.load();
        gridPane.add(children, id % 3, id / 3);
        id++;
    }
}
