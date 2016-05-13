package ch.heigvd.bomberman.common.game;

import ch.heigvd.bomberman.common.game.Arena.Arena;
import com.j256.ormlite.field.DatabaseField;
import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;

import java.util.Observable;

/**
 * Created by matthieu.villard on 09.05.2016.
 */
public abstract class Element extends Observable {
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(columnName = "x", canBeNull = false)
    private int x;

    @DatabaseField(columnName = "y", canBeNull = false)
    private int y;

    protected Point2D position;

    @DatabaseField (foreign = true, foreignAutoRefresh = true, columnName = "arena")
    protected Arena arena;

    public Element() {

    }

    public Element(Point2D position, Arena arena) {
        this.position = position;
        x = (int)position.getX();
        y = (int)position.getY();
        this.arena = arena;
    }

    public int getId() {
        return id;
    }

    public Point2D getPosition() {
        return position;
    }

    public abstract ImageView render();
}