package ch.heigvd.bomberman.client.views.auth;

import ch.heigvd.bomberman.client.views.ClientMainController;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Created by julien on 08.05.16.
 */
public class NewViewController {
    ClientMainController mainController;

    @FXML
    private Pane mainPane;


    public void setMainController(ClientMainController mainController)
    {
        this.mainController = mainController;
    }

    @FXML
    private void cancel()
    {
        ((Stage)mainPane.getScene().getWindow()).close();
    }

}