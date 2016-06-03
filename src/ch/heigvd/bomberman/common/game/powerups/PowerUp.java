package ch.heigvd.bomberman.common.game.powerups;

import ch.heigvd.bomberman.common.game.Arena.Arena;
import ch.heigvd.bomberman.common.game.Bomberman;
import ch.heigvd.bomberman.common.game.Element;
import ch.heigvd.bomberman.common.game.Point;

import javax.persistence.Entity;

@Entity
public abstract class PowerUp extends Element {

	public PowerUp(Point position, Arena arena, String path) {
		super(position, arena, path);
	}

	protected PowerUp() {}

	/**
	 * Apply the power-up on a bomberman.
	 *
	 * @param bomberman the bomberman on which apply the power-up
	 */
	public abstract void apply(Bomberman bomberman);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isDestructible() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isBlastAbsorber() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isTraversable() {
		return true;
	}
}
