package ch.heigvd.bomberman.common.game.bombs;

import ch.heigvd.bomberman.common.game.Arena;
import ch.heigvd.bomberman.common.game.Element;
import ch.heigvd.bomberman.common.util.Point;

import java.util.LinkedList;
import java.util.List;

/**
 * Projet : GEN_Projet
 * Créé le 12.05.2016.
 *
 * @author Adriano Ruberto
 */
public class BasicBomb extends Bomb {

	public BasicBomb(Point position, int countdown, int blastRange, Arena arena) {
		super(position, countdown, blastRange, arena);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Element> getElementsInRange() {
		return arena.elementsInRange(this, blastRange).reduce(new LinkedList<>(), (res, elems) -> {
			for (Element e : elems.first()) {
				if (e.isDestructible()) {
					res.add(e);
				}
				if (e.isBlastAbsorber()) {
					break;
				}
			}
			return res;
		}, (a, b) -> a);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getPath() {
		return "ch/heigvd/bomberman/common/game/img/bombs/Bomb.png";
	}
}