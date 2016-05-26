package ch.heigvd.bomberman.common.game.Arena;

import ch.heigvd.bomberman.common.game.Box;
import ch.heigvd.bomberman.common.game.Point;
import ch.heigvd.bomberman.common.game.Wall;


/**
 * Created by matthieu.villard on 09.05.2016.
 */
public class SimpleArena extends Arena {
    public SimpleArena() throws Exception {
        super(15, 15);

        for (int i = 2; i < getHeight() - 2; i++) {
            new Wall(new Point(getWidth() / 2, i), this);
        }

        new Box(new Point(10, 3), this);
        new Box(new Point(3, 10), this);
    }
}