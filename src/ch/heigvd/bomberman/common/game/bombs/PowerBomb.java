package ch.heigvd.bomberman.common.game.bombs;

import ch.heigvd.bomberman.common.game.Arena;
import ch.heigvd.bomberman.common.util.Point;

/**
 * Created by Adriano on 09.06.2016.
 */
public class PowerBomb extends BasicBomb {

	public PowerBomb(Point position, int countdown, Arena arena) {
		super(position, countdown, Math.max(arena.width(), arena.height()), arena);
	}

	@Override
	public String getPath() {
		return "ch/heigvd/bomberman/common/game/img/bombs/PowerBomb.png";
	}
}
