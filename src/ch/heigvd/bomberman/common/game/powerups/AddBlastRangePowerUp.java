package ch.heigvd.bomberman.common.game.powerups;

import ch.heigvd.bomberman.common.game.Arena.Arena;
import ch.heigvd.bomberman.common.game.Bomberman;
import ch.heigvd.bomberman.common.game.Point;

/**
 * Projet : GEN_Projet
 * Créé le 02.06.2016.
 *
 * @author Adriano Ruberto
 */
public class AddBlastRangePowerUp extends PowerUp {

	public AddBlastRangePowerUp(Point position, Arena arena) {
		super(position, arena, "ch/heigvd/bomberman/client/img/powerups/blastRange.png");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void apply(Bomberman bomberman) {
		bomberman.getBombFactory().addRange(1);
	}
}
