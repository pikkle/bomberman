package ch.heigvd.bomberman.common.game;


import ch.heigvd.bomberman.common.game.powerups.PowerUp;
import javafx.geometry.Point2D;

import java.util.List;

/**
 * Represents a bomberman character in-game
 */
public class Bomberman {
    private Skin skin;
    private Point2D position;
    private Bomb bomb;
    private int maxBombs;
    private List<PowerUp> powerUps;

    /**
     * Constructs a Bomberman at a given position and a given skin
     * @param position The position of the bomberman
     * @param skin The skin of the bomberman
     */
    public Bomberman(Point2D position, Skin skin){

    }

    public void move(){

    }

    public void dropBomb(){

    }

    public void givePowerup(PowerUp powerUp){
        powerUp.apply(this);
    }

    public Bomb getBomb(){
        return bomb;
    }
}
