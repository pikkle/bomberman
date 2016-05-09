package ch.heigvd.bomberman.server;

import ch.heigvd.bomberman.common.communication.requests.*;
import ch.heigvd.bomberman.common.communication.responses.*;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class RequestManager extends Thread {
    private Socket socket;
    private ObjectOutputStream writer;
    private ObjectInputStream reader;
    public boolean running = true;
	private PlayerSession player;
	private Room room;


    public RequestManager(Socket socket) {
        this.socket = socket;
        try {
            this.writer = new ObjectOutputStream(socket.getOutputStream());
            this.reader = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

	    // TODO: remove the creation of a player session here. He is normally created when the client joins a room or creates one
	    player = new PlayerSession();
	    room = new Room("Test", "", 1);
	    room.addPlayer(player);
    }

    @Override
    public void run() {
        while (running) {
            try {
                Request request = (Request) reader.readObject();
                Response response = process(request);
                send(response);
            } catch (EOFException e) {
                System.out.println("Client closed the connection");
                running = false;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Interprets the request, and makes a response to it
     *
     * @param request
     * @return
     */
    public Response process(Request request) {
        Response response = new NoResponse();
        switch (request.getType()) {
            case ACCOUNT_CREATION:
                break;
            case HELLO:
                HelloRequest helloReq = (HelloRequest) request;
                System.out.println("Received message: ");
                System.out.println(helloReq.getMessage());
                return new HelloResponse("Hello !");
	        case MOVE:
		        MoveRequest moveReq = (MoveRequest) request;
		        if (room.isRunning())
					player.getBomberman().move(moveReq.getDirection());
		        break;
	        case READY:
		        ReadyRequest readyReq = (ReadyRequest) request;
		        player.ready(readyReq.getState());
		        break;

            // TODO: other request types
        }
        return response;
    }

    /**
     * Sends the response to the client
     *
     * @param response
     */
    private void send(Response response) throws IOException {
        if (response.getType() != ResponseType.NO_RESPONSE) writer.writeObject(response);
    }

}
