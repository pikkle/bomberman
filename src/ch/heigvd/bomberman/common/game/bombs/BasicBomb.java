package ch.heigvd.bomberman.common.game.bombs;

import ch.heigvd.bomberman.common.game.Arena.Arena;
import ch.heigvd.bomberman.common.game.Element;
import javafx.geometry.Point2D;

/**
 * Projet : GEN_Projet
 * Créé le 12.05.2016.
 *
 * @author Adriano Ruberto
 */
public class BasicBomb extends Bomb {

	public BasicBomb(Point2D position, int countdown, int blastRange, Arena arena) {
		super(position, countdown, blastRange, arena);
	}

	@Override
	public boolean isInRange(Element e) {
		return false;
	}
}
