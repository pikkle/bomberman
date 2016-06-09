package ch.heigvd.bomberman.common.game.powerups;

import ch.heigvd.bomberman.common.game.Arena.Arena;
import ch.heigvd.bomberman.common.game.Bomberman;
import ch.heigvd.bomberman.common.game.Point;

import javax.persistence.Entity;

/**
 * Projet : GEN_Projet
 * Créé le 02.06.2016.
 *
 * @author Adriano Ruberto
 */

@Entity
public class AddBlastRangePowerUp extends PowerUp {

	public AddBlastRangePowerUp(Point position, Arena arena) {
		super(position, arena);
	}

	protected AddBlastRangePowerUp() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void apply(Bomberman bomberman) {
		bomberman.bombFactory().addRange(1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getPath() {
		return "ch/heigvd/bomberman/client/img/powerups/blastRange.png";
	}

}
