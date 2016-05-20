package ch.heigvd.bomberman.client;

import ch.heigvd.bomberman.common.communication.requests.*;
import ch.heigvd.bomberman.common.communication.responses.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;


public class ResponseManager
{
    private static ResponseManager instance;
    private Socket socket;
    private ObjectOutputStream writer;
    private ObjectInputStream reader;
    private Map<UUID, Consumer> callbacks;
    private Thread receiver;

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

    public void connect(String address, int port) {
        try {
            socket = new Socket(address, port);
            writer = new ObjectOutputStream(socket.getOutputStream());
            reader = new ObjectInputStream(socket.getInputStream());

            receiver = new Thread() {
                @Override
                public void run() {
                    while (!this.isInterrupted()) {
                        try {
                            Response response = (Response) reader.readObject();

                            Consumer callback = callbacks.get(response.getID());
                            Platform.runLater(() -> callback.accept(response.accept(ResponseProcessor.getInstance())));


                        } catch (IOException | ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            receiver.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() throws IOException {
        receiver.interrupt();
        writer.close();
        reader.close();
        socket.close();
    }

    public boolean isConnected() {
        return socket != null && socket.isConnected();
    }

    public void loginRequest(String username, String password, Consumer<Boolean> callback) {
        LoginRequest r = new LoginRequest(username, password);
        send(r, callback);
    }

    public void newAccountRequest(String username, String pwd, Consumer<Boolean> callback){
        AccountCreationRequest r = new AccountCreationRequest(username, pwd);
        send(r, callback);
    }

    private <T> void send(Request<T> r, Consumer<? super T> callback) {
        try {
            writer.writeObject(r);
	        if (callback != null)
                callbacks.put(r.getID(), callback);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}