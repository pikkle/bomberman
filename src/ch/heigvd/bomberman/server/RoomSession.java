package ch.heigvd.bomberman.server;

import ch.heigvd.bomberman.common.game.Arena.Arena;
import ch.heigvd.bomberman.common.game.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class RoomSession {
	private String name;
	private Arena arena;
	private String password;
	private int minPlayer;
	private List<PlayerSession> players = new LinkedList<PlayerSession>();
	private boolean running = false;

	/**
	 * Creates a room
	 * @param name
	 * @param password
	 */
	public RoomSession(String name, String password, int minPlayer, Arena arena) {
		this.name = name;
		this.password = password;
		this.arena = arena;
		this.minPlayer = minPlayer;
	}

	public String getName(){
		return name;
	}

	public Arena getArena(){
		return arena;
	}

	public String getPassword() {
		return password;
	}

	public int getMinPlayer(){
		return minPlayer;
	}

	public synchronized List<PlayerSession> getPlayers() {
		return players;
	}

	public synchronized void addPlayer(PlayerSession p) throws Exception {
		if(!players.contains(p)) {
			if(players.size() >= 4)
				throw new Exception("Already 4 players !");
			players.add(p);
		}
	}

	public synchronized void removePlayer(PlayerSession p) {
		if(players.contains(p)) {
			players.remove(p);
		}
	}

	public synchronized void start() {
		for (PlayerSession player : players) {
			Optional<StartPoint> start = arena.getStartPoints().stream().findFirst();
			if(start.isPresent()) {
				arena.remove(start.get());
				player.setBomberman(new Bomberman(start.get().position(), Skin.values()[players.indexOf(player)], arena));
			}
			else{
				return;
			}
		}
		arena.getStartPoints().forEach(startPoint -> arena.remove(startPoint));
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
					arena.getBombs().forEach(b -> {
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
		if (players.size() < minPlayer) {
			return;
		}

		start();
	}
}