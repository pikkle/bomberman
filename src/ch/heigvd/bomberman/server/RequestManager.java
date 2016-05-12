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
                Response response = request.accept(RequestProcessor.getInstance());
                if (response.isSendable()) writer.writeObject(response);
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
}
