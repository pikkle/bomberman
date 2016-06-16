package ch.heigvd.bomberman.common.game.powerups;

import ch.heigvd.bomberman.common.game.Arena;
import ch.heigvd.bomberman.common.game.Bomberman;
import ch.heigvd.bomberman.common.util.Point;

/**
 * The power up who give infinite range.
 */
public class FullFirePowerUp extends PowerUp {

	public FullFirePowerUp(Point position, Arena arena) {
		super(position, arena);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void apply(Bomberman bomberman) {
		bomberman.bombFactory().addRange(Math.max(arena.width(), arena.height()));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getPath() {
		return "ch/heigvd/bomberman/common/game/img/powerups/FireUp.png";
	}
}
