package ch.heigvd.bomberman.server;

import ch.heigvd.bomberman.common.communication.responses.RoomsResponse;
import ch.heigvd.bomberman.common.game.Room;
import ch.heigvd.bomberman.server.controllers.ConsoleAppender;
import ch.heigvd.bomberman.server.controllers.ConsoleController;
import ch.heigvd.bomberman.server.database.DBManager;
import com.sun.javafx.collections.ObservableListWrapper;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Server entry class, runs on port 3737.
 */
public class Server extends Application {
	private static Log logger = LogFactory.getLog(Server.class);
	private static final int DEFAULT_PORT = 3737;
	private static Server instance;
	private int port;
	private boolean running = true;
	private List<RequestManager> clients = new LinkedList<>();
	private ObservableList<RoomSession> roomSessions = new ObservableListWrapper<>(new ArrayList<>(), o -> new Observable[]{o.getPlayers()});
	private DBManager database;
	private ServerSocket socket;
	private TextArea console = new TextArea();
	private Thread thread;

	/**
	 * Creates a singleton server. Has to be public for JavaFX.
	 */
	public Server() {
		synchronized(Server.class){
			ConsoleAppender.setConsole(console);
			if(instance != null) throw new UnsupportedOperationException(
					getClass()+" is singleton but constructor called more than once");
			this.port = DEFAULT_PORT;
			instance = this;
		}
	}

	/**
	 * Singleton accessor
	 * @return the Server singleton instance
     */
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

	/**
	 * Starts the server application
	 * @param primaryStage
	 * @throws IOException
     */
	@Override
	public void start(Stage primaryStage) throws IOException {
		logger.info("Starting server...");

		primaryStage.setTitle("Server");
		FXMLLoader loader = new FXMLLoader();

		loader.setLocation(Server.class.getResource("views/ConsoleView.fxml"));

		AnchorPane mainLayout = null;
		try {
			mainLayout = loader.load();
		} catch (IOException e) {
			logger.fatal("Couldn't create console", e);
			throw e;
		}

		ConsoleController controller = loader.getController();
		controller.setConsole(console);

		primaryStage.setScene(new Scene(mainLayout));
		primaryStage.setOnCloseRequest(event -> {
			Platform.exit();
		});

		database = DBManager.getInstance();
		roomSessions.addListener(new ListChangeListener<RoomSession>() {
			@Override
			public void onChanged(Change<? extends RoomSession> c) {
				sendRooms();
			}
		});

		thread = new Thread(() -> {
			while (running) {
				try {
					socket = new ServerSocket(port);
					synchronized (this) {
						while (running) {
							RequestManager client = new RequestManager(socket.accept()); // creates a manager for each client
							client.start();
							clients.add(client);
						}
					}
				} catch (SocketException e){
					running = false;
				} catch (IOException e) {
					logger.error("Server error", e);
				}
			}
		});
		thread.start();

		logger.info("Server started.");
		primaryStage.show();
	}

	/**
	 * Stops the server application, and closes the open sockets.
	 */
	@Override
	public void stop() {
		logger.info("Closing server...");
		running = false;
		thread.interrupt();
		try {
			socket.close();
			clients.forEach(client -> {
				client.disconnect();
				try {
					client.join();
				} catch (InterruptedException e) {
					logger.error("Waiting on closing connection", e);
				}
			});
			database.closeFactory();
			logger.info("Server closed.");
		} catch (IOException e) {
			logger.error("Closing server error", e);
		}
	}

	/**
	 * Gets the database.
	 * @return the database
     */
	public DBManager getDatabase() {
		return database;
	}

	/**
	 * Adds a room session to the server.
	 * @param roomSession the new room session
     */
	public void addRoom(RoomSession roomSession) {
		roomSessions.add(roomSession);
	}

	/**
	 * Removes a room session of the server.
	 * @param roomSession the room to be deleted
     */
	public void removeRoomSession(RoomSession roomSession) {
		roomSessions.remove(roomSession);
	}

	/**
	 * Gets the rooms list.
	 * @return a list of the available rooms
     */
	public List<RoomSession> getRoomSessions() {
		return roomSessions;
	}

	/**
	 * Gets the clients list.
	 * @return a list of the logged in clients
     */
	public List<RequestManager> getClients(){
		return clients;
	}

	/**
	 * Sends the new rooms list to the clients.
	 */
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