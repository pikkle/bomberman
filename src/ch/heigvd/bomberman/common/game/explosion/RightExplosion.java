package ch.heigvd.bomberman.common.game.explosion;

import ch.heigvd.bomberman.common.game.Arena.Arena;
import ch.heigvd.bomberman.common.game.Point;

/**
 * Created by matthieu.villard on 31.05.2016.
 */
public class RightExplosion extends Explosion {

	public RightExplosion(Point position, Arena arena) {
		super(position, arena);
	}

	@Override
	public String getPath() {
		return "ch/heigvd/bomberman/client/img/explosion/explosionR.png";
	}
}
