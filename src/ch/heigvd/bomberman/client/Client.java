package ch.heigvd.bomberman.client;

import ch.heigvd.bomberman.client.views.auth.LoginViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;

/**
 * Client executable class
 */
public class Client extends Application {
    private static Log logger = LogFactory.getLog(Client.class);
    private static Client instance;
    private Stage primaryStage;
    private Pane mainLayout;
    private ResponseManager rm = ResponseManager.getInstance();

    public Client(){
        synchronized(Client.class){
            if(instance != null) throw new UnsupportedOperationException(
                    getClass()+" is singleton but constructor called more than once");
            instance = this;
        }
    }

    public static Client getInstance() {
        if (instance == null)
            instance = new Client();
        return instance;
    }

    /**
     * Entry point of the client
     *
     * @param args Not used
     */
    public static void main(String... args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws IOException {
        logger.info("Starting client...");
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Bomberman");

        primaryStage.setResizable(false);

        FXMLLoader loader = new FXMLLoader();

        loader.setLocation(Client.class.getResource("views/auth/LoginView.fxml"));

        try {
            mainLayout = loader.load();
        } catch (IOException e) {
            logger.fatal("Couldn't create login form", e);
            throw e;
        }

        LoginViewController controller = loader.getController();

        primaryStage.setScene(new Scene(mainLayout));
        primaryStage.show();
        logger.info("Client started");
    }

    @Override
    public void stop(){
        logger.info("Closing client...");
        if(rm.isConnected())
            rm.stop();
        logger.info("Client closed");
    }

    public void changeScene(Pane pane){
        primaryStage.hide();
        mainLayout = pane;
        primaryStage.setScene(new Scene(mainLayout));

        primaryStage.show();
    }

    public void setTitle(String title){
        primaryStage.setTitle(title);
    }

    public ResponseManager getRm() {
        return rm;
    }

    public Stage getPrimatyStage(){
        return primaryStage;
    }
}