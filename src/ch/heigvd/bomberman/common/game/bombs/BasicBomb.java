package ch.heigvd.bomberman.common.game.bombs;

import ch.heigvd.bomberman.common.game.Arena.Arena;
import ch.heigvd.bomberman.common.game.DestructibleElement;
import ch.heigvd.bomberman.common.game.ElementVisitor;
import javafx.geometry.Point2D;

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
		super(position, countdown, blastRange, arena);
		arena.add(this);
	}

	@Override
	public List<DestructibleElement> getElementsInRange() {
		// Todo add element in the range
		return new LinkedList<>();
	}

	@Override
	public void accept(ElementVisitor visitor) {
		visitor.visit(this);
	}
}
