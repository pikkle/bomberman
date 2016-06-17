package ch.heigvd.bomberman.server;

import ch.heigvd.bomberman.common.communication.requests.Request;
import ch.heigvd.bomberman.common.communication.responses.AddElementResponse;
import ch.heigvd.bomberman.common.communication.responses.DestroyElementsResponse;
import ch.heigvd.bomberman.common.communication.responses.Response;
import ch.heigvd.bomberman.common.game.Bomberman;
import ch.heigvd.bomberman.common.game.Element;
import ch.heigvd.bomberman.common.game.Player;
import ch.heigvd.bomberman.common.game.bombs.Bomb;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;
import java.util.UUID;

/**
 * Treats the communication with the client. Each request manager object is dedicated to one client only.
 */
public class RequestManager extends Thread implements Observer {
	private static Log logger = LogFactory.getLog(RequestManager.class);
	private Socket socket;
	private ObjectOutputStream writer;
	private ObjectInputStream reader;
	public boolean running = true;
	private PlayerSession playerSession;
	private Player player;
	private RequestProcessor requestProcessor;
	private boolean loggedIn = false;
	private UUID roomsCallback;


	/**
	 * Constructs a request manager for a new client.
	 *
	 * @param socket the socket of communication with the client
	 */
	public RequestManager(Socket socket) {
		this.socket = socket;
		this.requestProcessor = new RequestProcessor(this);
		logger.info("Client connecting (" + socket.getInetAddress().getHostAddress() + ":" + socket.getPort() + ")...");
		try {
			this.writer = new ObjectOutputStream(socket.getOutputStream());
			this.reader = new ObjectInputStream(socket.getInputStream());
			logger.info("Client connected (" + socket.getInetAddress().getHostAddress() + ":" + socket.getPort() + ").");
		} catch (IOException e) {
			logger.error("Client connection error (" + socket.getInetAddress().getHostAddress() + ":" + socket
					.getPort() + ")", e);
			e.printStackTrace();
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Waits continually for new requests from the client, and response to him with responses objects.
	 */
	@Override
	public void run() {
		while (isConnected()) {
			try {
				Request request = (Request) reader.readObject();
				logger.info("Request received from client (" + socket.getInetAddress().getHostAddress() + ":"
						            + socket.getPort() + ") : " + request.getClass().getSimpleName());

				Response response = request.accept(requestProcessor);
				if (response.isSendable()) {
					writer.reset();
					writer.writeObject(response);
					logger.info("Response sent to client (" + socket.getInetAddress().getHostAddress() + ":"
							            + socket.getPort() + ") : " + response.getClass().getSimpleName());
				}
			} catch (EOFException e) {
				disconnect();
			} catch (SocketException e) {
				disconnect();
			} catch (IOException e) {
				logger.error("Communication error (" + socket.getInetAddress().getHostAddress() + ":" + socket
						.getPort() + ")", e);
				disconnect();
			} catch (ClassNotFoundException e) {
				logger.error("Communication error (" + socket.getInetAddress().getHostAddress() + ":" + socket
						.getPort() + ")", e);
			}
		}
		try {
			socket.close();
			logger.info("Connection closed (" + socket.getInetAddress().getHostAddress() + ":" + socket.getPort() + ").");
		} catch (IOException e) {
			logger.error("Closing connection error (" + socket.getInetAddress().getHostAddress() + ":" + socket
					.getPort() + ")", e);
		}
	}

	/**
	 * Sends a response to the client.
	 *
	 * @param response the response to send
	 */
	public void send(Response response) {
		if (isConnected()) {
			try {
				if (response.isSendable()) {
					writer.reset();
					writer.writeObject(response);
					logger.info("Response sent to client (" + socket.getInetAddress().getHostAddress() + ":"
							            + socket.getPort() + ") : " + response.getClass().getSimpleName());
				}
			} catch (EOFException e) {
				disconnect();
			} catch (SocketException e) {
				disconnect();
			} catch (IOException e) {
				logger.error("Communication error (" + socket.getInetAddress().getHostAddress() + ":" + socket
						.getPort() + ")", e);
				disconnect();
			}
		}
	}

	/**
	 * Returns whether the client is connected to the server or not.
	 *
	 * @return the connection status of the client.
	 */
	public boolean isConnected() {
		return running && socket != null && !socket.isClosed() && socket.isConnected();
	}


	/**
	 * Closes the connection to the client.
	 */
	public void disconnect() {
		if (isConnected()) {
			logger.info("Closing connection (" + socket.getInetAddress().getHostAddress() + ":" + socket.getPort() + ")...");
			running = false;
			interrupt();
			loggedIn = false;
			if (playerSession != null)
				playerSession.close();
			if (playerSession != null)
				playerSession.getRoomSession().getArena().deleteObserver(this);
			Server.getInstance().getClients().remove(this);
			try {
				if (writer != null)
					writer.close();
				if (reader != null)
					reader.close();
			} catch (IOException e) {
				logger.error("Closing streams error (" + socket.getInetAddress().getHostAddress() + ":" + socket
						.getPort() + ")", e);
			}
		}
	}

	/**
	 * Returns whether the client is logged in (proving the right credentials).
	 *
	 * @return the authentication status of the client
	 */
	public boolean isLoggedIn() {
		return loggedIn;
	}

	/**
	 * Updates the authentication status of the client.
	 *
	 * @param newState the new authentication status.
	 */
	public void setLoggedIn(boolean newState) {
		loggedIn = newState;
	}

	/**
	 * Updates the player corresponding the client.
	 *
	 * @param player the new player object
	 */
	public void setPlayer(Player player) {
		this.player = player;
	}

	/**
	 * Gets the player object of the client.
	 *
	 * @return the player object
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Closes the player session
	 */
	public void closePlayerSession() {
		if (getPlayerSession().isPresent()) {
			playerSession.close();
			playerSession = null;
		}
	}

	/**
	 * Opens a new player session
	 *
	 * @param roomSession the room the player enters
	 * @param readyUuid   the ready request's uuid
	 * @throws Exception
	 */
	public void openPlayerSession(RoomSession roomSession, UUID readyUuid) throws Exception {
		if (getPlayerSession().isPresent()) {
			closePlayerSession();
		}
		playerSession = new PlayerSession(player, roomSession, readyUuid, this);
		roomSession.addPlayer(playerSession);
	}

	/**
	 * Gets the player session.
	 *
	 * @return an Optional of the player session
	 */
	public Optional<PlayerSession> getPlayerSession() {
		return Optional.ofNullable(playerSession);
	}

	/**
	 * Sets the new room callback id.
	 *
	 * @param roomsCallback the new room's callback uuid
	 */
	public void setRoomsCallback(UUID roomsCallback) {
		this.roomsCallback = roomsCallback;
	}

	/**
	 * Gets the room callback id.
	 *
	 * @return the room's callback uuid
	 */
	public UUID getRoomsCallback() {
		return roomsCallback;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void update(Observable o, Object arg) {
		if (arg != null && arg instanceof Element)
			updateElement((Element) arg);
		else if (o instanceof Bomb)
			checkKills((Bomb) o);
	}

	/**
	 * Updates the element.
	 *
	 * @param element the new element to be updated
	 */
	private void updateElement(Element element) {
		if (!getPlayerSession().isPresent() || playerSession.getRoomSession() == null)
			return;
		getPlayerSession().ifPresent(p -> {
			if (p.getRoomSession().getArena().elements().contains(element)) {
				if (p.getAddUuid() != null)
					send(new AddElementResponse(p.getAddUuid(), element));
			} else {
				if (element instanceof Bomberman) {
					if (p.getBomberman().equals(element))
						p.close();
				}
				if (p.getDestroyUuid() != null)
					send(new DestroyElementsResponse(p.getDestroyUuid(), element));
			}
		});
		checkIfEnded();
	}

	/**
	 * Informs the players if the game is ended.
	 */
	private void checkIfEnded() {
		if (!getPlayerSession().isPresent() || playerSession.getRoomSession() == null)
			return;
		if (getPlayerSession().get().getRoomSession().getPlayers().stream()
		                      .filter(playerSession -> playerSession.getStatistic().getSurvivalTime() == null)
		                      .count() <= 1) {
			getPlayerSession().get().getRoomSession().close();
		}
	}

	/**
	 * Checks if a bomb kills a person.
	 *
	 * @param bomb the bomb that has exploded
	 */
	private void checkKills(Bomb bomb) {
		if (!getPlayerSession().isPresent() || playerSession.getRoomSession() == null)
			return;
		getPlayerSession().ifPresent(p -> {
			if (!p.getRoomSession().getArena().elements().contains(bomb)) {
				bomb.getElementsInRange()
				    .stream()
				    .filter(element -> element instanceof Bomberman)
				    .forEach(bomberman -> playerSession.getStatistic().kill());
			}
		});
	}
}