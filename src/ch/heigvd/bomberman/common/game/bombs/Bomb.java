package ch.heigvd.bomberman.common.game.bombs;

import ch.heigvd.bomberman.common.game.Arena.Arena;
import ch.heigvd.bomberman.common.game.DestructibleElement;
import ch.heigvd.bomberman.common.game.Element;
import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;

import java.util.List;


public abstract class Bomb extends DestructibleElement {
	protected int countdown;
	protected int blastRange;
	protected Arena arena;

	public Bomb(Point2D position, int countdown, int blastRange, Arena arena, ImageView imageView) {
		super(position, imageView);
		this.countdown = countdown;
		this.blastRange = blastRange;
		this.arena = arena;
	}

	/**
	 * To call when the bomb explose, will remove all the element in range
	 */
	public void explose() {
		getElementsInRange().forEach(e -> arena.remove(e));
	}

	/**
	 * @return the elements in range of the blastRange
	 */
	public abstract List<Element> getElementsInRange();
}
