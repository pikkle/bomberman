package ch.heigvd.bomberman.client.views.game;

import ch.heigvd.bomberman.client.Client;
import ch.heigvd.bomberman.client.ResponseManager;
import ch.heigvd.bomberman.client.views.render.ArenaRenderer;
import ch.heigvd.bomberman.common.game.Bomberman;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Created by matthieu.villard on 29.05.2016.
 */
public class GameController {

    private ResponseManager rm;
    private ArenaRenderer renderer;
    private AnchorPane mainPane;
    private Client client;

    public GameController(Bomberman bomberman, Client client){
        renderer = new ArenaRenderer(bomberman.getArena(), bomberman, 750, 750);
        this.client = client;
        mainPane = renderer.getView();
        rm = ResponseManager.getInstance();
        initialize();
    }

    private void initialize(){
        Stage stage = new Stage();
        stage.setTitle("Bomberman");

        stage.initModality(Modality.APPLICATION_MODAL);

        Scene scene = new Scene(mainPane);
        stage.setScene(scene);

        rm.moveRequest(null, bomberman -> {
            renderer.getArena().change(bomberman);
        });

        rm.addElementRequest(element -> {
            renderer.getArena().add(element);
        });

        rm.destroyElementsRequest(element -> {
           renderer.getArena().destroy(element);
        });

        stage.show();
    }

}
