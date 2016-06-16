package ch.heigvd.bomberman.server;

import ch.heigvd.bomberman.common.game.*;
import ch.heigvd.bomberman.common.game.Arena;
import ch.heigvd.bomberman.common.game.bombs.Bomb;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.Instant;
import java.util.Optional;

/**
 * Represents a room session containing players to play a game of bomberman.
 */
public class RoomSession {
	private String name;
	private Arena arena;
	private String password;
	private int minPlayer;
	private ObservableList<PlayerSession> players = FXCollections.observableArrayList();
	private boolean running = false;
	private Game game = new Game();
	private Instant start;
	private Player owner;

	/**
	 * Creates a room.
	 * @param name the name of the room
	 * @param password the password of the room
	 * @param minPlayer the minimum amount of players required to launch the game
	 * @param arena the arena that will be used for the game
     * @param owner the creator of the arena
     */
	public RoomSession(String name, String password, int minPlayer, Arena arena, Player owner) {
		this.name = name;
		this.password = password;
		this.arena = arena;
		this.minPlayer = minPlayer;
		this.owner = owner;
	}

	/**
	 * Gets the creator of the room.
	 * @return the owner of the room
     */
	public Player getOwner(){
		return owner;
	}

	/**
	 * Gets the game start's time.
	 * @return the time at which the game has started.
     */
	public Instant getStart(){
		return start;
	}

	/**
	 * Gets the game corresponding to the room session.
	 * @return the game
     */
	public Game getGame(){
		return game;
	}

	/**
	 * Closes the room and tells the players inside to quit.
	 */
	public synchronized void close(){
		while(!players.isEmpty()){
			players.stream().findFirst().get().close();
		}
		running = false;
		Server.getInstance().getRoomSessions().remove(this);
	}

	/**
	 * Gets the name of the room.
	 * @return the room's name
     */
	public String getName() {
		return name;
	}

	/**
	 * Gets the arena of the room session.
	 * @return the room's arena
     */
	public Arena getArena() {
		return arena;
	}

	/**
	 * Gets the room's password.
	 * @return the room's password
     */
	public String getPassword() {
		return password;
	}

	/**
	 * Gets the minimum amount of players required to launch a game.
	 * @return the minimum amount of players required
     */
	public int getMinPlayer() {
		return minPlayer;
	}

	/**
	 * Gets the players in a list.
	 * @return the players list
     */
	public synchronized ObservableList<PlayerSession> getPlayers() {
		return players;
	}

	/**
	 * Adds a player in the room
	 * @param p the player to add
	 * @throws Exception
     */
	public synchronized void addPlayer(PlayerSession p) throws Exception {
		if (!players.contains(p)) {
			if (players.size() >= 4) throw new Exception("Already 4 players !");
			players.add(p);
		}
	}

	/**
	 * Removes a player from the room.
	 * @param p the player to remove
     */
	public synchronized void removePlayer(PlayerSession p) {
		if (players.contains(p)) {
			players.remove(p);
		}
	}

	/**
	 * Starts the game.
	 */
	public synchronized void start() {
		game.getStatistics().clear();
		for (PlayerSession player : players) {
			Optional<StartPoint> start = arena.elements(StartPoint.class).stream().findFirst();
			if (start.isPresent()) {
				arena.remove(start.get());
				player.setBomberman(
						new Bomberman(start.get().position(), Skin.values()[players.indexOf(player)], arena)
				);
				game.addStatistic(player.getStatistic());
			}
			else{
				return;
			}
		}
		arena.elements(StartPoint.class).forEach(startPoint -> arena.remove(startPoint));
		running = true;
		start = Instant.now();

		new Thread(() -> {
			while (running) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				arena.elements(Bomb.class).forEach(b -> {
					b.decreaseCountdown();
					if (b.countdown() <= 0) {
						b.delete();
					}
				});
			}
		}).start();
	}

	/**
	 * Returns whether the game is running
	 * @return the game status
     */
	public synchronized boolean isRunning() {
		return running;
	}

	/**
	 * Tries to launch the game
	 */
	public void update() {
		for (PlayerSession player : players) {
			if (!player.isReady()) {
				return;
			}
		}
		if (players.size() < minPlayer) {
			return;
		}

		start();
	}
}