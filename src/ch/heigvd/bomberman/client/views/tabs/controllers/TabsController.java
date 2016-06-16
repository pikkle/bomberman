package ch.heigvd.bomberman.client.views.tabs.controllers;

import ch.heigvd.bomberman.client.Client;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.io.IOException;

/**
 * Created by julien on 27.05.16.
 */
public class TabsController
{
    private Client client;

    @FXML
    private Tab roomsTab;

    @FXML
    private Tab mapsTab;

    @FXML
    private Tab statisticsTab;

    @FXML
    private Tab profileTab;

    @FXML
    private Tab playersTab;

    @FXML
    private RoomsController roomsController;

    @FXML
    private MapsController mapsController;

    @FXML
    private StatisticsController statisticsController;

    @FXML
    private ProfileController profileController ;

    @FXML
    private PlayersController playersController ;

    @FXML
    private TabPane tabs;

    @FXML
    public void initalize(){
        tabs.getSelectionModel().selectedItemProperty()
                .addListener((obs, oldTab, newTab) -> {
                    if (newTab == mapsTab) {
                        mapsController.initalize();
                    }
                    else if(newTab == statisticsTab){
                        statisticsController.initalize();
                    }
                    else if(newTab == profileTab){
                        profileController.initialize();
                    }
                    else if(newTab == playersTab){
                        try {
                            playersController.initialize();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
