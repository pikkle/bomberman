package ch.heigvd.bomberman.common.game.Arena;

import ch.heigvd.bomberman.common.game.Wall;
import javafx.geometry.Point2D;

import java.util.Random;

/**
 * Created by matthieu.villard on 09.05.2016.
 */
public class RandomArena extends Arena {
    public RandomArena() throws Exception {
        super(15, 15);

        for (int i = 0; i < getWidth(); i++) {
            add(new Wall(new Point2D(i, 0), this));
            add(new Wall(new Point2D(i, getHeight() - 1), this));
        }

        for (int i = 1; i < getHeight() - 1; i++) {
            add(new Wall(new Point2D(0, i), this));
            add(new Wall(new Point2D(getWidth() - 1, i), this));
        }

        for (int i = 0; i < 10; ) {
            Random rand = new Random();
            Point2D position = new Point2D(rand.nextInt(getWidth()), rand.nextInt(getHeight()));
            if (isEmpty(position)) {
                add(new Wall(position, this));
                i++;
            }
        }
    }
}