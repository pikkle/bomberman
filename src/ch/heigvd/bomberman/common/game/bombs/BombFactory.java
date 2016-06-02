package ch.heigvd.bomberman.common.game.bombs;

import ch.heigvd.bomberman.common.game.Arena.Arena;
import ch.heigvd.bomberman.common.game.Point;

import java.io.Serializable;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;

/**
 * Created by Adriano on 12.05.2016.
 */
public abstract class BombFactory implements Observer, Serializable {
	protected int countdown;
	protected int blastRange;
	protected int nbBomb;
	protected Arena arena;

	public BombFactory(int countdown, int blastRange, Arena arena, int maxBomb) {
		this.countdown = countdown;
		this.blastRange = blastRange;
		this.nbBomb = maxBomb;
		this.arena = arena;
	}

	/**
	 * Create the bomb at the position
	 *
	 * @param position the position
	 * @return the new bomb
	 */
	public abstract Optional<? extends Bomb> create(Point position);


	@Override
	public void update(Observable o, Object arg) {
		nbBomb++;
	}

	public void addCountdown(int n) {
		countdown += n;
	}

	public void addRange(int n) {
		blastRange += n;
	}

	public void addBomb(int n) {
		nbBomb += n;
	}
}