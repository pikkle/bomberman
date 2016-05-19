package ch.heigvd.bomberman.common.game.bombs;

import ch.heigvd.bomberman.common.game.Arena.Arena;
import ch.heigvd.bomberman.common.game.Element;
import ch.heigvd.bomberman.common.game.ElementVisitor;
import ch.heigvd.bomberman.common.game.Point;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Projet : GEN_Projet
 * Créé le 12.05.2016.
 *
 * @author Adriano Ruberto
 */
public class BasicBomb extends Bomb {

	public BasicBomb(Point position, int countdown, int blastRange, Arena arena) {
		super(position, countdown, blastRange, arena);
		arena.add(this);
	}

	@Override
	public List<Element> getElementsInRange() {
		int x = position.x();
		int y = position.y();

		List<Element> up = new ArrayList<>(),
				left = new ArrayList<>(),
				right = new ArrayList<>(),
				down = new ArrayList<>();

		Function<Element, Double> distanceFromBomb = e -> Math.pow(e.x() - x, 2) +
				Math.pow(e.y() - y, 2);

		arena.getElements()
				.stream()
				.filter(e -> (e.x() == x || e.y() == y) && e != this)
				.filter(e -> {
					double pos = (e.x() == x) ? e.y() : e.x();
					double ref = (e.x() == x) ? y : x;
					return Math.abs(ref - pos) <= blastRange;
				})
				.sorted((a, b) -> (int) (distanceFromBomb.apply(a) - distanceFromBomb.apply(b)))
				.forEach(e -> {
					double ex = e.x();
					double ey = e.y();
					((ex == x) ? (ey >= y ? down : up) : (ex > x ? right : left)).add(e);
				});

		return Stream.of(up, left, down, right).reduce(new LinkedList<>(), (res, elems) -> {
			for (Element e : elems) {
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

	@Override
	public void accept(ElementVisitor visitor) {
		visitor.visit(this);
	}
}