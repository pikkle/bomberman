package ch.heigvd.bomberman.common.game.Arena;

import ch.heigvd.bomberman.common.game.Wall;
import javafx.geometry.Point2D;


/**
 * Created by matthieu.villard on 09.05.2016.
 */
public class SimpleArena extends Arena {
	public SimpleArena() throws Exception {
		super(15, 15);

		for (int i = 0; i < getWidth(); i++) {
			add(new Wall(new Point2D(i, 0), this));
			add(new Wall(new Point2D(i, getHeight() - 1), this));
		}

		for (int i = 1; i < getHeight() - 1; i++) {
			add(new Wall(new Point2D(0, i), this));
			add(new Wall(new Point2D(getWidth() - 1, i), this));
		}

		for (int i = 2; i < getHeight() - 2; i++) {
			add(new Wall(new Point2D(getWidth() / 2, i), this));
		}
	}
}
