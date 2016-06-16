package ch.heigvd.bomberman.common.game;

import ch.heigvd.bomberman.common.game.util.Point;

import javax.persistence.Entity;

/**
 * An indestructible element which stop de blast and can't be traversed.
 * Commonly used all around the arena.
 */
@Entity
public class Wall extends Element {
	public Wall() {
	}

	public Wall(Point position, Arena arena) {
		super(position, arena);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isDestructible() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isBlastAbsorber() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isTraversable() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getPath() {
		return "ch/heigvd/bomberman/common/game/img/wall.png";
	}
}
