package ch.heigvd.bomberman.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.LinkedList;
import java.util.List;

/**
 * Server executable class
 * Runs on port 3737 by default
 */
public class Server {
    private static Server instance;
    private static final int DEFAULT_PORT = 3737;
    private int port;
    private boolean running = true;
    private List<RequestManager> clients;

    private Server(int port){
        this.port = port;
        this.clients = new LinkedList<RequestManager>();
    }


    public static Server getInstance() {
        if (instance == null)
            instance = new Server(DEFAULT_PORT);
        return instance;
    }

    public void start(){
        try {
            ServerSocket socket = new ServerSocket(port);
            synchronized (this) {
                while (running) {
                    RequestManager client = new RequestManager(socket.accept()); // creates a manager for each client
                    client.start();
                    clients.add(client);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public synchronized void stop(){
        running = false;
    }

    /**
     * Entry point of the server
     * @param args {server port, ... }
     */
    public static void main(String... args){
        getInstance().start();
    }
}