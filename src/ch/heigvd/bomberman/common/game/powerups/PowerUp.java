package ch.heigvd.bomberman.common.game.powerups;

import ch.heigvd.bomberman.common.game.Arena.Arena;
import ch.heigvd.bomberman.common.game.Bomberman;
import ch.heigvd.bomberman.common.game.DestructibleElement;
import javafx.geometry.Point2D;

public abstract class PowerUp extends DestructibleElement {

	public PowerUp(Point2D position, Arena arena) {
		super(position, arena);
	}

	public abstract void apply(Bomberman bomberman);
}
