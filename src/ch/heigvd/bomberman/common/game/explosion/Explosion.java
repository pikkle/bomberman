package ch.heigvd.bomberman.common.game.explosion;

import ch.heigvd.bomberman.common.game.Arena.Arena;
import ch.heigvd.bomberman.common.game.Element;
import ch.heigvd.bomberman.common.util.Point;

/**
 * Created by matthieu.villard on 31.05.2016.
 */
public abstract class Explosion extends Element {

    public Explosion(Point position, Arena arena) {
        super(position, arena);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isDestructible() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isBlastAbsorber() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isTraversable() {
        return true;
    }
}
