package ch.heigvd.bomberman.common.game.powerups;

import ch.heigvd.bomberman.common.game.Arena;
import ch.heigvd.bomberman.common.game.Bomberman;
import ch.heigvd.bomberman.common.util.Point;
import ch.heigvd.bomberman.common.game.bombs.RedBombFactory;

import javax.persistence.Entity;

/**
 * Created by Adriano on 09.06.2016.
 */
@Entity
public class RedBombPowerUp extends PowerUp {

	public RedBombPowerUp(Point position, Arena arena) {
		super(position, arena);
	}

	protected RedBombPowerUp() {
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
