package ch.heigvd.bomberman.common.game.bombs;

import ch.heigvd.bomberman.common.game.Arena.Arena;
import javafx.geometry.Point2D;

/**
 * Created by Adriano on 12.05.2016.
 */
public class BasicBombFactory extends BombFactory {
	public BasicBombFactory(Arena arena) {
		super(3, 10, arena, 1);
	}

	@Override
	public BasicBomb create(Point2D position) {
		if (nbBomb <= 0) throw new RuntimeException("Can't drop a bomb");

		BasicBomb b = new BasicBomb(position, countdown, blastRange, arena);
		b.addObserver(this);
		nbBomb--;
		return b;
	}

}
