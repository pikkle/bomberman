package ch.heigvd.bomberman.common.game.powerups;

import ch.heigvd.bomberman.common.game.Arena.Arena;
import ch.heigvd.bomberman.common.game.Bomberman;
import ch.heigvd.bomberman.common.game.Point;

import javax.persistence.Entity;

/**
 * Created by Adriano on 12.05.2016.
 */

@Entity
public class BombUpPowerUp extends PowerUp {

	public BombUpPowerUp(Point position, Arena arena) {
		super(position, arena);
	}

	public BombUpPowerUp() {

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void apply(Bomberman bomberman) {
		bomberman.bombFactory().addBomb(1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getPath() {
		return "ch/heigvd/bomberman/common/game/img/powerups/BombUp.png";
	}
}