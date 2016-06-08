package ch.heigvd.bomberman.client;

import ch.heigvd.bomberman.client.views.auth.LoginViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Client executable class
 */
public class Client extends Application {

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
    public void start(Stage primaryStage) throws Exception {

        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Bomberman");
        FXMLLoader loader = new FXMLLoader();

        loader.setLocation(Client.class.getResource("views/auth/LoginView.fxml"));

        mainLayout = loader.load();

        LoginViewController controller = loader.getController();

        primaryStage.setScene(new Scene(mainLayout));
        primaryStage.show();
    }

    @Override
    public void stop(){
        System.out.println("Stage is closing");
        try
        {
            if(rm.isConnected())
                rm.disconnect();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        // Save file
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