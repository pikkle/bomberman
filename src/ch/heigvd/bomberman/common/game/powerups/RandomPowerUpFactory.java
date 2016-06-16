package ch.heigvd.bomberman.common.game.powerups;

import ch.heigvd.bomberman.common.game.Arena.Arena;
import ch.heigvd.bomberman.common.game.Point;

import java.util.Optional;
import java.util.Random;

/**
 * Projet : GEN_Projet
 * Créé le 16.06.2016.
 *
 * @author Adriano Ruberto
 */
public class RandomPowerUpFactory {

	private static Random rand = new Random();

	/**
	 * Generates a random power up.
	 *
	 * @param position the position of the power up
	 * @param arena    the arena where generate the power up
	 * @return a random power up
	 */
	public static Optional<PowerUp> generate(Point position, Arena arena) {
		int gaps = 25;
		int p = rand.nextInt(gaps * 10);

		if (p < gaps) {
			return Optional.of(new BombDownPowerUp(position, arena));
		} else if (p < gaps * 2) {
			return Optional.of(new BombUpPowerUp(position, arena));
		} else if (p < gaps * 3) {
			return Optional.of(new FireDownPowerUp(position, arena));
		} else if (p < gaps * 4) {
			return Optional.of(new FireUpPowerUp(position, arena));
		} else if (p < gaps * 5) {
			return Optional.of(new FullFirePowerUp(position, arena));
		} else if (p < gaps * 6) {
			return Optional.of(new PowerBombPowerUp(position, arena));
		} else if (p < gaps * 7) {
			return Optional.of(new RedBombPowerUp(position, arena));
		} else {
			return Optional.empty();
		}
	}
}
