package ch.heigvd.bomberman.common.game.bombs;

import ch.heigvd.bomberman.common.util.Point;

/**
 * A simple red bomb factory which generate {@link RedBomb}
 */
public class RedBombFactory extends BombFactory {

	public RedBombFactory(BombFactory bombFactory) {
		super(bombFactory);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Bomb generate(Point position) {
		return new RedBomb(position, countdown, blastRange, arena);
	}
}
