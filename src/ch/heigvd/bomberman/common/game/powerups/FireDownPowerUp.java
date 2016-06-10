package ch.heigvd.bomberman.common.game.powerups;

import ch.heigvd.bomberman.common.game.Arena.Arena;
import ch.heigvd.bomberman.common.game.Bomberman;
import ch.heigvd.bomberman.common.game.Point;

/**
 * Created by Adriano on 09.06.2016.
 */
public class FireDownPowerUp extends PowerUp {

	public FireDownPowerUp(Point position, Arena arena) {
		super(position, arena);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void apply(Bomberman bomberman) {
		bomberman.bombFactory().addRange(-1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getPath() {
		return "ch/heigvd/bomberman/client/img/powerups/FireDown.png";
	}
}
