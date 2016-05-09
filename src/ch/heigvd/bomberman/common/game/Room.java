package ch.heigvd.bomberman.common.game;

/**
 * Created by matthieu.villard on 09.05.2016.
 */
public class Room
{
    private String name;
    private int minPlayer;
    private Arena arena;

    public Room(String name, Arena arena){
        this.name = name;
        this.arena = arena;
        minPlayer = 2;
    }

    public Room(String name, Arena arena, int minPlayer){
        this.name = name;
        this.arena = arena;
        this.minPlayer = minPlayer;
    }

    public Arena getArena(){
        return arena;
    }
}
