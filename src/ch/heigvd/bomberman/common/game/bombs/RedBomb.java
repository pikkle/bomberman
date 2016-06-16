package ch.heigvd.bomberman.common.game.bombs;

import ch.heigvd.bomberman.common.game.Arena.Arena;
import ch.heigvd.bomberman.common.game.Element;
import ch.heigvd.bomberman.common.util.Point;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Adriano on 09.06.2016.
 */
public class RedBomb extends Bomb {

	public RedBomb(Point position, int countdown, int blastRange, Arena arena) {
		super(position, countdown, blastRange, arena);
	}

	@Override
	public List<Element> getElementsInRange() {
		return arena.elementsInRange(this, blastRange).reduce(new LinkedList<>(), (res, elems) -> {
			res.addAll(elems.first().stream()
			                .filter(Element::isDestructible)
			                .collect(Collectors.toList()));
			return res;
		}, (a, b) -> a);
	}

	@Override
	public String getPath() {
		return "ch/heigvd/bomberman/common/game/img/bombs/RedBomb.png";
	}
}
