package ch.heigvd.bomberman.common.game;

import javafx.geometry.Point2D;

import java.util.Random;

/**
 * Created by matthieu.villard on 09.05.2016.
 */
public class RandomArena extends Arena
{
    public RandomArena() throws Exception {
        super(15, 15);

        for(int i = 0; i < getWidth(); i++){
            add(new Wall(new Point2D(i, 0)));
            add(new Wall(new Point2D(i, getHeight() - 1)));
        }

        for(int i = 1; i < getHeight() - 1; i++){
            add(new Wall(new Point2D(0, i)));
            add(new Wall(new Point2D(getWidth() - 1, i)));
        }

        for(int i = 0; i < 10; ){
            Random rand = new Random();
            Point2D position = new Point2D(rand.nextInt(getWidth()), rand.nextInt(getHeight()));
            if(isEmpty(position)) {
                add(new Wall(position));
                i++;
            }
        }
    }
}
