package ch.heigvd.bomberman.client.views.tabs.controllers;

import ch.heigvd.bomberman.client.Client;
import ch.heigvd.bomberman.client.views.room.RoomsController;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

/**
 * Created by julien on 27.05.16.
 */
public class UserTabsController
{
    private Client client;

    @FXML
    private Tab roomsTabs;

    @FXML
    private RoomsController roomsController ;

    @FXML
    private TabPane tabs;

    @FXML
    private void initalize(){
        tabs.getSelectionModel().clearAndSelect(0);
    }

    public void setClient(Client client){
        this.client = client;
        roomsController.setClient(client);
    }
}
