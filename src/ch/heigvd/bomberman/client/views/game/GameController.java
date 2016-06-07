package ch.heigvd.bomberman.client.views.game;

import ch.heigvd.bomberman.client.Client;
import ch.heigvd.bomberman.client.ResponseManager;
import ch.heigvd.bomberman.client.views.render.ArenaRenderer;
import ch.heigvd.bomberman.common.game.Bomberman;
import ch.heigvd.bomberman.common.game.Room;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by matthieu.villard on 29.05.2016.
 */
public class GameController {

    private ResponseManager rm;
    private ArenaRenderer renderer;
    private Client client;

    @FXML
    private AnchorPane mainPane;

    @FXML
    private GridPane mapContainer;

    @FXML
    private GridPane header;

    public void setClient(Client client){
        this.client = client;
    }

    public void loadGame(Bomberman bomberman, Room room){
        renderer = new ArenaRenderer(bomberman.getArena(), bomberman, mapContainer.getWidth(), mapContainer.getHeight());
        mapContainer.getChildren().clear();
        mapContainer.add(renderer.getView(), 0, 0);

        System.out.println(room.getPlayerNumber() / 2 + room.getPlayerNumber() % 2 + 1);
        room.getPlayers().forEach(player ->{
            if((header.getChildren().size() - 1) % 2 == 0){
                header.add(new Label(player), header.getChildren().size() - 1, 0);
            }
            if(header.getChildren().size() < room.getPlayerNumber() / 2 + room.getPlayerNumber() % 2 + 1){
                header.add(new Label(player), header.getChildren().size() - 1, 0);
            }
            else {
                header.add(new Label(player), header.getChildren().size(), 0);
            }
        });
    }

    @FXML
    private void initialize(){

        rm = ResponseManager.getInstance();

        rm.moveRequest(null, bomberman -> {
            renderer.getArena().change(bomberman);
        });

        rm.addElementRequest(element -> {
            renderer.getArena().add(element);
        });

        rm.destroyElementsRequest(element -> {
           renderer.getArena().destroy(element);
        });

        rm.endGameRequest(statistic -> {
            final Stage results = new Stage();
            results.setTitle("Results");

            results.setOnCloseRequest(event -> {
                client.getPrimatyStage().show();
            });

            results.initModality(Modality.APPLICATION_MODAL);

            FXMLLoader loader = new FXMLLoader(Client.class.getResource("views/game/endGame.fxml"));
            try
            {
                Pane pane = loader.load();
                EndGameController controller = loader.getController();
                controller.setClient(client);
                controller.setStatistic(statistic);
                results.setScene(new Scene(pane));
                ((Stage)mainPane.getScene().getWindow()).close();
                results.showAndWait();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        });
    }
}
