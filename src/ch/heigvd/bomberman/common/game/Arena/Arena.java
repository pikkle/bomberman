package ch.heigvd.bomberman.common.game.Arena;


import ch.heigvd.bomberman.common.game.Bomberman;
import ch.heigvd.bomberman.common.game.Element;
import ch.heigvd.bomberman.common.game.Skin;
import ch.heigvd.bomberman.common.game.bombs.Bomb;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Point2D;
import javafx.util.Duration;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

/**
 * Created by matthieu.villard on 09.05.2016.
 */
@DatabaseTable(tableName = "arena")
public class Arena
{
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(columnName = "width", canBeNull = false)
    private int width;

    @DatabaseField(columnName = "height", canBeNull = false)
    private int height;

    @ForeignCollectionField(eager = true)
    private Collection<Element> elements = new LinkedList<Element>();

    private Queue<Bomb> bombs = new ConcurrentLinkedQueue<>();

    public Arena() {
        new Thread(() -> {
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(1000), ae -> bombs.forEach(b -> {
                b.decreaseCountdown();
                if (b.getCountdown() <= 0) {
                    b.explose();
                    elements.remove(b);
                    bombs.remove(b);
                }
            })));
            timeline.setCycleCount(Animation.INDEFINITE);
            timeline.play();
        }).start();
    }

    public Arena(int width, int height) {
        this();
        this.width = width;
        this.height = height;
    }

    public int getId() {
        return id;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Bomberman putBomberman() throws Exception {
        for(int i = 0; i < 4; i++){
            Point2D position = new Point2D(1 + i / 2, 1 + i % 2);
            if(isEmpty(position)){
                Bomberman bomberman = new Bomberman(position, Skin.values()[i], this);
                elements.add(bomberman);
                return bomberman;
            }
        }
        throw new Exception("No available place!");
    }

    /**
     * @param position the position
     * @return true if nothing is at the position given
     */
    public boolean isEmpty(Point2D position) {
        return elements.stream().noneMatch(element -> element.getPosition().equals(position));
    }

    /**
     * @return all the elements
     */
    public Collection<Element> getElements() {
        return elements;
    }

    /**
     * @param position the position
     * @return All the elements at the position
     */
    public Collection<Element> getElements(Point2D position) {
        return elements.stream().filter(e -> e.getPosition().equals(position)).collect(Collectors.toList());
    }

    /**
     * Add the element to the arena
     *
     * @param element The element to add
     * @throws RuntimeException if the cell is already occuped
     */
    public void add(Element element) throws RuntimeException {
        if(!elements.contains(element)) {
            if (isEmpty(element.getPosition())) {
                elements.add(element);
            } else {
                throw new RuntimeException("Cell already occuped");
            }
        }
    }

    /**
     * Add a bomb to the arena
     *
     * @param bomb
     * @throws RuntimeException
     */
    public void add(Bomb bomb) throws RuntimeException {
        if (bombs.stream().noneMatch(b -> b.getPosition().equals(bomb.getPosition()))) {
            if(!bombs.contains(bomb))
                bombs.add(bomb);
            if(!elements.contains(bomb))
                elements.add(bomb);
        } else {
            throw new RuntimeException("Already a bomb");
        }

    }

    public void remove(Element e) {
        // TODO end the element (kill the player, explose the box)
        elements.remove(e);
    }
}
