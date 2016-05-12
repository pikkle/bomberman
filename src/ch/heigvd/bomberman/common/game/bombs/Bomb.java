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

	public Bomb(Point2D position, int countdown, int blastRange, Arena arena) {
		super(position, new ImageView(new javafx.scene.image.Image("ch/heigvd/bomberman/client/img/bomb.png")));
		this.countdown = countdown;
		this.blastRange = blastRange;
		this.arena = arena;
	}

	public void explose() {
		getElementInRange().forEach(e -> arena.getElements().remove(e));
	}

	public abstract List<Element> getElementInRange();

	public void setBlastRange(int blastRange) {
		this.blastRange = blastRange;
	}

	public void setCountdown(int countdown) {
		this.countdown = countdown;
	}
}
