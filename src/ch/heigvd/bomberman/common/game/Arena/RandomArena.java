package ch.heigvd.bomberman.common.game.Arena;

import ch.heigvd.bomberman.common.game.Point;
import ch.heigvd.bomberman.common.game.Wall;

import java.util.Random;

/**
 * Created by matthieu.villard on 09.05.2016.
 */
public class RandomArena extends Arena {
    public RandomArena() throws Exception {
        super(15, 15);

        for (int i = 0; i < getWidth(); i++) {
            new Wall(new Point(i, 0), this);
            new Wall(new Point(i, getHeight() - 1), this);
        }

        for (int i = 1; i < getHeight() - 1; i++) {
            new Wall(new Point(0, i), this);
            new Wall(new Point(getWidth() - 1, i), this);
        }

        for (int i = 0; i < 10; ) {
            Random rand = new Random();
            Point position = new Point(rand.nextInt(getWidth()), rand.nextInt(getHeight()));
            if (isEmpty(position)) {
                new Wall(position, this);
                i++;
            }
        }
    }
}