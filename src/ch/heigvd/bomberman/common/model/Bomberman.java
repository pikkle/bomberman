package ch.heigvd.bomberman.common.model;


import ch.heigvd.bomberman.common.model.powerups.PowerUp;
import javafx.geometry.Point2D;

import java.util.List;

public class Bomberman {
    private Skin skin;
    private Point2D position;
    private Bomb bomb;
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
        powerUp.effect(this);
    }

    public Bomb getBomb(){
        return bomb;
    }
}
