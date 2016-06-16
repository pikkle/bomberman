package ch.heigvd.bomberman.common.game.powerups;

import ch.heigvd.bomberman.common.game.Arena;
import ch.heigvd.bomberman.common.game.Bomberman;
import ch.heigvd.bomberman.common.game.Element;
import ch.heigvd.bomberman.common.util.Point;

import javax.persistence.Entity;

/**
 * A power up is applied on a bomberman when he walk on.
 * Power up have different kind of changes, which are implemented by
 * subclasses.
 */
@Entity
public abstract class PowerUp extends Element {

	public PowerUp(Point position, Arena arena) {
		super(position, arena);
	}

	public PowerUp() {
	}

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
