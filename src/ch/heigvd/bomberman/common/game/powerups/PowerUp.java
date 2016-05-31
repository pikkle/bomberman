package ch.heigvd.bomberman.common.game.powerups;

import ch.heigvd.bomberman.common.game.Arena.Arena;
import ch.heigvd.bomberman.common.game.Bomberman;
import ch.heigvd.bomberman.common.game.Element;
import ch.heigvd.bomberman.common.game.Point;

import javax.persistence.Entity;

@Entity
public abstract class PowerUp extends Element {

	public PowerUp() {
		super();
	}

	public PowerUp(Point position, Arena arena) {
		super(position, null);
		arena.add(this);
	}

	public abstract void apply(Bomberman bomberman);

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
		return true;
	}
}
