package ch.heigvd.bomberman.common.game.explosion;

import ch.heigvd.bomberman.common.game.Arena.Arena;
import ch.heigvd.bomberman.common.game.Point;

/**
 * Created by matthieu.villard on 31.05.2016.
 */
public class TopExplosion extends Explosion {

	public TopExplosion(Point position, Arena arena) {
		super(position, arena, "ch/heigvd/bomberman/client/img/explosion/explosionT.png");
	}
}