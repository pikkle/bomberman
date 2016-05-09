package ch.heigvd.bomberman.client;

import ch.heigvd.bomberman.client.views.ClientMainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Client executable class
 */
public class Client extends Application {

    private ResponseManager rm;
    private Stage primaryStage;
    private Pane mainLayout;
    private ClientMainController controller;


 /*  public Client(String ip, int port) throws IOException {
	  rm = new ResponseManager(new Socket(ip, port));
   }
   public ResponseManager responseManager() {
	  return rm;
   }
*/

    /**
     * Entry point of the client
     *
     * @param args Not used
     */
    public static void main(String... args) {
        //TODO: lancer une fenêtre JavaFX
        launch(args);

    }

    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Bomberman");
        FXMLLoader loader = new FXMLLoader();

        loader.setLocation(Client.class.getResource("views/ClientMain.fxml"));
        mainLayout = loader.load();

        primaryStage.setScene(new Scene(mainLayout));
        primaryStage.show();

        controller = loader.getController();
    }
}