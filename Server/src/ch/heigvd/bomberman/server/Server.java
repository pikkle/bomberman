package ch.heigvd.bomberman.server;

import ch.heigvd.bomberman.common.communication.responses.RoomsResponse;
import ch.heigvd.bomberman.common.game.Room;
import ch.heigvd.bomberman.server.controllers.ConsoleAppender;
import ch.heigvd.bomberman.server.controllers.ConsoleController;
import ch.heigvd.bomberman.server.database.DBManager;
import com.sun.javafx.collections.ObservableListWrapper;
import javafx.application.Application;
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

	public Server() {
		synchronized(Server.class){
			ConsoleAppender.setConsole(console);
			if(instance != null) throw new UnsupportedOperationException(
					getClass()+" is singleton but constructor called more than once");
			this.port = DEFAULT_PORT;
			instance = this;
		}
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

	@Override
	public void start(Stage primaryStage) throws IOException, SQLException {
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
			logger.info("Closing server...");
			running = false;
			try {
				if(socket != null)
					socket.close();
				clients.forEach(client -> client.disconnect());
			} catch (IOException e) {
				logger.error("Server closing error", e);
				e.printStackTrace();
				event.consume();
			}
		});

		database = DBManager.getInstance();
		roomSessions.addListener(new ListChangeListener<RoomSession>() {
			@Override
			public void onChanged(Change<? extends RoomSession> c) {
				sendRooms();
			}
		});

		new Thread(() -> {
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
					e.printStackTrace();
				}
			}
		}).start();

		logger.info("Server started.");
		primaryStage.show();
	}

	@Override
	public synchronized void stop() {
		clients.forEach(client -> client.disconnect());
		logger.info("Server closed...");
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