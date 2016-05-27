package ch.heigvd.bomberman.client;

import ch.heigvd.bomberman.client.views.auth.LoginViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.UncheckedIOException;

/**
 * Client executable class
 */
public class Client extends Application {

    private Stage primaryStage;
    private Pane mainLayout;
    private ResponseManager rm = ResponseManager.getInstance();


    /**
     * Entry point of the client
     *
     * @param args Not used
     */
    public static void main(String... args) {
        launch(args);
    }

    public void start(Stage primaryStage) throws Exception {

        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Bomberman");
        FXMLLoader loader = new FXMLLoader();

        loader.setLocation(Client.class.getResource("views/auth/LoginView.fxml"));

        mainLayout = loader.load();

        LoginViewController controller = loader.getController();
        controller.setClient(this);
        primaryStage.setOnCloseRequest(event -> {
            try
            {
                ResponseManager.getInstance().disconnect();
            } catch (IOException e)
            {
                throw new UncheckedIOException(e);
            }
        });

        primaryStage.setScene(new Scene(mainLayout));

        primaryStage.show();
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
}