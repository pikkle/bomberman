package ch.heigvd.bomberman.server;

import ch.heigvd.bomberman.common.game.Arena.Arena;
import ch.heigvd.bomberman.common.game.Bomberman;
import ch.heigvd.bomberman.common.game.Point;
import ch.heigvd.bomberman.common.game.Skin;
import javafx.geometry.Point2D;

import java.util.LinkedList;
import java.util.List;

public class Room {
	private String name;
	private String password;
	private int minPlayer;

	private Arena arena;

	private boolean running = false;


	private List<PlayerSession> players = new LinkedList<PlayerSession>();

	/**
	 * Creates a room
	 * @param name
	 * @param password
	 */
	public Room(String name, String password, int minPlayer) {
		this.name = name;
		this.password = password;
		this.minPlayer = minPlayer;
	}

	public synchronized void addPlayer(PlayerSession p) {
		players.add(p);
	}

	public synchronized void removePlayer(PlayerSession p) {
		players.remove(p);
	}

	public synchronized void start() {
		for (PlayerSession player : players) {
			player.setBomberman(new Bomberman(new Point(0, 0), Skin.SKIN1, null));
		}
		running = true;
	}

	public synchronized boolean isRunning() {
		return running;
	}

	/**
	 * Tries to launch the game
	 */
	public void update() {
		for (PlayerSession player : players) {
			if (! player.isReady()) {
				return;
			}
		}
		if (players.size() < minPlayer) {
			return;
		}

		start();
	}
}