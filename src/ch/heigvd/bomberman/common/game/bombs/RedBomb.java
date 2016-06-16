package ch.heigvd.bomberman.common.game.bombs;

import ch.heigvd.bomberman.common.game.Arena;
import ch.heigvd.bomberman.common.game.Element;
import ch.heigvd.bomberman.common.game.util.Point;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Like a {@link Bomb} but this bomb can't be stopped by a wall.
 */
public class RedBomb extends Bomb {

	public RedBomb(Point position, int countdown, int blastRange, Arena arena) {
		super(position, countdown, blastRange, arena);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Element> getElementsInRange() {
		return arena.elementsInRange(this, blastRange).reduce(new LinkedList<>(), (res, elems) -> {
			res.addAll(elems.first().stream()
			                .filter(Element::isDestructible)
			                .collect(Collectors.toList()));
			return res;
		}, (a, b) -> a);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getPath() {
		return "ch/heigvd/bomberman/common/game/img/bombs/RedBomb.png";
	}
}
