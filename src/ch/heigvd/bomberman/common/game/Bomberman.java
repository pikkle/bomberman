package ch.heigvd.bomberman.common.game;


import ch.heigvd.bomberman.common.game.bombs.Bomb;
import ch.heigvd.bomberman.common.game.powerups.PowerUp;
import javafx.scene.image.ImageView;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents a bomberman character in-game
 */
public class Bomberman extends DestructibleElement {
    private Bomb bomb;
    private int maxBombs = 1;
    private List<PowerUp> powerUps = new LinkedList<>();;

    /**
     * Constructs a Bomberman at a given position and a given skin
     *
     * @param position The position of the bomberman
     * @param skin     The skin of the bomberman
     */
    public Bomberman(Point position, Skin skin){
        super(position, new ImageView(skin.getImage()));
    }

    public void move(Point position){
        this.position = position;
        setChanged();
        notifyObservers();
    }

    /**
     * Move the bomberman in the direction wanted
     *
     * @param direction the direction
     */
    public void move(Direction direction) { // TODO Add hitbox collision detection
        switch (direction) {
            case RIGHT:
                position.move(1, 0);
                break;
            case LEFT:
                position.move(-1, 0);
                break;
            case UP:
                position.move(0, -1);
                break;
            case DOWN:
                position.move(0, 1);
                break;
        }

    }

    /**
     * Drop the bomb
     */
    public void dropBomb() {
        // TODO add the bomb to the map

    }

    /**
     * Add a power to the bomberman and apply it.
     *
     * @param powerUp the power up
     */
    public void givePowerup(PowerUp powerUp) {
        powerUps.add(powerUp);
        powerUp.apply(this);
    }

    /**
     * @return the bomb of the bomberman
     */
    public Bomb getBomb() {
        return bomb;
    }

}
