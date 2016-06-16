package ch.heigvd.bomberman.client.views.game;

import ch.heigvd.bomberman.client.Client;
import ch.heigvd.bomberman.common.game.Statistic;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * Created by matthieu.villard on 28.05.2016.
 */
public class EndGameController {

    private Client client;

    @FXML
    private Label lblTitle, lblRank;

    @FXML
    private AnchorPane mainPane;

    public EndGameController(){
        client = Client.getInstance();
    }

    public void setStatistic(Statistic statistic){
        lblTitle.setText(statistic.getRank() == 1 ? "You win !" : "You just loose the game !");
        lblRank.setText(String.valueOf(statistic.getRank()));
    }

    @FXML
    private void leave(){
        ((Stage)mainPane.getScene().getWindow()).close();
        client.getPrimatyStage().show();
    }
}
