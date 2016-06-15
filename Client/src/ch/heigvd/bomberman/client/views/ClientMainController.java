package ch.heigvd.bomberman.client.views;

import ch.heigvd.bomberman.client.Client;
import ch.heigvd.bomberman.client.ResponseManager;
import ch.heigvd.bomberman.client.views.tabs.controllers.TabsController;
import ch.heigvd.bomberman.common.game.Room;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class ClientMainController {

    private static final int DEFAULT_PORT = 3737;
    private static final String DEFAULT_ADDRESS = "127.0.0.1";

    private ResponseManager rm;
    private Client client;
    private TabsController userTabsController;

    @FXML
    private TableView<Room> roomsTableView;

    @FXML
    private Pane tabsPane;

    @FXML
    private Pane mainPane;

    /***********
     * Methodes*
     ***********/

    public ClientMainController(){
        client = Client.getInstance();
    }

    @FXML
    private void initialize() {
        rm = ResponseManager.getInstance();
        rm.playerRequest(player -> {
            if (player.isAdmin())
            {
                try
                {
                    FXMLLoader loader = new FXMLLoader(Client.class.getResource("views/tabs/views/AdminTabsView.fxml"));
                    TabPane pane = loader.load();
                    userTabsController = loader.getController();
                    userTabsController.initalize();
                    tabsPane.getChildren().add(pane);
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            } else {
                try
                {
                    FXMLLoader loader = new FXMLLoader(Client.class.getResource("views/tabs/views/UserTabsView.fxml"));
                    TabPane pane = loader.load();
                    userTabsController = loader.getController();
                    userTabsController.initalize();
                    tabsPane.getChildren().add(pane);
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        });
    }


    public ResponseManager getRm(){
        return rm;
    }

    @FXML
    private void about() {
        Stage stage = new Stage();
        AnchorPane pane;

        try {
            FXMLLoader loader = new FXMLLoader(Client.class.getResource("views/About.fxml"));

            stage.setTitle("Bomberman - About");
            pane = loader.load();

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);

            stage.setScene(new Scene(pane));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void closeApp () throws Exception
    {
        Platform.exit();
    }
}