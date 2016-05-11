package ch.heigvd.bomberman.client.views.auth;

import ch.heigvd.bomberman.client.Client;
import ch.heigvd.bomberman.client.views.ClientMainController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Created by julien on 08.05.16.
 */
public class LoginViewController {
    ClientMainController mainController;

    @FXML
    private Pane mainPane;

    @FXML
    private Label serverStatusLabel;

    @FXML
    private TextField userId;

    @FXML
    private PasswordField mdp;

    @FXML
    private void initialize() {

    }

    public void setMainController(ClientMainController mainController)
    {
        this.mainController = mainController;
        serverStatusLabel.setText(mainController.getRm().isConnected() ? "Connection accepted" : "Connection refused");
    }

    @FXML
    private void login()
    {
        String hashPasswd = mdp.getText();

    }

    @FXML
    private void closeWindow()
    {
        Platform.exit();
        ( (Stage)mainPane.getScene().getWindow() ).close();
    }

    @FXML
    private void newUser() throws Exception
    {
        Stage stage = new Stage();
        Pane pane;
        NewViewController controller;
        FXMLLoader loader = new FXMLLoader(Client.class.getResource("views/auth/NewView.fxml"));

        stage.setTitle("New User");
        pane = loader.load();
        controller = loader.getController();
        controller.setMainController(mainController);

        stage.initModality(Modality.APPLICATION_MODAL);

        stage.setScene(new Scene(pane));
        stage.showAndWait();
    }
}