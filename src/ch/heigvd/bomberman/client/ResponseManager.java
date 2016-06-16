package ch.heigvd.bomberman.client;

import ch.heigvd.bomberman.common.communication.Message;
import ch.heigvd.bomberman.common.communication.requests.*;
import ch.heigvd.bomberman.common.communication.responses.Response;
import ch.heigvd.bomberman.common.game.Arena;
import ch.heigvd.bomberman.common.game.*;
import ch.heigvd.bomberman.common.util.Direction;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.*;
import java.util.function.Consumer;


public class ResponseManager extends Observable
{
    private static Log logger = LogFactory.getLog(ResponseManager.class);
    private static ResponseManager instance;
    private Socket socket;
    private ObjectOutputStream writer;
    private ObjectInputStream reader;
    private Map<UUID, Consumer> callbacks;
    private Thread receiver;
    private boolean closeRequest = true;

    private ResponseManager()
    {
        callbacks = new HashMap<>();
    }

    public static ResponseManager getInstance()
    {
        if (instance == null)
            instance = new ResponseManager();
        return instance;
    }

    public void connect(String address, int port) throws IOException
    {
        if(!isConnected()) {
            closeRequest = false;
            socket = new Socket(address, port);
            writer = new ObjectOutputStream(socket.getOutputStream());
            reader = new ObjectInputStream(socket.getInputStream());

            receiver = new Thread() {
                @Override
                public void run() {
                    while (isConnected()) {
                        try {
                            Response response = (Response) reader.readObject();

                            if (callbacks.get(response.getID()) != null) {
                                Consumer callback = callbacks.get(response.getID());
                                Platform.runLater(() -> callback.accept(response.accept(ResponseProcessor.getInstance())));
                            }
                        } catch (EOFException e) {
                            disconnect();
                        } catch (SocketException e) {
                            disconnect();
                        } catch (IOException e) {
                            logger.error("Communication error (" + socket.getInetAddress().getHostAddress() + ":" + socket
                                    .getPort() + ")", e);
                            disconnect();
                        } catch (ClassNotFoundException e) {
                            logger.error("Communication error (" + socket.getInetAddress().getHostAddress() + ":" + socket
                                    .getPort() + ")", e);
                        }
                    }
                }
            };
            receiver.start();
        }
    }

    public void stop(){
        if(isConnected()) {
            logger.info("Closing connection (" + socket.getInetAddress().getHostAddress() + ":" + socket.getPort() + ")...");
            closeRequest = true;
            receiver.interrupt();
            try {
                socket.close();
                if (writer != null)
                    writer.close();
                if (reader != null)
                    reader.close();
                logger.info("Connection closed (" + socket.getInetAddress().getHostAddress() + ":" + socket.getPort() +
                                    ").");
            } catch (IOException e) {
                logger.error("Closing connection error (" + socket.getInetAddress().getHostAddress() + ":" + socket
                        .getPort() + ")", e);
            }
        }
    }

    public void disconnect()
    {
        if(isConnected()) {
            stop();
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Server error");
                alert.setTitle("Server error");
                alert.setContentText("Server is unreachable. Try to connect again.");
                alert.showAndWait();
                Platform.exit();
            });
        }
    }

    public boolean isConnected(){
        return !closeRequest && socket != null && !socket.isClosed() && socket.isConnected();
    }

    public void loginRequest(String username, String password, Consumer<Message> callback)
    {
        LoginRequest r = new LoginRequest(username, password);
        send(r, callback);
    }

    public void newAccountRequest(String username, String pwd, Consumer<Message> callback)
    {
        AccountCreationRequest r = new AccountCreationRequest(username, pwd);
        send(r, callback);
    }

    public void modifyAccountRequest(String username, String pwd, Consumer<Message> callback)
    {
        AccountModifyRequest r = new AccountModifyRequest(username, pwd);
        send(r, callback);
    }

    public void playerRequest(Consumer<Player> callback)
    {
        PlayerRequest r = new PlayerRequest();
        send(r, callback);
    }

    public void playerStateRequest(long id, boolean isLocked, Consumer<Message> callback)
    {
        PlayerStateRequest r = new PlayerStateRequest(id, isLocked);
        send(r, callback);
    }

    public void playersRequest(Consumer<List<Player>> callback)
    {
        PlayersRequest r = new PlayersRequest();
        send(r, callback);
    }

    public void arenasRequest(Consumer<List<Arena>> callback)
    {
        ArenasRequest r = new ArenasRequest();
        send(r, callback);
    }

    public void saveArenaRequest(Arena arena, Consumer<Message> callback)
    {
        SaveArenaRequest r = new SaveArenaRequest(arena);
        send(r, callback);
    }

    public void removeArenaRequest(Arena arena, Consumer<Message> callback)
    {
        RemoveArenaRequest r = new RemoveArenaRequest(arena);
        send(r, callback);
    }

    public void createRoomRequest(String name, long arena, int minPlayer, String password, Consumer<Message> callback)
    {
        CreateRoomRequest r = new CreateRoomRequest(name, arena, minPlayer, password);
        send(r, callback);
    }

    public void roomsRequest(Consumer<List<Room>> callback)
    {
        RoomsRequest r = new RoomsRequest();
        send(r, callback);
    }

    public void joinRoomRequest(String room, Consumer<Room> callback)
    {
        JoinRoomRequest r = new JoinRoomRequest(room, null);
        send(r, callback);
    }

    public void joinRoomRequest(String room, String password, Consumer<Room> callback)
    {
        JoinRoomRequest r = new JoinRoomRequest(room, password);
        send(r, callback);
    }

    public void readyRequest(boolean ready, Consumer<Bomberman> callback)
    {
        ReadyRequest r = new ReadyRequest(ready);
        send(r, callback);
    }

    public void moveRequest(Direction direction, Consumer<Bomberman> callback)
    {
        MoveRequest r = new MoveRequest(direction);
        send(r, callback);
    }

    public void addElementRequest(Consumer<Element> callback)
    {
        AddElementRequest r = new AddElementRequest();
        send(r, callback);
    }

    public void destroyElementsRequest(Consumer<Element> callback)
    {
        DestroyElementsRequest r = new DestroyElementsRequest();
        send(r, callback);
    }

    public void dropBombRequest()
    {
        DropBombRequest r = new DropBombRequest();
        send(r, null);
    }

    public void endGameRequest(Consumer<Statistic> callback)
    {
        EndGameRequest r = new EndGameRequest();
        send(r, callback);
    }

    public void ejectRequest(String player){
        EjectRequest r = new EjectRequest(player);
        send(r, null);
    }

    private <T> void send(Request<T> r, Consumer<? super T> callback)
    {
        if(isConnected()) {
            try {
                writer.writeObject(r);
                if (callback != null)
                    callbacks.put(r.getID(), callback);
            }
            catch (IOException e) {
                logger.error("Communication error (" + socket.getInetAddress().getHostAddress() + ":" + socket
                        .getPort() + ")", e);
            }
        }
    }
}