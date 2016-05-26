package ch.heigvd.bomberman.common.game.bombs;

import ch.heigvd.bomberman.common.game.Arena.Arena;
import ch.heigvd.bomberman.common.game.Element;
import ch.heigvd.bomberman.common.game.Point;

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
     * To call when the bomb explose, will remove all the element in range
     */
    public void explose() {
        getElementsInRange().forEach(e -> arena.destroy(e));
        arena.remove(this);
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
}