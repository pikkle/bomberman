package ch.heigvd.bomberman.client.views.game;

import ch.heigvd.bomberman.client.Client;
import ch.heigvd.bomberman.client.ResponseManager;
import ch.heigvd.bomberman.client.views.render.ArenaRenderer;
import ch.heigvd.bomberman.common.game.Bomberman;
import ch.heigvd.bomberman.common.game.Direction;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
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
    private AnchorPane mainPane;
    private Client client;

    public GameController(Bomberman bomberman, Client client){
        this.client = client;
        renderer = new ArenaRenderer(bomberman.arena(), 750, 750);
        renderer.getGrid().setOnKeyPressed(key -> {
            switch (key.getCode()) {
                case RIGHT:
                    rm.moveRequest(Direction.RIGHT, null);
                    break;
                case LEFT:
                    rm.moveRequest(Direction.LEFT, null);
                    break;
                case DOWN:
                    rm.moveRequest(Direction.DOWN, null);
                    break;
                case UP:
                    rm.moveRequest(Direction.UP, null);
                    break;
                case SPACE:
                    rm.dropBombRequest();
                    break;
                default:
                    return;
            }
            key.consume();
        });
        renderer.getGrid().setFocusTraversable(true);
        mainPane = renderer.getView();
        rm = ResponseManager.getInstance();
        initialize();
    }

    private void initialize(){
        Stage stage = new Stage();
        stage.setTitle("Bomberman");

        stage.setOnCloseRequest(event -> {
            rm.readyRequest(false, null);
            event.consume();
        });

        stage.initModality(Modality.APPLICATION_MODAL);

        Scene scene = new Scene(mainPane);
        stage.setScene(scene);

        rm.moveRequest(null, bomberman -> {
            renderer.getArena().notify(bomberman);
        });

        rm.addElementRequest(element -> renderer.getArena().add(element));

        rm.destroyElementsRequest(element -> renderer.getArena().remove(element));

        rm.endGameRequest(statistic -> {
            final Stage results = new Stage();
            results.setTitle("Results");

            results.setOnCloseRequest(event -> client.getPrimatyStage().show());

            results.initModality(Modality.APPLICATION_MODAL);

            FXMLLoader loader = new FXMLLoader(Client.class.getResource("views/game/endGame.fxml"));
            try
            {
                Pane pane = loader.load();
                EndGameController controller = loader.getController();
                controller.setClient(client);
                controller.setStatistic(statistic);
                results.setScene(new Scene(pane));
                stage.close();
                results.showAndWait();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        });

        stage.show();
    }

}
