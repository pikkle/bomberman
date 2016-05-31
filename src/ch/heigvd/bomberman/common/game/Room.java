package ch.heigvd.bomberman.common.game;

import ch.heigvd.bomberman.common.game.Arena.Arena;

import java.io.Serializable;

public class Room implements Serializable {
    private String name;
    private boolean isPrivate;
    private int minPlayer;
    private int playerNumber = 0;
    private Arena arena;

    public Room(String name, boolean isPrivate, int minPlayer, Arena arena) {
        this.name = name;
        this.isPrivate = isPrivate;
        this.minPlayer = minPlayer;
        this.arena = arena;
    }

    public String getName() {
        return name;
    }

    public boolean isPrivate(){
        return isPrivate;
    }

    public int getMinPlayer(){
        return minPlayer;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public void addPlayer(){
        playerNumber++;
    }

    public void removePlayer(){
        playerNumber = Math.max(0, playerNumber - 1);
    }

    public Arena getArena() {
        return arena;
    }
}