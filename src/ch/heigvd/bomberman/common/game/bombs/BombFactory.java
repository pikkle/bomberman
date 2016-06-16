package ch.heigvd.bomberman.common.game.bombs;

import ch.heigvd.bomberman.common.game.Arena;
import ch.heigvd.bomberman.common.game.util.Point;

import java.io.Serializable;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;

/**
 * A {@link Bomb} factory. Every subclasses needs to implement generate,
 * which creates the actual bomb of the factory.
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

	public BombFactory(BombFactory bf) {
		this(bf.countdown, bf.blastRange, bf.arena, bf.nbBomb);
	}

	/**
	 * Creates a bomb at the position. Can be empty if there isn't any bomb
	 * left.
	 *
	 * @param position the position
	 * @return the new bomb
	 */
	public Optional<? extends Bomb> create(Point position) {
		if (nbBomb <= 0)
			return Optional.empty();

		Bomb b = generate(position);

		b.addObserver(this);
		nbBomb--;
		return Optional.of(b);
	}

	/**
	 * Generates a bomb at the given position
	 *
	 * @param position the position
	 * @return the bomb
	 */
	protected abstract Bomb generate(Point position);

	/**
	 * Called when a bomb explode, will increase the number of bomb.
	 */
	@Override
	public void update(Observable o, Object arg) {
		nbBomb++;
	}

	/**
	 * Adds n time to countdown.
	 *
	 * @param time the time to add
	 */
	public void addCountdown(int time) {
		if (countdown + time > 1)
			countdown += time;
	}

	/**
	 * Adds range to the bomb.
	 *
	 * @param range the range to add
	 */
	public void addRange(int range) {
		if (blastRange + range > 0)
			blastRange += range;
	}

	/**
	 * Adds n bombs to the max bombs.
	 *
	 * @param n the number of bombs to add
	 */
	public void addBomb(int n) {
		if (nbBomb + n > 0)
			nbBomb += n;
	}
}