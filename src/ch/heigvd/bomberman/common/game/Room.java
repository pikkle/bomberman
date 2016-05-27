package ch.heigvd.bomberman.common.game;

import ch.heigvd.bomberman.common.game.Arena.Arena;

import java.io.Serializable;

public class Room implements Serializable {
    private String name;
    private boolean isPrivate;
    private int minPlayer;
    private Arena arena;
    private int playerNumber = 0;

    public Room(String name, boolean isPrivate, int minPlayer, Arena arena) {
        this.name = name;
        this.isPrivate = isPrivate;
        this.minPlayer = minPlayer;
        this.arena = arena;
        this.playerNumber = playerNumber;
    }

    public String getName() {
        return name;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public void setPlayerNumber(int playerNumber) {
        this.playerNumber = playerNumber;
    }

    public Arena getArena() {
        return arena;
    }
}