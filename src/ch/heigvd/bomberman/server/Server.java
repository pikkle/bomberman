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
    private static final int DEFAULT_PORT = 3737;
    private int port;
    private boolean running = true;
    private List<RequestManager> clients;

    public Server(){
        this(DEFAULT_PORT);
    }

    public Server(int port){
        this.port = port;
        this.clients = new LinkedList<RequestManager>();
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
        new Server().start();
    }
}