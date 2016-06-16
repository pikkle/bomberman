package ch.heigvd.bomberman.common.game.explosion;

import ch.heigvd.bomberman.common.game.Arena;
import ch.heigvd.bomberman.common.util.Point;

/**
 * Created by matthieu.villard on 31.05.2016.
 */
public class TopExplosion extends Explosion {

	public TopExplosion(Point position, Arena arena) {
		super(position, arena);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getPath() {
		return "ch/heigvd/bomberman/common/game/img/explosion/explosionT.png";
	}
}
