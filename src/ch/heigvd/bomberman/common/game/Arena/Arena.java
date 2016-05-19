package ch.heigvd.bomberman.common.game.Arena;


import ch.heigvd.bomberman.common.game.Bomberman;
import ch.heigvd.bomberman.common.game.Element;
import ch.heigvd.bomberman.common.game.Point;
import ch.heigvd.bomberman.common.game.Skin;
import ch.heigvd.bomberman.common.game.bombs.Bomb;
import ch.heigvd.bomberman.server.database.arena.elements.PositionsConverter;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

/**
 * Created by matthieu.villard on 09.05.2016.
 */
@DatabaseTable(tableName = "arena")
public class Arena extends Observable
{
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(columnName = "width", canBeNull = false)
    private int width;

    @DatabaseField(columnName = "height", canBeNull = false)
    private int height;

    @ForeignCollectionField(eager = true)
    private Collection<Element> elements = new LinkedList();

    @DatabaseField(columnName = "availableStartPoints", canBeNull = false, persisterClass = PositionsConverter.class)
    private Collection<Point> availableStartPoints = new LinkedList();

    private Queue<Bomb> bombs = new ConcurrentLinkedQueue<>();

    public Arena() {
        new Thread(() -> {
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(1000), ae -> bombs.forEach(b -> {
                b.decreaseCountdown();
                if (b.getCountdown() <= 0) {
                    b.explose();
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
            Point position = new Point(1 + i / 2, 1 + 1 % 2);
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
    public boolean isEmpty(Point position) {
        return elements.stream().noneMatch(element -> element.position().equals(position));
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
    public Collection<Element> getElements(Point position) {
        return elements.stream().filter(e -> e.position().equals(position)).collect(Collectors.toList());
    }

    /**
     * Add the element to the arena
     *
     * @param element The element to add
     * @throws RuntimeException if the cell is already occuped
     */
    public void add(Element element) throws RuntimeException {
        if(!elements.contains(element)) {
            if (isEmpty(element.position())) {
                elements.add(element);
                setChanged();
                notifyObservers(element);
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
        if (bombs.stream().noneMatch(b -> b.position().equals(bomb.position()))) {
            if(!bombs.contains(bomb))
                bombs.add(bomb);
            if(!elements.contains(bomb))
                elements.add(bomb);
            setChanged();
            notifyObservers(bomb);
        } else {
            throw new RuntimeException("Already a bomb");
        }

    }

    public void remove(Element e) {
        // TODO end the element (kill the player, explose the box)
        elements.remove(e);
        setChanged();
        notifyObservers(e);
    }

    public void remove(Bomb b) {
        // TODO end the element (kill the player, explose the box)
        elements.remove(b);
        bombs.remove(b);
        setChanged();
        notifyObservers(b);
    }

    public void change(Element e) {
        setChanged();
        notifyObservers(e);
    }

    /**
     * @return all the availableStartPoints
     */
    public Collection<Point> getAvailableStartPoints() {
        return availableStartPoints;
    }

    /**
     * Add the available start point to the arena
     *
     * @param position The position to add
     * @throws RuntimeException if the cell is already occuped
     */
    public void add(Point position) throws RuntimeException {
        if(!availableStartPoints.contains(position)) {
            if(availableStartPoints.size() >= 4){
                throw new RuntimeException("4 availables start points have already been set");
            }
            else if(!isEmpty(position)){
                throw new RuntimeException("Cell already occuped");
            }
            else {
                availableStartPoints.add(position);
            }
        }
    }

    public void remove(Point position) {
        // TODO end the element (kill the player, explose the box)
        availableStartPoints.remove(position);
    }
}