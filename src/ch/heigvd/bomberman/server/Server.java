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
    private int port;
    private boolean running = true;
    private List<RequestManager> clients;

    public Server(int port){
        this.port = port;
        this.clients = new LinkedList<RequestManager>();
    }

    public void loop(){
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
        int port = 3737;
        if (args.length == 1){
            int parsed = Integer.parseInt(args[0]);
            if (0 <= parsed && parsed <= 65535)
                port = parsed;
            else
                throw new IllegalArgumentException("The port number must be between 0 and 65535.");
        } else if (args.length > 1){
            throw new IllegalArgumentException("Only one parameter, or none.");
        }

        Server server = new Server(port);
        server.loop();
    }
}
