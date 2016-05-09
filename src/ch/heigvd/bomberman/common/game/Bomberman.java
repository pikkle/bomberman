package ch.heigvd.bomberman.common.game;


import ch.heigvd.bomberman.common.game.powerups.PowerUp;
import javafx.scene.image.ImageView;

import java.awt.*;
import java.util.List;

/**
 * Represents a bomberman character in-game
 */
public class Bomberman extends DestructibleElement {
    private Skin skin;
    private Bomb bomb;
    private int maxBombs;
    private List<PowerUp> powerUps;

    /**
     * Constructs a Bomberman at a given position and a given skin
     * @param position The position of the bomberman
     * @param skin The skin of the bomberman
     */
    public Bomberman(Point position, Skin skin){
        super(position, new ImageView(skin.getImage()));
    }

    public void move(Point position){
        this.position = position;
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
