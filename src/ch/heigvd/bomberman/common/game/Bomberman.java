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
    private Arena arena;
    private Bomb bomb;
    private int maxBombs = 1;
    private List<PowerUp> powerUps = new LinkedList<>();

    /**
     * Constructs a Bomberman at a given position and a given skin
     *
     * @param position The position of the bomberman
     * @param skin     The skin of the bomberman
     */
    public Bomberman(Point position, Skin skin, Arena arena){
        super(position, new ImageView(skin.getImage()));
        this.arena = arena;
    }

    /**
     * Move the bomberman in the direction wanted
     *
     * @param direction the direction
     */
    public void move(Direction direction) { // TODO Add hitbox collision detection
        Point position = (Point)getPosition().clone();
        switch (direction) {
            case RIGHT:
                position.move(position.x + 1, position.y);
                break;
            case LEFT:
                position.move(position.x - 1, position.y);
                break;
            case UP:
                position.move(position.x, position.y - 1);
                break;
            case DOWN:
                position.move(position.x, position.y + 1);
                break;
        }
        if(arena.isEmpty(position) && position.x < arena.getWidth() && position.x >= 0 && position.y < arena.getHeight() && position.y >= 0){
            this.position = position;
            setChanged();
            notifyObservers();
        }
    }

    /**
     * Drop the bomb
     */
    public void dropBomb() {
        // TODO add the bomb to the map
        for(int i = -1; i < 2; i++){
            for(int j = -1; j < 2; j++){
                Point position = (Point)getPosition().clone();
                position.x = position.x + i;
                position.y = position.y + j;
                if(i != j && arena.isEmpty(position) && position.x < arena.getWidth() && position.x >= 0 && position.y < arena.getHeight() && position.y >= 0){
                    bomb = new Bomb((Point)position.clone(), 10, 1);
                    arena.getElements().add(bomb);
                    return;
                }
            }
        }
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
