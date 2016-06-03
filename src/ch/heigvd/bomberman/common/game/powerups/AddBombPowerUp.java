package ch.heigvd.bomberman.common.game.powerups;

import ch.heigvd.bomberman.common.game.Arena.Arena;
import ch.heigvd.bomberman.common.game.Bomberman;
import ch.heigvd.bomberman.common.game.Point;

import javax.persistence.Entity;

/**
 * Created by Adriano on 12.05.2016.
 */

@Entity
public class AddBombPowerUp extends PowerUp {

	public AddBombPowerUp(Point position, Arena arena) {
		super(position, arena, "ch/heigvd/bomberman/client/img/powerups/addBomb.png");
	}

	protected AddBombPowerUp() {}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void apply(Bomberman bomberman) {
		bomberman.getBombFactory().addBomb(1);
	}
}