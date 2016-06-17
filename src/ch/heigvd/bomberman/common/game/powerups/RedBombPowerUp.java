package ch.heigvd.bomberman.common.game.powerups;

import ch.heigvd.bomberman.common.game.Arena;
import ch.heigvd.bomberman.common.game.Bomberman;
import ch.heigvd.bomberman.common.game.bombs.RedBombFactory;
import ch.heigvd.bomberman.common.game.util.Point;

/**
 * The power up who change the actual bomb of the bomberman to
 * {@link ch.heigvd.bomberman.common.game.bombs.RedBomb}
 */
public class RedBombPowerUp extends PowerUp {

	public RedBombPowerUp() {

	}

	public RedBombPowerUp(Point position, Arena arena) {
		super(position, arena);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void apply(Bomberman bomberman) {
		bomberman.changeBombFactory(new RedBombFactory(bomberman.bombFactory()));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getPath() {
		return "ch/heigvd/bomberman/common/game/img/powerups/RedBomb.png";
	}
}
