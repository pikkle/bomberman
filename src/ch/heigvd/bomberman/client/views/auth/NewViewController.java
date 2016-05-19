package ch.heigvd.bomberman.client.views.auth;

import ch.heigvd.bomberman.client.Client;
import ch.heigvd.bomberman.client.views.ClientMainController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by julien on 08.05.16.
 */
public class NewViewController {

    ClientMainController mainController;

    @FXML
    private Pane mainPane;

    @FXML private TextField userId;
    @FXML private PasswordField pwd;

    @FXML
    private void initialize()
    {
    }


    public void setMainController(ClientMainController mainController)
    {
        this.mainController = mainController;
    }

    @FXML
    private void confirm()
    {
        mainController.getRm().newAccountRequest(userId.getText(), pwd.getText(), aBoolean -> {
            test();

        });
    }

    @FXML
    private void cancel()
    {
        ((Stage)mainPane.getScene().getWindow()).close();
    }

    public void test()
    {
        System.out.println("yolo");
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.showAndWait();
    }
}