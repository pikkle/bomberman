package ch.heigvd.bomberman.common.game.powerups;

import ch.heigvd.bomberman.common.game.Arena;
import ch.heigvd.bomberman.common.game.Bomberman;
import ch.heigvd.bomberman.common.game.bombs.PowerBombFactory;
import ch.heigvd.bomberman.common.game.util.Point;

/**
 * The power up who change the actual bomb of the bomberman to
 * {@link ch.heigvd.bomberman.common.game.bombs.PowerBomb}
 */
public class PowerBombPowerUp extends PowerUp {

	public PowerBombPowerUp() {

	}

	public PowerBombPowerUp(Point position, Arena arena) {
		super(position, arena);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void apply(Bomberman bomberman) {
		bomberman.changeBombFactory(new PowerBombFactory(bomberman.bombFactory()));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getPath() {
		return "ch/heigvd/bomberman/common/game/img/powerups/PowerBomb.png";
	}
}
