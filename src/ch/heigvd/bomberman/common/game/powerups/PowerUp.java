package ch.heigvd.bomberman.common.game.powerups;

import ch.heigvd.bomberman.common.game.Arena.Arena;
import ch.heigvd.bomberman.common.game.Bomberman;
import ch.heigvd.bomberman.common.game.Element;
import ch.heigvd.bomberman.common.game.Point;

public abstract class PowerUp extends Element {

	public PowerUp(Point position, Arena arena) {
		super(position, arena);
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
