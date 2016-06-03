package ch.heigvd.bomberman.common.game;


import ch.heigvd.bomberman.common.game.Arena.Arena;
import ch.heigvd.bomberman.common.game.bombs.BasicBombFactory;
import ch.heigvd.bomberman.common.game.bombs.Bomb;
import ch.heigvd.bomberman.common.game.bombs.BombFactory;
import ch.heigvd.bomberman.common.game.powerups.PowerUp;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * Represents a bomberman character in-game
 */
public class Bomberman extends Element {
    private BombFactory bombFactory = new BasicBombFactory(arena);
    private List<PowerUp> powerUps = new LinkedList<>();
    private Skin skin;

    /**
     * Constructs a Bomberman at a given position and a given skin
     *
     * @param position The position of the bomberman
     * @param skin     The skin of the bomberman
     */
    public Bomberman(Point position, Skin skin, Arena arena) {
        super(position, arena);
        this.skin = skin;
        arena.add(this);
    }

    public Skin getSkin(){
        return skin;
    }

    /**
     * Move the bomberman in the direction wanted
     *
     * @param direction the direction
     */
    public void move(Direction direction) {
        Point position = position();
        switch (direction) {
            case RIGHT:
                position = position.add(1, 0);
                break;
            case LEFT:
                position = position.add(-1, 0);
                break;
            case UP:
                position = position.add(0, -1);
                break;
            case DOWN:
                position = position.add(0, 1);
                break;
        }
        if (position.x() < arena.getWidth() && position.x() >= 0 && position.x() < arena.getHeight() && position.x() >= 0) {
            if(arena.isEmpty(position)){
                this.position = position;
            } else if(arena.getElements(position).stream().allMatch(element -> element instanceof PowerUp)){
                arena.getElements(position).forEach(element -> {
                    ((PowerUp)element).apply(this);
                    arena.remove(element);
                });
                this.position = position;
            }
        }
        if (arena.isEmpty(position) && position.x() < arena.getWidth() && position.x() >= 0 &&
                position.x() < arena.getHeight() && position.x() >= 0) {
            this.position = position;
        }
    }

    /**
     * Drop the bomb
     */
    public Optional<? extends Bomb> dropBomb() {
        return bombFactory.create(position);
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

    public void changeBombFactory(BombFactory bombFactory) {
        this.bombFactory = bombFactory;
    }

    public BombFactory getBombFactory() {
        return bombFactory;
    }

    @Override
    public void accept(ElementVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean isDestructible() {
        return true;
    }

    @Override
    public boolean isBlastAbsorber() {
        return false;
    }

    @Override
    public boolean isTraversable() {
        return true;
    }
}