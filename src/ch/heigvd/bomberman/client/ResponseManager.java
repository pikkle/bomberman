package ch.heigvd.bomberman.client;

import ch.heigvd.bomberman.common.communication.requests.HelloRequest;
import ch.heigvd.bomberman.common.communication.requests.Request;
import ch.heigvd.bomberman.common.communication.responses.HelloResponse;
import ch.heigvd.bomberman.common.communication.responses.Response;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ResponseManager {
   private ObjectOutputStream writer;
   private ObjectInputStream reader;
   private boolean run = true;

   class ResponseReceiver extends Thread {
	  @Override
	  public void run() {
		 synchronized (ResponseManager.this) {
			while (run) {
			   try {
				  Response response = (Response) reader.readObject();
				  // TODO check server responses
				  ResponseManager.this.process(response);
			   } catch (IOException | ClassNotFoundException e) {
				  e.printStackTrace();
			   }
			}
		 }
	  }
   }

   public ResponseManager(Socket socket) {
	  try {
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

   public Response process(Response response) {
	  switch (response.getType()){
		 case NO_RESPONSE:
			break;
		 case HELLO_RESPONSE:
			System.out.println(((HelloResponse)response).message());
			break;
		 case MOVE_RESPONSE:

			break;
	  }
	  return response;
   }
}