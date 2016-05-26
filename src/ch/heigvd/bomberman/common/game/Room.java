package ch.heigvd.bomberman.common.game;

import ch.heigvd.bomberman.common.game.Arena.Arena;

import java.util.LinkedList;
import java.util.List;

public class Room {
    private String name;
    private String password;
    private int minPlayer;
    private Arena arena;
    private boolean running = false;
    private List<Player> players = new LinkedList<Player>();

    public Room(String name, Arena arena, int minPlayer) {
        this.name = name;
        this.arena = arena;
        this.minPlayer = minPlayer;
    }

    /**
     * Creates a room
     *
     * @param name
     * @param arena
     * @param minPlayer
     * @param password
     */
    public Room(String name, Arena arena, int minPlayer, String password) {
        this.name = name;
        this.arena = arena;
        this.minPlayer = minPlayer;
        this.password = password;
    }

    public synchronized void addPlayer(Player p) {
        players.add(p);
    }

    public synchronized void removePlayer(Player p) {
        players.remove(p);
    }

    public String getName() {
        return name;
    }

    public int getPlayerNumber() {
        return players.size();
    }

    public Arena getArena() {
        return arena;
    }
}