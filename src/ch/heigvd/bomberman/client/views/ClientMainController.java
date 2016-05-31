package ch.heigvd.bomberman.client.views;

import ch.heigvd.bomberman.client.Client;
import ch.heigvd.bomberman.client.ResponseManager;
import ch.heigvd.bomberman.client.views.room.NewViewController;
import ch.heigvd.bomberman.client.views.tabs.controllers.UserTabsController;
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
    private UserTabsController userTabsController;

    @FXML
    private TableView<Room> roomsTableView;

    @FXML
    private Pane tabsPane;

    @FXML
    private Pane mainPane;

    /***********
     * Methodes*
     ***********/

    public ResponseManager getRm(){
        return rm;
    }

    public void setMainApp(Client client)
    {
        this.client = client;
        userTabsController.setClient(client);
    }

    @FXML
    private void initialize() {
        rm = ResponseManager.getInstance();

        try
        {
            FXMLLoader loader = new FXMLLoader(Client.class.getResource("views/tabs/views/UserTabsView.fxml"));
            TabPane pane = loader.load();
            userTabsController = loader.getController();
            tabsPane.getChildren().add(pane);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @FXML
    private void closeApp () throws Exception
    {
        Platform.exit();
    }

    @FXML
    private void arena()
    {

    }

    @FXML
    private void mapEditor() throws Exception
    {
        Stage stage = new Stage();
        AnchorPane pane;
        FXMLLoader loader = new FXMLLoader(Client.class.getResource("views/tabs/mapEditor/MapEditor.fxml"));

        stage.setTitle("MapEditor");
        pane = loader.load();

        stage.initModality(Modality.APPLICATION_MODAL);

        stage.setScene(new Scene(pane));
        stage.showAndWait();
    }

    @FXML
    private void room() throws Exception
    {
        Stage stage = new Stage();
        AnchorPane pane;
        NewViewController controller;
        FXMLLoader loader = new FXMLLoader(Client.class.getResource("views/room/NewView.fxml"));

        stage.setTitle("Salle");
        pane = loader.load();
        controller = loader.getController();

        stage.initModality(Modality.APPLICATION_MODAL);

        stage.setScene(new Scene(pane));
        stage.showAndWait();
    }
}