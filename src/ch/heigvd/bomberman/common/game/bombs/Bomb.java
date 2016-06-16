package ch.heigvd.bomberman.common.game.bombs;

import ch.heigvd.bomberman.common.game.Arena;
import ch.heigvd.bomberman.common.util.Direction;
import ch.heigvd.bomberman.common.game.Element;
import ch.heigvd.bomberman.common.util.Pair;
import ch.heigvd.bomberman.common.util.Point;
import ch.heigvd.bomberman.common.game.explosion.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * A bomb is a destructible element which explode after short time.
 * If a another bomb is in the range of the explosion, she's triggered too.
 */
public abstract class Bomb extends Element {
	private int countdown;
	protected int blastRange;

	public Bomb(Point position, int countdown, int blastRange, Arena arena) {
		super(position, arena);
		this.countdown = countdown;
		this.blastRange = blastRange;
	}

	/**
	 * Gets the countdown for the bomb
	 *
	 * @return the countdown
	 */
	public int countdown() {
		return countdown;
	}

	/**
	 * Decease by one the countdown
	 */
	public void decreaseCountdown() {
		countdown--;
	}

	/**
	 * Displays the explosion of the bomb
	 */
	public void showExplosion() {
		Function<Direction, Supplier<Point>> fakePositionSupplierFactory = d -> () -> {
			int x = 0;
			int y = 0;
			switch (d) {
				case RIGHT:
					x = 1;
					break;
				case LEFT:
					x = -1;
					break;
				case UP:
					y = -1;
					break;
				case DOWN:
					y = 1;
					break;
			}
			return new Point(this.x() + x * (blastRange + 1), this.y() + y * (blastRange + 1));
		};

		deleteAfter(new CentralExplosion(position, arena));

		arena.elementsInRange(this, blastRange)
		     .map(p -> Pair.of(p.first()
		                        .stream()
		                        .filter(Element::isBlastAbsorber)
		                        .findFirst()
		                        .map(Element::position)
		                        .orElseGet(fakePositionSupplierFactory.apply(p.second())),
		                       p.second()))
		     .flatMap(p -> {
			     Point target = p.first();
			     BiFunction<Point, Arena, Explosion> endExplosionFactory;
			     BiFunction<Point, Arena, Explosion> explosionFactory;
			     switch (p.second()) {
				     case RIGHT:
					     explosionFactory = HorizontalExplosion::new;
					     endExplosionFactory = RightExplosion::new;
					     break;
				     case LEFT:
					     explosionFactory = HorizontalExplosion::new;
					     endExplosionFactory = LeftExplosion::new;
					     break;
				     case UP:
					     explosionFactory = VerticalExplosion::new;
					     endExplosionFactory = TopExplosion::new;
					     break;
				     case DOWN:
					     explosionFactory = VerticalExplosion::new;
					     endExplosionFactory = BottomExplosion::new;
					     break;
				     default:
					     throw new IllegalArgumentException();
			     }

			     int dx = target.x() - x();
			     int dy = target.y() - y();
			     int d = Math.max(Math.abs(dx + dy), 1);

			     return Stream.iterate(1, i -> i + 1)
			                  .limit(d - 1)
			                  .map(i -> Pair.of(i, new Point(x() + i * dx / d,
			                                                 y() + i * dy / d)))
			                  .map(pair -> (pair.first() == d - 1 ? endExplosionFactory :
					                  explosionFactory).apply(pair.second(), arena));
		     })
		     .forEach(this::deleteAfter);
	}

	/**
	 * Delete the element after a short time
	 *
	 * @param e the element to destroy
	 */
	private void deleteAfter(Element e) {
		new Timeline(new KeyFrame(Duration.millis(100), ae -> e.delete())).play();
	}

	/**
	 * @return the elements in range of the blastRange
	 */
	public abstract List<Element> getElementsInRange();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isDestructible() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isBlastAbsorber() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isTraversable() {
		return false;
	}

	/**
	 * Show the explosion
	 * {@inheritDoc}
	 */
	@Override
	public void delete() {
		super.delete();
		getElementsInRange().stream().filter(element -> !(element instanceof Explosion)).forEach(Element::delete);
	}
}