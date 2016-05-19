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