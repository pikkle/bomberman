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

    public String getName() {
        return name;
    }

    public boolean isPrivate(){
        return isPrivate;
    }

    public int getMinPlayer(){
        return minPlayer;
    }

    public int getPlayerNumber(){
        return players.size();
    }

    public List<String> getPlayers() {
        return players;
    }

    public void addPlayer(String name){
        players.add(name);
    }

    public void removePlayer(String name){
        players.remove(name);
    }

    public Arena getArena() {
        return arena;
    }

    public boolean isInRoom(){ return inRoom; }
}