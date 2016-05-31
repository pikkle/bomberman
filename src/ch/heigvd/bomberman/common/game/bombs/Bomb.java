package ch.heigvd.bomberman.common.game.bombs;

import ch.heigvd.bomberman.common.game.Arena.Arena;
import ch.heigvd.bomberman.common.game.Element;
import ch.heigvd.bomberman.common.game.Point;
import ch.heigvd.bomberman.common.game.explosion.CentralExplosion;
import ch.heigvd.bomberman.common.game.explosion.Explosion;
import ch.heigvd.bomberman.common.game.explosion.HorizontalExplosion;
import ch.heigvd.bomberman.common.game.explosion.VerticalExplosion;
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
    public void explose(){
        arena.remove(this);
        getElementsInRange().forEach(element -> arena.destroy(element));
    }

    /**
     * To call when the bomb explose, will display the explosion
     */
    public void showExplosion(){


        int x = position.x();
        int y = position.y();

        boolean exploseRight = true;
        boolean exploseLeft = true;
        boolean exploseTop = true;
        boolean exploseBottom = true;

        List<Explosion> explosions = new LinkedList<>();

        arena.remove(this);
        arena.getElements(position).stream().filter(element -> element.isDestructible()).forEach(element -> arena.destroy(element));

        if(!arena.getElements(position).stream().filter(element -> !element.isBlastAbsorber()).findFirst().isPresent()) {
            explosions.add(new CentralExplosion(position, arena));

            for (int i = 1; i <= blastRange && (exploseRight || exploseLeft || exploseTop || exploseBottom); i++) {
                Point right = new Point(x + i, y);
                Point left = new Point(x - i, y);
                Point top = new Point(x, y - i);
                Point bottom = new Point(x, y + i);
                if (exploseRight && right.x() < arena.getWidth() - 1) {
                    if(arena.getElements(right).stream().filter(element -> element.isBlastAbsorber()).findFirst().isPresent())
                        exploseRight = false;
                    if (!arena.getElements(right).stream().filter(element -> !element.isDestructible()).findFirst().isPresent()) {
                        explosions.add(new HorizontalExplosion(right, arena));
                    }
                }
                if (exploseLeft && left.x() > 0) {
                    if(arena.getElements(left).stream().filter(element -> element.isBlastAbsorber()).findFirst().isPresent())
                        exploseLeft = false;
                    if (!arena.getElements(left).stream().filter(element -> !element.isDestructible()).findFirst().isPresent()) {
                        explosions.add(new HorizontalExplosion(left, arena));
                    }
                }
                if (exploseTop && top.y() > 0) {
                    if(arena.getElements(top).stream().filter(element -> element.isBlastAbsorber()).findFirst().isPresent())
                        exploseTop = false;
                    if (!arena.getElements(top).stream().filter(element -> !element.isDestructible()).findFirst().isPresent()) {
                        explosions.add(new VerticalExplosion(top, arena));
                    }
                }
                if (exploseBottom && bottom.y() < arena.getHeight() - 1) {
                    if(arena.getElements(bottom).stream().filter(element -> element.isBlastAbsorber()).findFirst().isPresent())
                        exploseBottom = false;
                    if (!arena.getElements(bottom).stream().filter(element -> !element.isDestructible()).findFirst().isPresent()) {
                        explosions.add(new VerticalExplosion(bottom, arena));
                    }
                }
            }
        }

        // explosion images disapear after a timeout
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.millis(500),
                ae -> {
                    explosions.forEach(explosion -> {
                        arena.remove(explosion);
                    });
                    explosions.clear();
                }
        ));
        timeline.play();
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