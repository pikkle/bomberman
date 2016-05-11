package ch.heigvd.bomberman.client.views;

import ch.heigvd.bomberman.client.Client;
import ch.heigvd.bomberman.client.views.auth.LoginViewController;
import ch.heigvd.bomberman.client.views.room.NewViewController;
import ch.heigvd.bomberman.common.game.Room;
import ch.heigvd.bomberman.common.game.SimpleArena;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ClientMainController {
    private Client client;
    private ObservableList<Room> rooms;

    @FXML
    private TableView<Room> roomsTableView;

    public void setMainApp(Client client)
    {
        this.client = client;
    }

    public void addRoom(Room room){
        rooms.add(room);
    }

    @FXML
    private void initialize() {
        rooms = roomsTableView.getItems();
        try {
            login();
        } catch (Exception e) {
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
        try {
            ArenaController controller = new ArenaController(new SimpleArena());
            FXMLLoader loader = new FXMLLoader(Client.class.getResource("views/arena.fxml"));

            stage.setTitle("Bomberman");
            loader.setController(controller);
            GridPane pane = loader.load();

            stage.setOnCloseRequest(event -> {
                Platform.exit();
                stage.close();
            });

            stage.initModality(Modality.APPLICATION_MODAL);

            stage.setScene(new Scene(pane));
            stage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
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

    private void login() throws Exception
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