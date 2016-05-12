package ch.heigvd.bomberman.common.game;


import ch.heigvd.bomberman.common.game.Arena.Arena;
import ch.heigvd.bomberman.common.game.bombs.BasicBomb;
import ch.heigvd.bomberman.common.game.bombs.Bomb;
import ch.heigvd.bomberman.common.game.powerups.PowerUp;
import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;

import java.util.LinkedList;
import java.util.List;

/**
 * Represents a bomberman character in-game
 */
public class Bomberman extends DestructibleElement {
	private Arena arena;
	private Bomb bomb;
	private int maxBombs = 1;
	private List<PowerUp> powerUps = new LinkedList<>();

	/**
	 * Constructs a Bomberman at a given position and a given skin
	 *
	 * @param position The position of the bomberman
	 * @param skin     The skin of the bomberman
	 */
	public Bomberman(Point2D position, Skin skin, Arena arena) {
		super(position, new ImageView(skin.getImage()));
		this.arena = arena;
	}

	/**
	 * Move the bomberman in the direction wanted
	 *
	 * @param direction the direction
	 */
	public void move(Direction direction) { // TODO Add hitbox collision detection
		Point2D position = getPosition();
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
		if (arena.isEmpty(position) && position.getX() < arena.getWidth() && position.getX() >= 0 &&
		    position.getY() < arena.getHeight() && position.getY() >= 0) {
			this.position = position;
			setChanged();
			notifyObservers();
		}
	}

	/**
	 * Drop the bomb
	 */
	public void dropBomb() {
		try {
			bomb = new BasicBomb(getPosition(), 10, 1, arena);
			arena.add(bomb);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Add a power to the bomberman and apply it.
	 *
	 * @param powerUp the power up
	 */
	public void givePowerup(PowerUp powerUp) {
		powerUps.add(powerUp);
		powerUp.apply(this);
	}

	/**
	 * @return the bomb of the bomberman
	 */
	public Bomb getBomb() {
		return bomb;
	}

}

