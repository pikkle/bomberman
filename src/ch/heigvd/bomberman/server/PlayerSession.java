package ch.heigvd.bomberman.server;

import ch.heigvd.bomberman.common.game.Bomberman;
import ch.heigvd.bomberman.common.game.Player;

public class PlayerSession {
	private Bomberman bomberman;
	private Room room;
	private boolean ready;
	private Player player;

	public PlayerSession(Player player, Room room){
		this.player = player;
		this.room = room;
	}

	public Bomberman getBomberman() {
		return bomberman;
	}

	public void setBomberman(Bomberman bomberman) {
		this.bomberman = bomberman;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public Player getPlayer() {
		return player;
	}

	public boolean isReady() {
		return ready;
	}

	public void ready(boolean state) {
		ready = state;
		room.update();
	}
}