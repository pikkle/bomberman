package ch.heigvd.bomberman.common.game.powerups;

import ch.heigvd.bomberman.common.game.Bomberman;
import ch.heigvd.bomberman.common.game.DestructibleElement;
import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;

public abstract class PowerUp extends DestructibleElement {

	public PowerUp(Point2D position, ImageView renderView) {
		super(position, renderView);
	}

	public abstract void apply(Bomberman bomberman);
}
