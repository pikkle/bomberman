package ch.heigvd.bomberman.common.game;

import ch.heigvd.bomberman.common.game.Arena.Arena;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class Room implements Serializable {
	private String name;
	private boolean isPrivate;
	private int minPlayer;
	private List<String> players = new LinkedList<>();
	private Arena arena;
	private boolean inRoom;

	public Room(String name, boolean isPrivate, int minPlayer, Arena arena, boolean inRoom) {
		this.name = name;
		this.isPrivate = isPrivate;
		this.minPlayer = minPlayer;
		this.arena = arena;
		this.inRoom = inRoom;
	}

	public Room(String name, boolean isPrivate, int minPlayer, List<String> players, Arena arena, boolean inRoom) {
		this(name, isPrivate, minPlayer, arena, inRoom);
		this.players = players;
	}

	/**
	 * Gets the name of the room.
	 *
	 * @return the name of the room
	 */
	public String getName() {
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
	public int getMinPlayer() {
		return minPlayer;
	}

	/**
	 * TODO
	 *
	 * @return
	 */
	public int getPlayerNumber(){
		return players.size();
	}

	/**
	 * TODO
	 */
	public List<String> getPlayers() {
		return players;
	}

	/**
	 * TODO
	 */
	public void addPlayer(String name){
		players.add(name);
	}

	/**
	 * TODO
	 */
	public void removePlayer(String name){
		players.remove(name);
	}

	/**
	 * Gets the arena for the room.
	 *
	 * @return gets the arena
	 */
	public Arena getArena() {
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