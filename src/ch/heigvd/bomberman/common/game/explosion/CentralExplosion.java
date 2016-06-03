package ch.heigvd.bomberman.common.game.explosion;

import ch.heigvd.bomberman.common.game.Arena.Arena;
import ch.heigvd.bomberman.common.game.Point;

/**
 * Created by matthieu.villard on 31.05.2016.
 */
public class CentralExplosion extends Explosion {

	public CentralExplosion(Point position, Arena arena) {
		super(position, arena, "ch/heigvd/bomberman/client/img/explosion/explosionC.png");
	}
}
