package ch.heigvd.bomberman.common.game.Arena;


import ch.heigvd.bomberman.common.game.*;
import ch.heigvd.bomberman.common.game.bombs.Bomb;
import ch.heigvd.bomberman.common.game.powerups.PowerUp;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.io.Serializable;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Observable;
import java.util.stream.Collectors;

/**
 * Created by matthieu.villard on 09.05.2016.
 */
@DatabaseTable(tableName = "arena")
public class Arena extends Observable implements Serializable
{
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(columnName = "width", canBeNull = false)
    private int width;

    @DatabaseField(columnName = "height", canBeNull = false)
    private int height;

    @ForeignCollectionField(eager = true)
    private Collection<Element> elements = new LinkedList<>();


    public Arena() throws URISyntaxException {
	    this(0, 0);
    }

    public Arena(int width, int height) throws URISyntaxException {
        this.width = Math.max(width, 2);
        this.height = Math.max(height, 2);

	    for (int i = 0; i < getWidth(); i++) {
		    new Wall(new Point(i, 0), this);
		    new Wall(new Point(i, getHeight() - 1), this);
	    }

	    for (int i = 1; i < getHeight() - 1; i++) {
		    new Wall(new Point(0, i), this);
		    new Wall(new Point(getWidth() - 1, i), this);
	    }
    }

    public void start(){
        new Thread(() -> {
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(1000), ae -> getBombs().forEach(b -> {
                b.decreaseCountdown();
                if (b.getCountdown() <= 0) {
                    b.explose();
                }
            })));
            timeline.setCycleCount(Animation.INDEFINITE);
            timeline.play();
        }).start();
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
        return elements.stream().noneMatch(e -> e.position().equals(position));
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
     * @return all the availableStartPoints
     */
    public Collection<Bomb> getBombs() {
        return elements.stream().filter(element -> element instanceof Bomb).map(element -> (Bomb)element).collect(Collectors.toList());
    }

    /**
     * @return all the availableStartPoints
     */
    public Collection<StartPoint> getStartPoints() {
        return elements.stream().filter(element -> element instanceof StartPoint).map(element -> (StartPoint)element).collect(Collectors.toList());
    }

    /**
     * Add the element to the arena
     *
     * @param element The element to add
     * @throws RuntimeException if the cell is already occuped
     */
    public void add(Element element) throws RuntimeException {
        element.accept(new ElementAddHandler(this));
        setChanged();
        notifyObservers(element);
    }

    /**
     * Add the element to the arena
     *
     * @param element The element to add
     * @throws RuntimeException if the cell is already occuped
     */
    protected void insert(Element element) throws RuntimeException {
        if(!elements.contains(element)) {
            if (isEmpty(element.position())) {
                elements.add(element);
                element.setArena(this);
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
    protected void insert(Bomb bomb) throws RuntimeException {
        if (getBombs().stream().noneMatch(b -> b.position().equals(bomb.position()))) {
            if(!elements.contains(bomb)) {
                elements.add(bomb);
                bomb.setArena(this);
            }
        } else {
            throw new RuntimeException("Already a bomb");
        }

    }

    /**
     * Add the available start point to the arena
     *
     * @param startPoint The position to add
     * @throws RuntimeException if the cell is already occuped
     */
    protected void insert(StartPoint startPoint) throws RuntimeException {
        if(!elements.contains(startPoint)) {
            if(getStartPoints().size() >= 4){
                throw new RuntimeException("4 availables start points have already been set");
            }
            else if(!isEmpty(startPoint.position())){
                throw new RuntimeException("Cell already occuped");
            }
            else {
                elements.add(startPoint);
            }
        }
    }

    /**
     * Add the powerup start point to the arena
     *
     * @param powerUp The position to add
     * @throws RuntimeException if the cell is already occuped
     */
    protected void insert(PowerUp powerUp) throws RuntimeException {
        Collection<Element> boxes = getElements(powerUp.position());
        if(boxes.size() == 1 && boxes.stream().filter(element -> element instanceof Box).findFirst().isPresent()){
            Box box = (Box)boxes.stream().filter(element -> element instanceof Box).findFirst().get();
            box.setPowerUp(powerUp);
        }
        else if(isEmpty(powerUp.position())){
            insert((Element)powerUp);
        }
        else{
            throw new RuntimeException("Cell already occuped");
        }
    }

    public void remove(Element e) {
        e.accept(new ElementRemoveHandler(this));
        setChanged();
        notifyObservers(e);
    }

    protected void delete(Element e) {
        elements.remove(e);
    }

    protected void delete(PowerUp powerUp) {
        Collection<Element> boxes = getElements(powerUp.position());
        if(boxes.size() == 1 && boxes.stream().filter(element -> element instanceof Box).findFirst().isPresent()){
            Box box = (Box)boxes.stream().filter(element -> element instanceof Box).findFirst().get();
            box.setPowerUp(null);
        }
        else {
            delete((Element)powerUp);
        }
    }

    public void destroy(Element e) {
        e.accept(new ElementDestroyHandler(this));
        setChanged();
        notifyObservers(e);
    }

    protected void destroy(Box b) {
        delete((Element)b);
        if(b.getPowerUp().isPresent())
            add(b.getPowerUp().get());
    }

    public void change(Element e) {
        setChanged();
        notifyObservers(e);
    }
}