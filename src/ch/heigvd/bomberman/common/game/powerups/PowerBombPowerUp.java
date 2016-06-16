package ch.heigvd.bomberman.common.game.powerups;

import ch.heigvd.bomberman.common.game.Arena.Arena;
import ch.heigvd.bomberman.common.game.Bomberman;
import ch.heigvd.bomberman.common.util.Point;
import ch.heigvd.bomberman.common.game.bombs.PowerBombFactory;

import javax.persistence.Entity;

/**
 * Created by Adriano on 09.06.2016.
 */

@Entity
public class PowerBombPowerUp extends PowerUp {

	public PowerBombPowerUp(Point position, Arena arena) {
		super(position, arena);
	}

	protected PowerBombPowerUp() {
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
