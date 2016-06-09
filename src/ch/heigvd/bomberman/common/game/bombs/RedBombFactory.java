package ch.heigvd.bomberman.common.game.bombs;

import ch.heigvd.bomberman.common.game.Point;

/**
 * Created by Adriano on 09.06.2016.
 */
public class RedBombFactory extends BombFactory {

	public RedBombFactory(BombFactory bombFactory) {
		super(bombFactory);
	}

	@Override
	protected Bomb generate(Point position) {
		return new RedBomb(position, countdown, blastRange, arena);
	}
}
