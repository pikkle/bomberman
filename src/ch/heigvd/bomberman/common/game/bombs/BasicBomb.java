package ch.heigvd.bomberman.common.game.bombs;

import ch.heigvd.bomberman.common.game.Arena.Arena;
import ch.heigvd.bomberman.common.game.DestructibleElement;
import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;

import java.util.LinkedList;
import java.util.List;

/**
 * Projet : GEN_Projet
 * Créé le 12.05.2016.
 *
 * @author Adriano Ruberto
 */
public class BasicBomb extends Bomb {

	public BasicBomb(Point2D position, int countdown, int blastRange, Arena arena) {
		super(position, countdown, blastRange, arena,
		      new ImageView(new javafx.scene.image.Image("ch/heigvd/bomberman/client/img/bomb.png")));
	}

	@Override
	public List<DestructibleElement> getElementsInRange() {
		// Todo add element in the range
		return new LinkedList<>();
	}
}
