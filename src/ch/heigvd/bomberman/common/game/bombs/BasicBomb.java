package ch.heigvd.bomberman.common.game.bombs;

import ch.heigvd.bomberman.common.game.Arena.Arena;
import ch.heigvd.bomberman.common.game.Element;
import ch.heigvd.bomberman.common.game.ElementVisitor;
import javafx.geometry.Point2D;

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

	public BasicBomb(Point2D position, int countdown, int blastRange, Arena arena) {
		super(position, countdown, blastRange, arena);
		arena.add(this);
	}

	@Override
	public List<Element> getElementsInRange() {
		int x = (int) position.getX();
		int y = (int) position.getY();

		List<Element> up = new ArrayList<>(),
				left = new ArrayList<>(),
				right = new ArrayList<>(),
				down = new ArrayList<>();

		Function<Element, Double> distanceFromBomb = e -> Math.pow(e.getPosition().getX() - x, 2) +
		                                                  Math.pow(e.getPosition().getY() - y, 2);

		arena.getElements()
		     .stream()
		     .filter(e -> (e.getPosition().getX() == x || e.getPosition().getY() == y) && e != this)
		     .filter(e -> {
			     double pos = (e.getPosition().getX() == x) ? e.getPosition().getY() : e.getPosition().getX();
			     double ref = (e.getPosition().getX() == x) ? y : x;
			     return Math.abs(ref - pos) <= blastRange;
		     })
		     .sorted((a, b) -> (int) (distanceFromBomb.apply(a) - distanceFromBomb.apply(b)))
		     .forEach(e -> {
			     double ex = e.getPosition().getX();
			     double ey = e.getPosition().getY();
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
