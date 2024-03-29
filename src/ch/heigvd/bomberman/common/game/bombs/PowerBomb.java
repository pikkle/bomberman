package ch.heigvd.bomberman.common.game.bombs;

import ch.heigvd.bomberman.common.game.Arena;
import ch.heigvd.bomberman.common.game.util.Point;

/**
 * Like a {@link BasicBomb} but with unlimited blast range
 */
public class PowerBomb extends BasicBomb {

	public PowerBomb(Point position, int countdown, Arena arena) {
		super(position, countdown, Math.max(arena.width(), arena.height()), arena);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getPath() {
		return "ch/heigvd/bomberman/common/game/img/bombs/PowerBomb.png";
	}
}
