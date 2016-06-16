package ch.heigvd.bomberman.client.views.tabs.controllers;

import ch.heigvd.bomberman.client.ResponseManager;
import ch.heigvd.bomberman.common.communication.Message;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.Observable;

/**
 * Created by matthieu.villard on 09.05.2016.
 */

public class ProfileController extends Observable
{
    private ResponseManager rm;

    @FXML
    private VBox pwdBox;

    @FXML
    private Label lblPseudo, lblPwd;

    @FXML
    private TextField userId;

    @FXML
    private PasswordField pwd, pwdc;


    @FXML
    public void initialize() {
        rm = ResponseManager.getInstance();

        rm.playerRequest(player -> {
            lblPseudo.setText(player.getPseudo());
        });

        lblPseudo.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                lblPseudo.setVisible(false);
                userId.setText(lblPseudo.getText());
                userId.setVisible(true);
                userId.requestFocus();
            }
        });

        userId.focusedProperty().addListener((obs, oldValue, newValue) -> {
            if(!newValue){
                userId.setVisible(false);
                lblPseudo.setVisible(true);
            }
        });

        lblPwd.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                lblPwd.setVisible(false);
                pwd.setText("");
                pwdc.setText("");
                pwdBox.setVisible(true);
                pwd.requestFocus();
            }
        });

        pwd.focusedProperty().addListener((obs, oldValue, newValue) -> {
            if(!newValue && !pwdc.isFocused()){
                pwdBox.setVisible(false);
                lblPwd.setVisible(true);
            }
        });

        pwdc.focusedProperty().addListener((obs, oldValue, newValue) -> {
            if(!newValue && !pwd.isFocused()){
                pwdBox.setVisible(false);
                lblPwd.setVisible(true);
            }
        });

    }

    @FXML
    private void changeUsername(){
        if(!rm.isConnected()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("You are not connected");
            alert.showAndWait();
            lblPseudo.setVisible(false);
            userId.setVisible(true);
            userId.requestFocus();
        }
        else if (userId.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("The user name is empty");
            alert.showAndWait();
            lblPseudo.setVisible(false);
            userId.setVisible(true);
            userId.requestFocus();
        }
        else {
            rm.modifyAccountRequest(userId.getText(), null, message -> {
                if(message.state()){
                    userId.setVisible(false);
                    lblPseudo.setVisible(true);
                }
                else{
                    failure(message);
                    lblPseudo.setVisible(false);
                    userId.setVisible(true);
                    userId.requestFocus();
                }
            });
        }
    }

    @FXML
    private void changePassword(){
        if(!rm.isConnected()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("You are not connected");
            alert.showAndWait();
            lblPwd.setVisible(false);
            pwdBox.setVisible(true);
            pwd.requestFocus();
        }
        else if (pwd.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("The password name is empty");
            alert.showAndWait();
            lblPwd.setVisible(false);
            pwdBox.setVisible(true);
            pwd.requestFocus();
        }
        else if (!pwd.getText().equals(pwdc.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("The two password are not the same");
            alert.showAndWait();
            lblPwd.setVisible(false);
            pwdBox.setVisible(true);
            pwd.requestFocus();
        }
        else {
            rm.modifyAccountRequest(null, pwd.getText(), message -> {
                if(message.state()){
                    pwdBox.setVisible(false);
                    lblPwd.setVisible(true);
                }
                else{
                    failure(message);
                    lblPwd.setVisible(false);
                    pwdBox.setVisible(true);
                    pwd.requestFocus();
                }
            });
        }
    }

    private void failure(Message message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message.getMessage());
        alert.showAndWait();
    }

}
