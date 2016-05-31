package ch.heigvd.bomberman.client;

import ch.heigvd.bomberman.common.communication.Message;
import ch.heigvd.bomberman.common.communication.requests.*;
import ch.heigvd.bomberman.common.communication.responses.Response;
import ch.heigvd.bomberman.common.game.*;
import ch.heigvd.bomberman.common.game.Arena.Arena;
import javafx.application.Platform;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.*;
import java.util.function.Consumer;


public class ResponseManager extends Observable
{
    private static ResponseManager instance;
    private Socket socket;
    private ObjectOutputStream writer;
    private ObjectInputStream reader;
    private Map<UUID, Consumer> callbacks;
    private Thread receiver;
    private boolean closeRequest = false;

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

        socket = new Socket(address, port);
        writer = new ObjectOutputStream(socket.getOutputStream());
        reader = new ObjectInputStream(socket.getInputStream());

        receiver = new Thread()
        {
            @Override
            public void run()
            {
                while (isConnected() && !this.isInterrupted())
                {
                    try
                    {
                        Response response = (Response) reader.readObject();

                        if(callbacks.get(response.getID()) != null) {
                            Consumer callback = callbacks.get(response.getID());
                            Platform.runLater(() -> callback.accept(response.accept(ResponseProcessor.getInstance())));
                        }

                    } catch (IOException | ClassNotFoundException e)
                    {
                        try {
                            disconnect();
                        } catch (IOException e1) {
                            if (!closeRequest)
                                e.printStackTrace();
                        }
                    }
                }
            }
        };
        receiver.start();

    }

    public void disconnect() throws IOException
    {
        receiver.interrupt();
        writer.close();
        reader.close();
        socket.close();
        closeRequest = true;
    }

    public boolean isConnected()
    {
        return socket != null && socket.isConnected();
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

    public void arenasRequest(Consumer<List<Arena>> callback)
    {
        ArenasRequest r = new ArenasRequest();
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

    public void joinRoomRequest(Room room, Consumer<Room> callback)
    {
        JoinRoomRequest r = new JoinRoomRequest(room);
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

    private <T> void send(Request<T> r, Consumer<? super T> callback)
    {
        if(isConnected()) {
            try {
                writer.writeObject(r);
                if (callback != null)
                    callbacks.put(r.getID(), callback);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}