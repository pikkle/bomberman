package ch.heigvd.bomberman.server;

import ch.heigvd.bomberman.common.game.Arena.Arena;
import ch.heigvd.bomberman.common.game.Bomberman;
import ch.heigvd.bomberman.common.game.Skin;
import ch.heigvd.bomberman.common.game.StartPoint;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class Room {
	private String name;
	private String password;
	private int minPlayer;
	private ch.heigvd.bomberman.common.game.Room clientRoom;

	private Arena arena;

	private boolean running = false;

	private List<PlayerSession> players = new LinkedList<PlayerSession>();

	/**
	 * Creates a room
	 * @param name
	 * @param password
	 */
	public Room(String name, String password, int minPlayer, Arena arena) {
		this.name = name;
		this.password = password;
		this.minPlayer = minPlayer;
		this.arena = arena;
		clientRoom = new ch.heigvd.bomberman.common.game.Room(name, password != null && !password.isEmpty(), minPlayer, arena);
	}

	public synchronized void addPlayer(PlayerSession p) throws Exception {
		if(!players.contains(p)) {
			if(players.size() >= 4)
				throw new Exception("Already 4 players !");
			players.add(p);
			clientRoom.setPlayerNumber(clientRoom.getPlayerNumber() + 1);
		}
	}

	public synchronized void removePlayer(PlayerSession p) {
		players.remove(p);
		clientRoom.setPlayerNumber(clientRoom.getPlayerNumber() - 1);
	}

	public synchronized List<PlayerSession> getPlayers() {
		return players;
	}

	public ch.heigvd.bomberman.common.game.Room getClientRoom(){
		return clientRoom;
	}

	public int getPlayerNumber() {
		return players.size();
	}

	public int getMinPlayer(){
		return minPlayer;
	}

	public String getName() {
		return name;
	}

	public String getPassword() {
		return password;
	}

	public Arena getArena() {
		return arena;
	}

	public synchronized void start() {
		for (PlayerSession player : players) {
			Optional<StartPoint> start = clientRoom.getArena().getStartPoints().stream().findFirst();
			if(start.isPresent()) {
				player.setBomberman(new Bomberman(start.get().position(), Skin.values()[players.indexOf(player)], clientRoom.getArena()));
			}
			else{
				return;
			}
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