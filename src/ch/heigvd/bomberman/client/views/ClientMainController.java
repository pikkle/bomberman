package ch.heigvd.bomberman.client.views;

import ch.heigvd.bomberman.client.Client;
import ch.heigvd.bomberman.client.views.auth.LoginViewController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ClientMainController {
    private Client client;

    public void setMainApp(Client client)
    {
        this.client = client;
    }

    @FXML
    private void initialize() {

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

        stage.setOnCloseRequest(event -> {
            Platform.exit();
            stage.close();
        });

        stage.initModality(Modality.APPLICATION_MODAL);

        stage.setScene(new Scene(pane));
        stage.showAndWait();
    }
}
