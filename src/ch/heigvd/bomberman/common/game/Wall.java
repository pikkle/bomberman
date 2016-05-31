package ch.heigvd.bomberman.common.game;

import ch.heigvd.bomberman.common.game.Arena.Arena;

import javax.persistence.Entity;
import java.net.URISyntaxException;

/**
 * Created by matthieu.villard on 09.05.2016.
 */
@Entity
public class Wall extends Element
{
    public Wall() {
        super();
    }

    public Wall(Point position, Arena arena) throws URISyntaxException {
        super(position, arena);
        arena.add(this);
    }

    @Override
    public void accept(ElementVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean isDestructible() {
        return false;
    }

    @Override
    public boolean isBlastAbsorber() {
        return true;
    }

    @Override
    public boolean isTraversable() {
        return false;
    }
}
