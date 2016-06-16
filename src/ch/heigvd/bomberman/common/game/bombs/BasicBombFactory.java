package ch.heigvd.bomberman.common.game.bombs;

import ch.heigvd.bomberman.common.game.Arena.Arena;
import ch.heigvd.bomberman.common.util.Point;

/**
 * Created by Adriano on 12.05.2016.
 */
public class BasicBombFactory extends BombFactory {
	public BasicBombFactory(Arena arena) {
		super(3, 1, arena, 1);
	}

	@Override
	protected BasicBomb generate(Point position) {
		return new BasicBomb(position, countdown, blastRange, arena);
	}
}