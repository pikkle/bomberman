package ch.heigvd.bomberman.common.game.bombs;

import ch.heigvd.bomberman.common.game.Arena;
import ch.heigvd.bomberman.common.util.Point;

/**
 * A {@link BasicBomb} factory which begin with a 3 countdown, 1 blastRange
 * and 1 maxBombs
 */
public class BasicBombFactory extends BombFactory {
	public BasicBombFactory(Arena arena) {
		super(3, 1, arena, 1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected BasicBomb generate(Point position) {
		return new BasicBomb(position, countdown, blastRange, arena);
	}
}