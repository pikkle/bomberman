package ch.heigvd.bomberman.common.game.explosion;

import ch.heigvd.bomberman.common.game.Arena.Arena;
import ch.heigvd.bomberman.common.game.ElementVisitor;
import ch.heigvd.bomberman.common.game.Point;

/**
 * Created by matthieu.villard on 31.05.2016.
 */
public class HorizontalExplosion extends Explosion {

    public HorizontalExplosion() {
        super();
    }

    public HorizontalExplosion(Point position, Arena arena) {
        super(position, arena);
        arena.add(this);
    }

    @Override
    public void accept(ElementVisitor visitor) {
        visitor.visit(this);
    }
}
