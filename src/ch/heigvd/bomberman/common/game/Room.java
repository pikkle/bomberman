package ch.heigvd.bomberman.common.game;

import ch.heigvd.bomberman.common.game.Arena.Arena;

import java.io.Serializable;

public class Room implements Serializable {
	private String name;
	private boolean isPrivate;
	private int minPlayer;
	private int playerNumber;
	private Arena arena;
	private boolean inRoom;

	public Room(String name, boolean isPrivate, int minPlayer, int playerNumber, Arena arena, boolean inRoom) {
		this.name = name;
		this.isPrivate = isPrivate;
		this.minPlayer = minPlayer;
		this.arena = arena;
		this.inRoom = inRoom;
		this.playerNumber = playerNumber;
	}

	/**
	 * Gets the name of the room.
	 *
	 * @return the name of the room
	 */
	public String name() {
		return name;
	}

	/**
	 * Gets if the room is private.
	 *
	 * @return true if the room is private, false otherwise
	 */
	public boolean isPrivate() {
		return isPrivate;
	}

	/**
	 * Gets the minimum player for this room.
	 *
	 * @return the minimum player for this room
	 */
	public int minPlayer() {
		return minPlayer;
	}

	/**
	 * TODO
	 *
	 * @return
	 */
	public int playerNumber() {
		return playerNumber;
	}

	/**
	 * TODO
	 */
	public void addPlayer() {
		playerNumber++;
	}

	/**
	 * TODO
	 */
	public void removePlayer() {
		playerNumber = Math.max(0, playerNumber - 1);
	}

	/**
	 * Gets the arena for the room.
	 *
	 * @return gets the arena
	 */
	public Arena arena() {
		return arena;
	}

	/**
	 * TODO
	 *
	 * @return
	 */
	public boolean isInRoom() {
		return inRoom;
	}
}