package ch.heigvd.bomberman.common.game.powerups;

import ch.heigvd.bomberman.common.game.Bomberman;
import ch.heigvd.bomberman.common.game.ElementVisitor;

/**
 * Projet : GEN_Projet
 * Créé le 02.06.2016.
 *
 * @author Adriano Ruberto
 */
public class AddBlastRangePowerUp extends PowerUp {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void apply(Bomberman bomberman) {

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void accept(ElementVisitor visitor) {
		visitor.visit(this);
	}
}
