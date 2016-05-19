package ch.heigvd.bomberman.common.game.bombs;

import ch.heigvd.bomberman.common.game.Arena.Arena;
import ch.heigvd.bomberman.common.game.DestructibleElement;
import javafx.geometry.Point2D;

import java.util.List;


public abstract class Bomb extends DestructibleElement {
    protected int countdown;
    protected int blastRange;

    public Bomb(Point2D position, int countdown, int blastRange, Arena arena) {
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
        getElementsInRange().forEach(e -> arena.remove(e));
        setChanged();
        notifyObservers();
    }

    /**
     * @return the elements in range of the blastRange
     */
    public abstract List<DestructibleElement> getElementsInRange();
}
