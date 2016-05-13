package ch.heigvd.bomberman.common.game.bombs;

import ch.heigvd.bomberman.common.game.Arena.Arena;
import javafx.geometry.Point2D;

/**
 * Created by Adriano on 12.05.2016.
 */
public class BasicBombFactory extends BombFactory {
	public BasicBombFactory(int countdown, int blastRange, Arena arena) {
		super(countdown, blastRange, arena);
	}

	@Override
	public BasicBomb create(Point2D position) {
		return new BasicBomb(position, countdown, blastRange, arena);
	}
}