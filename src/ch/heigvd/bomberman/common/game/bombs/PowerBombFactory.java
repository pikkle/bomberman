package ch.heigvd.bomberman.common.game.bombs;

import ch.heigvd.bomberman.common.game.util.Point;

/**
 * Created by Adriano on 09.06.2016.
 */
public class PowerBombFactory extends BombFactory {

	public PowerBombFactory(BombFactory bf) {
		super(bf);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Bomb generate(Point position) {
		return new PowerBomb(position, countdown, arena);
	}
}
