package ch.heigvd.bomberman.server;

import ch.heigvd.bomberman.common.game.*;
import ch.heigvd.bomberman.common.game.Arena.Arena;
import ch.heigvd.bomberman.common.game.bombs.Bomb;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.Instant;
import java.util.Optional;

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
	 * Creates a room
	 *
	 * @param name
	 * @param password
	 */
	public RoomSession(String name, String password, int minPlayer, Arena arena, Player owner) {
		this.name = name;
		this.password = password;
		this.arena = arena;
		this.minPlayer = minPlayer;
		this.owner = owner;
	}

	public Player getOwner(){
		return owner;
	}

	public Instant getStart(){
		return start;
	}

	public Game getGame(){
		return game;
	}

	public synchronized void close(){
		while(!players.isEmpty()){
			players.stream().findFirst().get().close();
		}
		running = false;
		Server.getInstance().getRoomSessions().remove(this);
	}

	public String getName() {
		return name;
	}

	public Arena getArena() {
		return arena;
	}

	public String getPassword() {
		return password;
	}

	public int getMinPlayer() {
		return minPlayer;
	}

	public synchronized ObservableList<PlayerSession> getPlayers() {
		return players;
	}

	public synchronized void addPlayer(PlayerSession p) throws Exception {
		if (!players.contains(p)) {
			if (players.size() >= 4) throw new Exception("Already 4 players !");
			players.add(p);
		}
	}

	public synchronized void removePlayer(PlayerSession p) {
		if (players.contains(p)) {
			players.remove(p);
		}
	}

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