package ch.heigvd.bomberman.common.game.bombs;

import ch.heigvd.bomberman.common.game.Arena.Arena;
import javafx.geometry.Point2D;

/**
 * Created by Adriano on 12.05.2016.
 */
public abstract class BombFactory {
	protected int countdown;
	protected int blastRange;
	protected Arena arena;

	public BombFactory(int countdown, int blastRange, Arena arena) {
		this.countdown = countdown;
		this.blastRange = blastRange;
		this.arena = arena;
	}

	/**
	 * Create the bomb at the position
	 *
	 * @param position the position
	 * @return the new bomb
	 */
	public abstract Bomb create(Point2D position);
}