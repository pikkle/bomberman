package ch.heigvd.bomberman.server;

import ch.heigvd.bomberman.common.communication.requests.Request;
import ch.heigvd.bomberman.common.communication.responses.AddElementResponse;
import ch.heigvd.bomberman.common.communication.responses.DestroyElementsResponse;
import ch.heigvd.bomberman.common.communication.responses.Response;
import ch.heigvd.bomberman.common.game.Bomberman;
import ch.heigvd.bomberman.common.game.Element;
import ch.heigvd.bomberman.common.game.Player;
import ch.heigvd.bomberman.common.game.bombs.Bomb;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;
import java.util.UUID;

public class RequestManager extends Thread implements Observer {
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
        System.out.println("Client connecting (" + socket.getInetAddress().getHostAddress() + ":" + socket.getPort() + ")...");
        try {
            this.writer = new ObjectOutputStream(socket.getOutputStream());
            this.reader = new ObjectInputStream(socket.getInputStream());
            System.out.println("Client connected (" + socket.getInetAddress().getHostAddress() + ":" + socket.getPort() + ").");
        } catch (IOException e) {
            System.out.println("Client connection error (" + socket.getInetAddress().getHostAddress() + ":" + socket.getPort() + ") : ");
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (isConnected()) {
            try {
                Request request = (Request) reader.readObject();
                System.out.println("Request received from client (" + socket.getInetAddress().getHostAddress() + ":"
                                           + socket.getPort() + ") : " + request.getClass().getSimpleName());
                Response response = request.accept(requestProcessor);
                if (response.isSendable()) {
                    writer.reset();
                    writer.writeObject(response);
                    System.out.println("Response sent to client (" + socket.getInetAddress().getHostAddress() + ":"
                                               + socket.getPort() + ") : " + response.getClass().getSimpleName());
                }
            } catch (EOFException e){
                disconnect();
            } catch (IOException e) {
                System.out.println("Communication error (" + socket.getInetAddress().getHostAddress() + ":" + socket.getPort() + ") : ");
                disconnect();
            } catch (ClassNotFoundException e) {
                System.out.println("Communication error (" + socket.getInetAddress().getHostAddress() + ":" + socket.getPort() + ") : ");
                e.printStackTrace();
            }
        }
    }

    public void send(Response response){
        if(isConnected()) {
            try {
                if (response.isSendable()) {
                    writer.reset();
                    writer.writeObject(response);
                    System.out.println("Response sent to client (" + socket.getInetAddress().getHostAddress() + ":"
                                               + socket.getPort() + ") : " + response.getClass().getSimpleName());
                }
            } catch (EOFException e){
                disconnect();
            } catch (IOException e) {
                System.out.println("Communication error (" + socket.getInetAddress().getHostAddress() + ":" + socket.getPort() + ") : ");
                disconnect();
            }
        }
    }

    public boolean isConnected(){
        return running && socket != null && !socket.isClosed() && socket.isConnected();
    }

    public void disconnect()
    {
        System.out.println("Closing connection (" + socket.getInetAddress().getHostAddress() + ":" + socket.getPort() + ")...");
        try {
            socket.close();
            if(writer != null)
                writer.close();
            if(reader != null)
                reader.close();
            loggedIn = false;
            running = false;
            if(playerSession != null)
                playerSession.close();
            interrupt();
            Server.getInstance().getClients().remove(this);
            System.out.println("Connection closed (" + socket.getInetAddress().getHostAddress() + ":" + socket.getPort() + ").");
        } catch (IOException e) {
            System.out.println("Closing connection error (" + socket.getInetAddress().getHostAddress() + ":" + socket.getPort() + ") : ");
            e.printStackTrace();
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

    @Override
    public void update(Observable o, Object arg) {
        if(arg != null && arg instanceof Element)
            updateElement((Element)arg);
        else if(o instanceof Bomb)
            checkKills((Bomb)o);
    }

    private void updateElement(Element element){
        if(!getPlayerSession().isPresent() || playerSession.getRoomSession() == null)
            return;
        getPlayerSession().ifPresent(p -> {
            if(p.getRoomSession().getArena().elements().contains(element)){
                if(p.getAddUuid() != null)
                    send(new AddElementResponse(p.getAddUuid(), element));
            }
            else {
                if(element instanceof Bomberman) {
                    if(p.getBomberman().equals(element))
                        p.close();
                }
                if(p.getDestroyUuid() != null)
                    send(new DestroyElementsResponse(p.getDestroyUuid(), element));
            }
        });
        checkIfEnded();
    }

    private void checkIfEnded() {
        if(!getPlayerSession().isPresent() || playerSession.getRoomSession() == null)
            return;
        if(getPlayerSession().get().getRoomSession().getPlayers().stream().filter(playerSession -> playerSession.getStatistic().getSurvivalTime() == null).count() <= 1){
            getPlayerSession().get().getRoomSession().close();
        }
    }

    private void checkKills(Bomb bomb){
        if(!getPlayerSession().isPresent() || playerSession.getRoomSession() == null)
            return;
        getPlayerSession().ifPresent(p -> {
            if(!p.getRoomSession().getArena().elements().contains(bomb)){
                bomb.getElementsInRange()
                    .stream()
                    .filter(element -> element instanceof Bomberman)
                    .forEach(bomberman -> playerSession.getStatistic().kill());
            }
        });
    }
}