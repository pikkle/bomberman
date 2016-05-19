package ch.heigvd.bomberman.common.game.bombs;

import ch.heigvd.bomberman.common.game.Arena.Arena;
import ch.heigvd.bomberman.common.game.Point;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by Adriano on 12.05.2016.
 */
public abstract class BombFactory implements Observer {
	protected int countdown;
	protected int blastRange;
	protected Arena arena;
	protected int nbBomb;

	public BombFactory(int countdown, int blastRange, Arena arena, int maxBomb) {
		this.countdown = countdown;
		this.blastRange = blastRange;
		this.arena = arena;
		this.nbBomb = maxBomb;
	}

	/**
	 * Create the bomb at the position
	 *
	 * @param position the position
	 * @return the new bomb
	 */
	public abstract Bomb create(Point position);


	@Override
	public void update(Observable o, Object arg) {
		nbBomb++;
	}
}