package ch.heigvd.bomberman.client;

import java.io.IOException;
import java.net.Socket;

/**
 * Client executable class
 */
public class Client {

   private ResponseManager rm;

   public Client(String ip, int port) throws IOException {
	  rm = new ResponseManager(new Socket(ip, port));
   }

   public ResponseManager responseManager() {
	  return rm;
   }

   /**
	* Entry point of the client
	*
	* @param args Not used
	*/
   public static void main(String... args) {
	  //TODO: lancer une fenêtre JavaFX

   }
}
