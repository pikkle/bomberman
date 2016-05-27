package ch.heigvd.bomberman.server;

import ch.heigvd.bomberman.common.communication.requests.*;
import ch.heigvd.bomberman.common.communication.responses.*;
import ch.heigvd.bomberman.common.game.Player;

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
    private PlayerSession playerSession;
    private Player player;
    private Room room;
    private RequestProcessor requestProcessor;
    private boolean loggedIn = false;


    public RequestManager(Socket socket) {
        this.socket = socket;
        this.requestProcessor = new RequestProcessor(this);
        try {
            this.writer = new ObjectOutputStream(socket.getOutputStream());
            this.reader = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        while (running) {
            try {
                Request request = (Request) reader.readObject();
                Response response = request.accept(requestProcessor);
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

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean newState) {
        loggedIn = newState;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer(){
        return player;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Room getRoom(){
        return room;
    }

    public void setPlayerSession(PlayerSession playerSession) {
        this.playerSession = playerSession;
    }

    public PlayerSession getPlayerSession(){
        return playerSession;
    }
}