package ch.heigvd.bomberman.client.views.game;

import ch.heigvd.bomberman.client.Client;
import ch.heigvd.bomberman.client.ResponseManager;
import ch.heigvd.bomberman.client.views.render.ArenaRenderer;
import ch.heigvd.bomberman.common.game.Bomberman;
import ch.heigvd.bomberman.common.game.util.Direction;
import ch.heigvd.bomberman.common.game.Room;
import ch.heigvd.bomberman.common.game.bombs.Bomb;
import javafx.application.Platform;
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
import java.time.Duration;
import java.time.Instant;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by matthieu.villard on 29.05.2016.
 */
public class GameController {

    private ResponseManager rm;
    private ArenaRenderer renderer;
    private Timer timer;
    private Instant start;

    @FXML
    private AnchorPane mainPane;

    @FXML
    private GridPane mapContainer;

    @FXML
    private GridPane header;

    @FXML
    private Label duration;

    public GameController() {
        rm = ResponseManager.getInstance();
    }

    public void loadGame(Bomberman bomberman, Room room){
        renderer = new ArenaRenderer(bomberman.arena(), mapContainer.getWidth(), mapContainer.getHeight());
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

        mapContainer.getChildren().clear();
        mapContainer.add(renderer.getView(), 0, 0);

        rm.playerRequest(player -> {
            int i = 0;
            Iterator<String> it = room.getPlayers().iterator();
            while (it.hasNext()){
                Label lblPlayer;
                if(i % 2 == 0){
                    lblPlayer = new Label(it.next());
                    header.add(lblPlayer, i / 2, 0);
                }
                else{
                    lblPlayer = new Label(it.next());
                    header.add(lblPlayer, 4 - i / 2, 0);
                }
                if(player.isAdmin() && !player.getPseudo().equals(lblPlayer.getText())){
                    lblPlayer.setOnMouseClicked(event -> {
                        if (event.getClickCount() == 2) {
                            rm.ejectRequest(lblPlayer.getText());
                        }
                    });
                }
                i++;
            }
        });

        timer = new Timer();
        start = Instant.now();

        timer.schedule(new TimerTask() {
            // Créer une tâche qui incrémente toute les secondes le temps de 1
            public void run() {
                Platform.runLater(() -> displayDuration());
            }
        }, 0, 1000);
    }

    private void displayDuration(){
        long time = Duration.between(start, Instant.now()).getSeconds();
        duration.setText((time / 60 < 10 ? "0" : "") + time / 60 + ":" + (time % 60 < 10 ? "0" : "") + time % 60);
    }

    @FXML
    private void initialize(){

        rm.moveRequest(null, bomberman -> {
            renderer.getArena().notify(bomberman);
        });

        rm.addElementRequest(element -> renderer.getArena().add(element));

        rm.destroyElementsRequest(element -> {
            if(element instanceof Bomb) {
                element.setArena(renderer.getArena());
                ((Bomb) element).showExplosion();
            }

            renderer.getArena().remove(element);
        });

        rm.endGameRequest(statistic -> {
            final Stage results = new Stage();
            results.setTitle("Results");

            results.setOnCloseRequest(event -> Client.getInstance().getPrimatyStage().show());

            results.initModality(Modality.APPLICATION_MODAL);

            FXMLLoader loader = new FXMLLoader(Client.class.getResource("views/game/endGame.fxml"));
            try
            {
                Pane pane = loader.load();
                EndGameController controller = loader.getController();
                controller.setStatistic(statistic);
                results.setScene(new Scene(pane));
                ((Stage)mainPane.getScene().getWindow()).close();
                timer.cancel();
                timer.purge();
                results.showAndWait();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        });
    }
}
