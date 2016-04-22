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
    private List<RequestManager> clients;

    public Server(int port){
        this.port = port;
        this.clients = new LinkedList<RequestManager>();
    }

    public void loop(){
        try {
            ServerSocket socket = new ServerSocket(port);
            while (true) {
                RequestManager client = new RequestManager(socket.accept()); // creates a manager for each client
                client.start();
                clients.add(client);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Entry point of the server
     * @param args {server port, ... }
     */
    public static void main(String... args){
        //TODO: récupérer le port dans args
        Server server = new Server(3737);
        server.loop();
    }
}
