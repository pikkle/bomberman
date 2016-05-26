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

    @FXML private TextField userId, email;
    @FXML private PasswordField pwd, pwdc;

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
        if (userId.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("The user name is empty");
            alert.showAndWait();
        }

        else if (email.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("The email is empty");
            alert.showAndWait();
        }

        else if (pwd.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("The password name is empty");
            alert.showAndWait();
        }

        else if (!pwd.getText().equals(pwdc.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("The two password are not the same");
            alert.showAndWait();
        }

        else {
            mainController.getRm().newAccountRequest(userId.getText(), pwd.getText(), message -> {
                if (message.state()) {
                    creationSucces();
                } else {
                    creationFailure();
                }
            });
        }
    }

    private void creationSucces(){
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setContentText("Your acount has been created");
        alert.showAndWait();
        ((Stage)mainPane.getScene().getWindow()).close();
    }

    private void creationFailure(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText("An error uccured while creating your acount");
        alert.showAndWait();
    }

    @FXML
    private void cancel()
    {
        ((Stage)mainPane.getScene().getWindow()).close();
    }

}