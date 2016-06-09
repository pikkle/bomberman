package ch.heigvd.bomberman.common.game.powerups;

import ch.heigvd.bomberman.common.game.Arena.Arena;
import ch.heigvd.bomberman.common.game.Bomberman;
import ch.heigvd.bomberman.common.game.Point;

/**
 * Created by Adriano on 09.06.2016.
 */
public class FullFirePowerUp extends PowerUp {

	public FullFirePowerUp(Point position, Arena arena) {
		super(position, arena);
	}

	protected FullFirePowerUp() {
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
		return "ch/heigvd/bomberman/client/img/powerups/FireUp.png";
	}
}
