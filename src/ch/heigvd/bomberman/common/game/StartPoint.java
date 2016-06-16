package ch.heigvd.bomberman.common.game;

import ch.heigvd.bomberman.common.util.Point;

import javax.persistence.Entity;

/**
 * A StartPoint is where a bomberman can begin a game in the arena.
 */
@Entity
public class StartPoint extends Element {

	/**
	 * Constructs a Bomberman at a given position and a given skin
	 *
	 * @param position The position of the bomberman
	 */
	public StartPoint(Point position, Arena arena) {
		super(position, arena);
	}

	/**
	 * Constructor for Hibernate
	 */
	public StartPoint() {
	}

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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getPath() {
		return "ch/heigvd/bomberman/common/game/img/skins/" + Skin.values()[0] + ".png";
	}
}
