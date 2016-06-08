package ch.heigvd.bomberman.common.game.bombs;

import ch.heigvd.bomberman.common.game.Arena.Arena;
import ch.heigvd.bomberman.common.game.Element;
import ch.heigvd.bomberman.common.game.Point;
import ch.heigvd.bomberman.common.game.explosion.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.util.LinkedList;
import java.util.List;


public abstract class Bomb extends Element {
	protected int countdown;
	protected int blastRange;

	public Bomb(Point position, int countdown, int blastRange, Arena arena) {
		super(position, arena);
		this.countdown = countdown;
		this.blastRange = blastRange;
	}

	public int getCountdown() {
		return countdown;
	}

	public void decreaseCountdown() {
		countdown--;
	}


	/**
	 * To call when the bomb explose, will display the explosion
	 */
	public void showExplosion() {
		int x = position.x();
		int y = position.y();

		boolean exploseRight = true;
		boolean exploseLeft = true;
		boolean exploseTop = true;
		boolean exploseBottom = true;

		List<Explosion> explosions = new LinkedList<>();

		if (!arena.getElements(position)
		          .stream()
		          .filter(element -> !element.isBlastAbsorber())
		          .findFirst()
		          .isPresent()) {
			explosions.add(new CentralExplosion(position, arena));

			for (int i = 1; i <= blastRange && (exploseRight || exploseLeft || exploseTop || exploseBottom); i++) {
				Point right = new Point(x + i, y);
				Point left = new Point(x - i, y);
				Point top = new Point(x, y - i);
				Point bottom = new Point(x, y + i);
				if (exploseRight && right.x() < arena.width()) {
					if (arena.getElements(right).stream().filter(Element::isBlastAbsorber).findFirst().isPresent())
						exploseRight = false;
					if (!arena.getElements(right)
					          .stream()
					          .filter(element -> !element.isDestructible())
					          .findFirst()
					          .isPresent()) {
						if (exploseRight && right.x() + 1 < arena.width() && i + 1 <= blastRange &&
						    !arena.getElements(new Point(right.x() + 1, right.y()))
						          .stream()
						          .filter(element -> !element.isDestructible())
						          .findFirst()
						          .isPresent()) explosions.add(new HorizontalExplosion(right, arena));
						else explosions.add(new RightExplosion(right, arena));
					}
				}
				if (exploseLeft && left.x() >= 0) {
					if (arena.getElements(left).stream().filter(Element::isBlastAbsorber).findFirst().isPresent())
						exploseLeft = false;
					if (exploseLeft && left.x() >= 0 && i + 1 <= blastRange &&
					    !arena.getElements(new Point(left.x() - 1, left.y()))
					          .stream()
					          .filter(element -> !element.isDestructible())
					          .findFirst()
					          .isPresent()) explosions.add(new HorizontalExplosion(left, arena));
					else explosions.add(new LeftExplosion(left, arena));
				}
				if (exploseTop && top.y() >= 0) {
					if (arena.getElements(top).stream().filter(Element::isBlastAbsorber).findFirst().isPresent())
						exploseTop = false;
					if (exploseTop && top.x() >= 0 && i + 1 <= blastRange &&
					    !arena.getElements(new Point(top.x(), top.y() - 1))
					          .stream()
					          .filter(element -> !element.isDestructible())
					          .findFirst()
					          .isPresent()) explosions.add(new VerticalExplosion(top, arena));
					else explosions.add(new TopExplosion(top, arena));
				}
				if (exploseBottom && bottom.y() < arena.height()) {
					if (arena.getElements(bottom).stream().filter(Element::isBlastAbsorber).findFirst().isPresent())
						exploseBottom = false;
					if (!arena.getElements(bottom)
					          .stream()
					          .filter(element -> !element.isDestructible())
					          .findFirst()
					          .isPresent()) {
						if (exploseBottom && bottom.x() + 1 < arena.height() && i + 1 <= blastRange &&
						    !arena.getElements(new Point(bottom.x(), bottom.y() + 1))
						          .stream()
						          .filter(element -> !element.isDestructible())
						          .findFirst()
						          .isPresent()) explosions.add(new VerticalExplosion(bottom, arena));
						else explosions.add(new BottomExplosion(bottom, arena));
					}
				}
			}
		}

		// explosion images disapear after a timeout
		Timeline timeline = new Timeline(new KeyFrame(Duration.millis(100), ae -> explosions.forEach(Element::delete)));
		timeline.play();

		getElementsInRange().forEach(Element::delete);
	}

	/**
	 * @return the elements in range of the blastRange
	 */
	public abstract List<Element> getElementsInRange();

	@Override
	public boolean isDestructible() {
		return true;
	}

	@Override
	public boolean isBlastAbsorber() {
		return true;
	}

	@Override
	public boolean isTraversable() {
		return false;
	}

	@Override
	public void delete() {
		super.delete();
		showExplosion();
		setChanged();
		notifyObservers();
	}
}