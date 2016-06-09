package ch.heigvd.bomberman.common.game;

import ch.heigvd.bomberman.common.game.Arena.Arena;

import javax.persistence.Entity;

/**
 * Created by matthieu.villard on 23.05.2016.
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

	public StartPoint() {}

	@Override
	public boolean isDestructible() {
		return true;
	}

	@Override
	public boolean isBlastAbsorber() {
		return false;
	}

	@Override
	public boolean isTraversable() {
		return false;
	}

	@Override
	public String getPath() {
		return "ch/heigvd/bomberman/client/img/skins/" + Skin.values()[0] + ".png";
	}
}
