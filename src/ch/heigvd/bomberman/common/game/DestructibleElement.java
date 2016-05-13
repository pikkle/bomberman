package ch.heigvd.bomberman.common.game;

import ch.heigvd.bomberman.common.game.Arena.Arena;
import javafx.geometry.Point2D;


/**
 * Created by matthieu.villard on 09.05.2016.
 */
public abstract class DestructibleElement extends Element {
    public DestructibleElement(Point2D position, Arena arena) {
        super(position, arena);
    }

}