package ch.heigvd.bomberman.client.views.auth;

import ch.heigvd.bomberman.client.Client;
import ch.heigvd.bomberman.client.ResponseManager;
import ch.heigvd.bomberman.client.views.ClientMainController;
import ch.heigvd.bomberman.common.communication.Message;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.UncheckedIOException;

/**
 * Created by julien on 08.05.16.
 */
public class LoginViewController {
    private static Log logger = LogFactory.getLog(LoginViewController.class);
    private Client client;
    private static final int DEFAULT_PORT = 3737;
    private static String DEFAULT_ADDRESS = "127.0.0.1";
    private ResponseManager rm;

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

    @FXML
    private TextField ip;

    /***********
     * Methodes*
     ***********/

    public LoginViewController(){
        client = Client.getInstance();
    }

    public static String getAddress(){
        return DEFAULT_ADDRESS;
    }

    @FXML
    private void initialize() {
        ip.setText(DEFAULT_ADDRESS);
        rm = ResponseManager.getInstance();
        testServer();
    }

    @FXML
    private void testServer(){
        logger.info("Client connecting...");
        DEFAULT_ADDRESS = ip.getText();
        try
        {
            rm.connect(DEFAULT_ADDRESS, DEFAULT_PORT);
            serverStatusLabel.setText("Online");
            serverStatusIcon.setImage(new Image(Client.class.getResourceAsStream("img/ok_sign.png")));
            enableAll();
            mainPane.setOnKeyPressed(event -> {
                if(event.getCode() == KeyCode.ENTER){
                    login();
                }
            });
            logger.info("Client connected");
        } catch (Exception e) {
            logger.warn(e.getMessage());
            serverStatusLabel.setText("Offline");
            serverStatusIcon.setImage(new Image(Client.class.getResourceAsStream("img/ko_sign.png")));
            disableAll();
        }
    }


    private void disableAll() {
        userId.setDisable(true);
        pwd.setDisable(true);
        login.setDisable(true);
        createAcount.setDisable(true);
    }

    private void enableAll() {
        userId.setDisable(false);
        pwd.setDisable(false);
        login.setDisable(false);
        createAcount.setDisable(false);
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
            disableAll();
            rm.loginRequest(userId.getText(), hashPasswd, message -> {
                if (message.state()) {
                    try
                    {
                        loginSuccess();
                    } catch (IOException e)
                    {
                        throw new UncheckedIOException(e);
                    }
                } else {
                    loginFailure(message);
                }
            });
        }
    }

    private void loginSuccess() throws IOException{
        FXMLLoader loader = new FXMLLoader(Client.class.getResource("views/ClientMain.fxml"));
        Pane pane = loader.load();
        client.changeScene(pane);
        ClientMainController controller = loader.getController();
    }

    private void loginFailure(Message message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message.getMessage());
        alert.showAndWait();
        enableAll();
    }

    @FXML
    private void closeApp()
    {
        Platform.exit();
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

        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);

        stage.setScene(new Scene(pane));
        stage.showAndWait();
    }
}