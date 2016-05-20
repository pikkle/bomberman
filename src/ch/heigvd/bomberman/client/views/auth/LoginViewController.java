package ch.heigvd.bomberman.client.views.auth;

import ch.heigvd.bomberman.client.Client;
import ch.heigvd.bomberman.client.ResponseManager;
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
    private Client client;
    private ClientMainController mainController;
    private static final int DEFAULT_PORT = 3737;
    private static final String DEFAULT_ADDRESS = "127.0.0.1";
    ResponseManager rm;

    @FXML
    private Pane mainPane;

    @FXML
    private Label serverStatusLabel;

    @FXML
    private TextField userId;

    @FXML
    private PasswordField pwd;

    @FXML private Button login, createAcount;

    @FXML
    private ImageView serverStatusIcon;

    /***********
     * Methodes*
     ***********/

    @FXML
    private void initialize() {


        rm = ResponseManager.getInstance();
        testServer();
    }

    public void testServer(){
        try
        {
            rm.connect(DEFAULT_ADDRESS, DEFAULT_PORT);
        } catch (IOException e)
        {
            serverStatusLabel.setText("Offline");
            serverStatusIcon.setImage(new Image(Client.class.getResourceAsStream("img/ko_sign.png")));
            hiddeAll();
        }
    }

    public void setClient(Client client) {

        this.client = client;
    }

    private void hiddeAll() {
        userId.setDisable(true);
        pwd.setDisable(true);
        login.setDisable(true);
        createAcount.setDisable(true);
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
            mainController.getRm().loginRequest(userId.getText(), hashPasswd, message -> {
                if (message.state()) {
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

    @FXML
    private void bypass()
    {
        FXMLLoader loader = new FXMLLoader(Client.class.getResource("views/ClientMain.fxml"));
        try
        {
            Pane pane = loader.load();
            client.changeScene(pane);
        } catch (IOException e)
        {
            e.printStackTrace();
        }





    }
}