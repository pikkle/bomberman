package ch.heigvd.bomberman.common.game.explosion;

import ch.heigvd.bomberman.common.game.Arena;
import ch.heigvd.bomberman.common.game.util.Point;

/**
 * The horizontal explosion.
 */
public class HorizontalExplosion extends Explosion {

	public HorizontalExplosion(Point position, Arena arena) {
		super(position, arena);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getPath() {
		return "ch/heigvd/bomberman/common/game/img/explosion/explosionH.png";
	}
}
