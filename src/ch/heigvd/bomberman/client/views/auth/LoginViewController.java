package ch.heigvd.bomberman.client.views.auth;

import ch.heigvd.bomberman.client.Client;
import ch.heigvd.bomberman.client.views.ClientMainController;
import ch.heigvd.bomberman.common.communication.requests.LoginRequest;
import ch.heigvd.bomberman.common.communication.responses.Response;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.util.function.Consumer;

import java.io.IOException;

/**
 * Created by julien on 08.05.16.
 */
public class LoginViewController {
    ClientMainController mainController;
    private static final int DEFAULT_PORT = 3737;
    private static final String DEFAULT_ADDRESS = "127.0.0.1";

    @FXML
    private Pane mainPane;

    @FXML
    private Label serverStatusLabel;

    @FXML
    private TextField userId;

    @FXML
    private PasswordField pwd;

    @FXML
    private ImageView serverStatusIcon;

    /***********
     * Methodes*
     ***********/

    @FXML
    private void initialize() {

        serverStatusLabel.setText("");
        serverStatusIcon.setImage(new Image(Client.class.getResourceAsStream("img/ok_sign.png")));
    }

    public void setMainController(ClientMainController mainController)
    {
        this.mainController = mainController;
    }

    @FXML
    private void login()
    {
        if (userId.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("The user name is empty");
            alert.showAndWait();
        }
        else if (pwd.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("The password name is empty");
            alert.showAndWait();
        }
        else {
            String hashPasswd = pwd.getText();
            mainController.getRm().loginRequest(userId.getText(), hashPasswd, aBoolean -> {
                if (aBoolean) {
                    loginSucces();
                } else {
                    loginFailure();
                }
            });
        }
    }

    private void loginSucces(){
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setContentText("Your have been loged in");
        alert.getDialogPane().getButtonTypes().add(ButtonType.OK);
        alert.showAndWait();
        ((Stage)mainPane.getScene().getWindow()).close();
    }

    private void loginFailure(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText("An error uccured while loging in");
        alert.showAndWait();
    }



    @FXML
    private void closeWindow()
    {
        Platform.exit();
        ( (Stage)mainPane.getScene().getWindow() ).close();
        try
        {
            mainController.getRm().disconnect();

        } catch (IOException e)
        {
            e.printStackTrace();
        }
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