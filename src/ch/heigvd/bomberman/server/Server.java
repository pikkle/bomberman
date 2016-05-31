package ch.heigvd.bomberman.server;

import ch.heigvd.bomberman.server.database.DBManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.sql.SQLException;
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
    private List<RoomSession> roomSessions = new LinkedList();
    private DBManager database;

    private Server(int port){
        this.port = port;
        this.clients = new LinkedList<RequestManager>();
        try {
            database = DBManager.getInstance();
        } catch (SQLException e) {
            System.out.println("Database error:");
            e.printStackTrace();
        }
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

    public synchronized void stop() {
        running = false;
    }

    public DBManager getDatabase() {
        return database;
    }

    public void addRoom(RoomSession roomSession){
       roomSessions.add(roomSession);
    }

    public void removeRoom(RoomSession roomSession){
        roomSessions.remove(roomSession);
    }

    public List<RoomSession> getRoomSessions(){
        return roomSessions;
    }

    public List<RequestManager> getClients(){
        return clients;
    }

    /**
     * Entry point of the server
     * @param args {server port, ... }
     */
    public static void main(String... args){
        getInstance().start();
    }
}