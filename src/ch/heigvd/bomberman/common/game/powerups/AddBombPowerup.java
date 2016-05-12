package ch.heigvd.bomberman.common.game.powerups;

import ch.heigvd.bomberman.common.game.Bomberman;
import javafx.geometry.Point2D;

/**
 * Created by Adriano on 12.05.2016.
 */
public class AddBombPowerUp extends PowerUp {
	public AddBombPowerUp(Point2D position) {
		super(position);
	}

	@Override
	public void apply(Bomberman bomberman) {
		bomberman.addMaxBomb(1);
	}
}
