package ch.heigvd.bomberman.client;

import ch.heigvd.bomberman.common.communication.requests.*;
import ch.heigvd.bomberman.common.communication.responses.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ResponseManager {
   	private ObjectOutputStream writer;
   	private ObjectInputStream reader;
   	private boolean run = true;
	private boolean isConnected = false;

   class ResponseReceiver extends Thread {
	  @Override
	  public void run() {
		 synchronized (ResponseManager.this) {
			while (run) {
			   try {
				  Response response = (Response) reader.readObject();
				   response.getID();
				  response.accept(ResponseProcessor.getInstance());
			   } catch (IOException | ClassNotFoundException e) {
				  e.printStackTrace();
			   }
			}
		 }
	  }
   }

   public ResponseManager(String ip, int port) {
	  try {
		  Socket socket = new Socket(ip, port);
		  isConnected = socket.isConnected();
		  writer = new ObjectOutputStream(socket.getOutputStream());
		  reader = new ObjectInputStream(socket.getInputStream());
	  } catch (IOException e) {
		  e.printStackTrace();
	  }
	  new ResponseReceiver().start();
   }

   public void stop() {
	  run = false;
   }

   public void send(Request request) {
	  try {
		 writer.writeObject(request);
	  } catch (IOException e) {
		 e.printStackTrace();
	  }
   }

   public void process(Response response) {

   }

	public boolean isConnected(){
		return isConnected;
	}
}