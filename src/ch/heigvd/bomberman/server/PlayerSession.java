package ch.heigvd.bomberman.server;

import ch.heigvd.bomberman.common.game.Bomberman;

public class PlayerSession {
	private Bomberman bomberman;
	private Room room;
	private boolean ready;

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

	public boolean isReady() {
		return ready;
	}

	public void ready(boolean state) {
		ready = state;
		room.update();
	}
}
