package ch.heigvd.bomberman.common.game.explosion;

import ch.heigvd.bomberman.common.game.Arena;
import ch.heigvd.bomberman.common.game.util.Point;

/**
 * The right-ended explosion.
 */
public class RightExplosion extends Explosion {

	public RightExplosion(Point position, Arena arena) {
		super(position, arena);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getPath() {
		return "ch/heigvd/bomberman/common/game/img/explosion/explosionR.png";
	}
}
