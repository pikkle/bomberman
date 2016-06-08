package ch.heigvd.bomberman.server;

import ch.heigvd.bomberman.common.communication.requests.Request;
import ch.heigvd.bomberman.common.communication.responses.Response;
import ch.heigvd.bomberman.common.game.Player;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Optional;
import java.util.UUID;

public class RequestManager extends Thread {
    private Socket socket;
    private ObjectOutputStream writer;
    private ObjectInputStream reader;
    public boolean running = true;
    private PlayerSession playerSession;
    private Player player;
    private RequestProcessor requestProcessor;
    private boolean loggedIn = false;
    private UUID roomsCallback;


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
        while (running && socket != null && socket.isConnected()) {
            try {
                Request request = (Request) reader.readObject();
                Response response = request.accept(requestProcessor);
                if (response.isSendable()) {
                    writer.reset();
                    writer.writeObject(response);
                }
            } catch (IOException e) {
                e.printStackTrace();
                try {
                    disconnect();
                } catch (IOException e1) {
                    if (running)
                        e.printStackTrace();
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void send(Response response){
        if(running && socket != null && socket.isConnected()) {
            try {
                if (response.isSendable()) {
                    writer.reset();
                    writer.writeObject(response);
                }
            } catch (IOException e) {
                try {
                    disconnect();
                } catch (IOException e1) {
                    if (running)
                        e.printStackTrace();
                }
            }
        }
    }

    public void disconnect() throws IOException
    {
        interrupt();
        socket.close();
        writer.close();
        reader.close();
        loggedIn = false;
        running = false;
        if(playerSession != null)
            playerSession.close();
        Server.getInstance().getClients().remove(this);
        System.out.println("Client closed the connection");
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

    public void closePlayerSession() {
        if(getPlayerSession().isPresent()){
            playerSession.close();
            playerSession = null;
        }
    }

    public void openPlayerSession(RoomSession roomSession, UUID uuid) throws Exception {
        if(getPlayerSession().isPresent()){
            closePlayerSession();
        }
        playerSession = new PlayerSession(player, roomSession, uuid, this);
        roomSession.addPlayer(playerSession);
    }

    public Optional<PlayerSession> getPlayerSession(){
        return Optional.ofNullable(playerSession);
    }

    public void setRoomsCallback(UUID roomsCallback){
        this.roomsCallback = roomsCallback;
    }

    public UUID getRoomsCallback(){
        return roomsCallback;
    }
}