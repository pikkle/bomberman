package ch.heigvd.bomberman.server;

import ch.heigvd.bomberman.common.communication.responses.RoomsResponse;
import ch.heigvd.bomberman.common.game.Room;
import ch.heigvd.bomberman.server.database.DBManager;
import com.sun.javafx.collections.ObservableListWrapper;
import javafx.application.Application;
import javafx.beans.Observable;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.ServerSocket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Server executable class
 * Runs on port 3737 by default
 */
public class Server extends Application {
	private static final int DEFAULT_PORT = 3737;
	private static Server instance;
	private int port;
	private boolean running = true;
	private List<RequestManager> clients = new LinkedList<>();
	private ObservableList<RoomSession> roomSessions = new ObservableListWrapper<>(new ArrayList<>(),
	                                                                               o -> new Observable[]{o.getPlayers()});
	private DBManager database;

	public Server() {
		System.out.println("Starting server..");
		this.port = DEFAULT_PORT;
		try {
			database = DBManager.getInstance();
			roomSessions.addListener(new ListChangeListener<RoomSession>() {
				@Override
				public void onChanged(Change<? extends RoomSession> c) {
					sendRooms();
				}
			});
			System.out.println("Server started.");
		} catch (SQLException e) {
			System.out.println("Database error:");
			e.printStackTrace();
		}
		instance = this;
	}

	public static Server getInstance() {
		if (instance == null) instance = new Server();
		return instance;
	}

	/**
	 * Entry point of the server
	 *
	 * @param args {server port, ... }
	 */
	public static void main(String... args) {
		launch(args);
	}

	public void start(Stage primaryStage) {
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

	public void addRoom(RoomSession roomSession) {
		roomSessions.add(roomSession);
	}

	public void removeRoomSession(RoomSession roomSession) {
		roomSessions.remove(roomSession);
	}

	public List<RoomSession> getRoomSessions() {
		return roomSessions;
	}

	public List<RequestManager> getClients(){
		return clients;
	}

    public void sendRooms(){
        clients.stream()
               .filter(client -> client.getRoomsCallback() != null)
               .forEach(client -> client.send(
		               new RoomsResponse(
				               client.getRoomsCallback(),
				               roomSessions.stream()
				                           .map(r -> new Room(r.getName(),
				                                              r.getPassword() != null && ! r.getPassword().isEmpty(),
				                                              r.getMinPlayer(),
				                                              r.getPlayers().stream().map(p -> p.getPlayer().getPseudo()).collect(Collectors.toList()),
				                                              r.getArena(),
				                                              client.getPlayerSession().isPresent() && r.getPlayers().contains(client.getPlayerSession().get())))
				                           .collect(Collectors.toList()))));
    }
}