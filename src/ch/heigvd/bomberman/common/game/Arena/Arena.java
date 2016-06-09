package ch.heigvd.bomberman.common.game.Arena;


import ch.heigvd.bomberman.common.game.*;
import ch.heigvd.bomberman.common.game.bombs.Bomb;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ch.heigvd.bomberman.common.game.Direction.*;

/**
 * Created by matthieu.villard on 09.05.2016.
 */
@Entity
@Table(name = "arena")
public class Arena extends Observable implements Serializable {
	@Id @Column(name = "id") @GeneratedValue(strategy = GenerationType.AUTO) private Long id;

	@Column(name = "width") private int width;

	@Column(name = "height") private int height;

	@OneToMany(targetEntity = Element.class, fetch = FetchType.EAGER, mappedBy = "arena")
	@Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.DELETE})
	private Collection<Element> elements = new LinkedList<>();

	public Arena() {
		this(0, 0);
	}

	public Arena(int width, int height, Arena arena) throws URISyntaxException {
		this(width, height);
		if (arena != null) id = arena.getId();
	}

	public Arena(int width, int height) {
		this.width = Math.max(width, 2);
		this.height = Math.max(height, 2);

		for (int i = 0; i < width(); i++) {
			new Wall(new Point(i, 0), this);
			new Wall(new Point(i, height() - 1), this);
		}

		for (int i = 1; i < height() - 1; i++) {
			new Wall(new Point(0, i), this);
			new Wall(new Point(width() - 1, i), this);
		}
	}

	public Long getId() {
		return id;
	}

	/**
	 * Gets the cell width of the arena.
	 *
	 * @return the width of the arena
	 */
	public int width() {
		return width;
	}

	/**
	 * Gets the cell height of the arena.
	 *
	 * @return the height of the arena
	 */
	public int height() {
		return height;
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
	 * @return all the bombermen
	 */
	public Collection<Bomberman> getBombermen() {
		return elements.stream()
		               .filter(element -> element instanceof Bomberman)
		               .map(element -> (Bomberman) element)
		               .collect(Collectors.toList());
	}

	/**
	 * @return all the bombs
	 */
	public Collection<Bomb> getBombs() {
		return elements.stream()
		               .filter(element -> element instanceof Bomb)
		               .map(element -> (Bomb) element)
		               .collect(Collectors.toList());
	}

	/**
	 * @return all the availableStartPoints
	 */
	public Collection<StartPoint> getStartPoints() {
		return elements.stream()
		               .filter(element -> element instanceof StartPoint)
		               .map(element -> (StartPoint) element)
		               .collect(Collectors.toList());
	}

	/**
	 * Adds the element to the arena
	 *
	 * @param e The element to add
	 * @throws RuntimeException if the cell is already occuped
	 */
	public void add(Element e) {
		elements.add(e);
		notify(e);
	}

	/**
	 * Removes an element from the arena.
	 *
	 * @param e the element to remove
	 */
	public void remove(Element e) {
		elements.remove(e);
		notify(e);
	}

	/**
	 * Notifies the element that the arena has changed.
	 *
	 * @param e the element to notify
	 */
	public void notify(Element e) {
		setChanged();
		notifyObservers(e);
	}

	/**
	 * Gets elements in cross radius from the center.
	 *
	 * The stream have the different elements in each direction.
	 * TODO
	 *
	 * @param center the center of the cross
	 * @param radius the radius
	 * @return elements in cross radius
	 */
	public Stream<Pair<List<Element>, Direction>> elementsInRange(Element center, int radius) {
		int x = center.x();
		int y = center.y();

		List<Element> up = new ArrayList<>(),
				left = new ArrayList<>(),
				right = new ArrayList<>(),
				down = new ArrayList<>();

		Function<Element, Double> distanceFromCenter = e -> Math.pow(e.x() - x, 2) + Math.pow(e.y() - y, 2);

		elements.stream()
		     .filter(e -> (e.x() == x || e.y() == y) && e != center)
		     .filter(e -> {
			     double pos = (e.x() == x) ? e.y() : e.x();
			     double ref = (e.x() == x) ? y : x;
			     return Math.abs(ref - pos) <= radius;
		     })
		     .sorted(Comparator.comparing(distanceFromCenter))
		     .forEach(e -> {
			     double ex = e.x();
			     double ey = e.y();
			     ((ex == x) ? (ey >= y ? down : up) : (ex > x ? right : left)).add(e);
		     });

		Iterator<Direction> it = Stream.of(UP, LEFT, DOWN, RIGHT).iterator();
		return Stream.of(up, left, down, right).map(l -> Pair.of(l, it.next()));
	}
}