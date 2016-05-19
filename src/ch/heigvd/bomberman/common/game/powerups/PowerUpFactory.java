package ch.heigvd.bomberman.common.game.powerups;

import ch.heigvd.bomberman.common.game.Arena.Arena;
import ch.heigvd.bomberman.common.game.Bomberman;
import ch.heigvd.bomberman.common.game.ElementVisitor;
import ch.heigvd.bomberman.common.game.Point;

import java.util.Random;

/**
 * Projet : GEN_Projet
 * Créé le 19.05.2016.
 *
 * @author Adriano Ruberto
 */
public class PowerUpFactory {
	Random rand = new Random();

	private Arena arena;


	PowerUp createRandomPowerup(Point position){
		if(rand.nextDouble() * 100 < 50){
			return createAddBomb(position);
		}
			return null;
		
	}

	PowerUp createAddBomb(Point position) {
		return new PowerUp(position, arena) {
			@Override
			public void apply(Bomberman bomberman) {
				bomberman.addMaxBomb(1);
			}

			@Override
			public void accept(ElementVisitor visitor) {

			}
		};
	}

}
