package ch.heigvd.bomberman.server;

import ch.heigvd.bomberman.common.game.Arena.Arena;
import ch.heigvd.bomberman.common.game.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class RoomSession {
	private Room room;
	private String password;
	private List<PlayerSession> players = new LinkedList<PlayerSession>();
	private boolean running = false;

	/**
	 * Creates a room
	 * @param name
	 * @param password
	 */
	public RoomSession(String name, String password, int minPlayer, Arena arena) {
		this.password = password;
		room = new Room(name, password != null && !password.isEmpty(), minPlayer, arena);
	}

	public Room getRoom(){
		return room;
	}

	public String getPassword() {
		return password;
	}

	public synchronized List<PlayerSession> getPlayers() {
		return players;
	}

	public synchronized void addPlayer(PlayerSession p) throws Exception {
		if(!players.contains(p)) {
			if(players.size() >= 4)
				throw new Exception("Already 4 players !");
			players.add(p);
			room.addPlayer();
		}
	}

	public synchronized void removePlayer(PlayerSession p) {
		if(players.contains(p)) {
			players.remove(p);
			room.removePlayer();
		}
	}

	public synchronized void start() {
		for (PlayerSession player : players) {
			Optional<StartPoint> start = room.getArena().getStartPoints().stream().findFirst();
			if(start.isPresent()) {
				room.getArena().remove(start.get());
				player.setBomberman(new Bomberman(start.get().position(), Skin.values()[players.indexOf(player)], room.getArena()));
			}
			else{
				return;
			}
		}
		room.getArena().getStartPoints().forEach(startPoint -> room.getArena().remove(startPoint));
		running = true;

		new Thread(new Runnable() {
			@Override
			public void run() {
				while (running){
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					room.getArena().getBombs().forEach(b -> {
						b.decreaseCountdown();
						if (b.getCountdown() <= 0) {
							b.explose();
						}
					});
				}
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
			if (! player.isReady()) {
				return;
			}
		}
		if (players.size() < room.getMinPlayer()) {
			return;
		}

		start();
	}
}