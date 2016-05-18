package ch.heigvd.bomberman.client.views;

import ch.heigvd.bomberman.client.Client;
import ch.heigvd.bomberman.client.ResponseManager;
import ch.heigvd.bomberman.client.views.auth.LoginViewController;
import ch.heigvd.bomberman.client.views.render.ArenaRenderer;
import ch.heigvd.bomberman.client.views.room.NewViewController;
import ch.heigvd.bomberman.common.game.Arena.Arena;
import ch.heigvd.bomberman.common.game.Arena.SimpleArena;
import ch.heigvd.bomberman.common.game.Room;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
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
    private ObservableList<Room> rooms = FXCollections.observableArrayList();

    @FXML
    private TableView<Room> roomsTableView;

    @FXML
    private Pane tabsPane;

    /***********
     * Methodes*
     ***********/

    public ResponseManager getRm(){
        return rm;
    }

    public void setMainApp(Client client)
    {
        this.client = client;
    }

    public void addRoom(Room room){
        rooms.add(room);
    }

    @FXML
    private void initialize() {

        rm = new ResponseManager(DEFAULT_ADDRESS, DEFAULT_PORT);

        try {
            loginWindow();
        } catch (Exception e) {
            e.printStackTrace();
        }

        FXMLLoader loader = new FXMLLoader(Client.class.getResource("views/tabs/UserTabsView.fxml"));

        try
        {
            tabsPane.getChildren().add(loader.load());
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @FXML
    private void closeApp()
    {
        Platform.exit();
    }

    @FXML
    private void arena()
    {
        Stage stage = new Stage();
        stage.setTitle("Bomberman");

        stage.setOnCloseRequest(event -> {
            Platform.exit();
            stage.close();
        });

        stage.initModality(Modality.APPLICATION_MODAL);

        try {
            Arena arena = new SimpleArena();
            arena.putBomberman();
            stage.setScene(new Scene(new ArenaRenderer(arena, 750, 750).render()));
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        controller.setMainController(this);

        stage.initModality(Modality.APPLICATION_MODAL);

        stage.setScene(new Scene(pane));
        stage.showAndWait();
    }

    private void loginWindow() throws Exception
    {
        Stage stage = new Stage();
        Pane pane;
        LoginViewController controller;
        FXMLLoader loader = new FXMLLoader(Client.class.getResource("views/auth/LoginView.fxml"));

        stage.setTitle("Login");
        pane = loader.load();
        controller = loader.getController();
        controller.setMainController(this);

        /*stage.setOnCloseRequest(event -> {
            Platform.exit();
            stage.close();
        });*/

        stage.initModality(Modality.APPLICATION_MODAL);

        stage.setScene(new Scene(pane));
        stage.showAndWait();
    }
}