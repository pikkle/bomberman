package ch.heigvd.bomberman.common.game;


import ch.heigvd.bomberman.common.game.bombs.BasicBombFactory;
import ch.heigvd.bomberman.common.game.bombs.Bomb;
import ch.heigvd.bomberman.common.game.bombs.BombFactory;
import ch.heigvd.bomberman.common.game.powerups.PowerUp;
import ch.heigvd.bomberman.common.game.util.Direction;
import ch.heigvd.bomberman.common.game.util.Point;

import java.util.Optional;

/**
 * A bomberman character in-game. Can move and drop bombs.
 */
public class Bomberman extends Element {
	private BombFactory bombFactory = new BasicBombFactory(arena);
	private Skin skin;

	/**
	 * Constructs a Bomberman at a given position and a given skin
	 *
	 * @param position The position of the bomberman
	 * @param skin     The skin of the bomberman
	 */
	public Bomberman(Point position, Skin skin, Arena arena) {
		super(position, arena);
		this.skin = skin;
	}

	/**
	 * Moves the bomberman in the direction wanted
	 *
	 * @param direction the direction
	 */
	public void move(Direction direction) {
		Point position = position();
		switch (direction) {
			case RIGHT:
				position = position.add(1, 0);
				break;
			case LEFT:
				position = position.add(-1, 0);
				break;
			case UP:
				position = position.add(0, -1);
				break;
			case DOWN:
				position = position.add(0, 1);
				break;
		}

		if (arena.isEmpty(position)) {
			this.position = position;
			// if the bomberman is on power ups, he takes it
			final Point pos = position;
			arena.elements(PowerUp.class)
			     .stream()
			     .filter(p -> p.position.equals(pos))
			     .forEach(this::givePowerup);
		}
	}

	/**
	 * Drops a bomb, can be empty if the bomberman can't drop one.
	 * {@link BombFactory}
	 *
	 * @return the dropped bomb
	 */
	public Optional<? extends Bomb> dropBomb() {
		return bombFactory.create(position);
	}

	/**
	 * Adds a power to the bomberman and apply it.
	 *
	 * @param powerUp the power up
	 */
	public void givePowerup(PowerUp powerUp) {
		powerUp.apply(this);
		powerUp.delete();
	}

	/**
	 * Changes the bomb factory.
	 *
	 * @param bombFactory the bomb factory
	 */
	public void changeBombFactory(BombFactory bombFactory) {
		this.bombFactory = bombFactory;
	}

	/**
	 * Gets the bomb factory
	 *
	 * @return the bomb factory
	 */
	public BombFactory bombFactory() {
		return bombFactory;
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
		return skin.path();
	}
}