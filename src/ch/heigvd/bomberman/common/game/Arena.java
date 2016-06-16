package ch.heigvd.bomberman.common.game;

import ch.heigvd.bomberman.common.util.Direction;
import ch.heigvd.bomberman.common.util.Pair;
import ch.heigvd.bomberman.common.util.Point;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.*;
import java.net.URISyntaxException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ch.heigvd.bomberman.common.util.Direction.*;

/**
 * An arena have all the elements and give some function to go search any
 * elements.
 */
@Entity
@Table(name = "arena")
public class Arena extends Observable implements Serializable, Cloneable {

	private static final long serialVersionUID = -7494940591783142675L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "width")
	private int width;

	@Column(name = "height")
	private int height;

	@OneToMany(targetEntity = Element.class, fetch = FetchType.EAGER, mappedBy = "arena")
	@Fetch(value = FetchMode.SELECT)
	@Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.DELETE})
	private List<Element> elements = new LinkedList<>();

	public Arena() {
		this(0, 0);
	}

	public Arena(int width, int height, Arena arena) throws URISyntaxException {
		this(width, height);
		if (arena != null) id = arena.getId();
	}

	/**
	 * Add wall all around of the arena.
	 */
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

	/**
	 * Gets the ID of the arena.
	 *
	 * @return the ID of the arena
	 */
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
	 * Gets if the position is already occupied.
	 *
	 * @param position the position
	 * @return true if nothing is at the position given
	 */
	public boolean isEmpty(Point position) {
		return elements(position).stream().filter(e -> !e.isTraversable()).count() == 0;
	}

	/**
	 * Gets all elements of the arena.
	 *
	 * @return all the elements
	 */
	public Collection<Element> elements() {
		return elements;
	}

	/**
	 * Gets all elements of the arena for a given class.
	 *
	 * @param type the class
	 * @param <T>  the type of the searched element
	 * @return a list of typed element searched
	 */
	@SuppressWarnings("unchecked")
	public <T extends Element> List<T> elements(Class<T> type) {
		return elements.stream()
		               .filter(e -> type.isAssignableFrom(e.getClass()))
		               .map(e -> (T) e)
		               .collect(Collectors.toList());
	}

	/**
	 * Gets all elements at the position.
	 *
	 * @param position the position
	 * @return All the elements at the position
	 */
	public List<Element> elements(Point position) {
		return elements.stream()
		               .filter(e -> e.position().equals(position))
		               .collect(Collectors.toList());
	}

	/**
	 * Adds the element to the arena.
	 *
	 * @param e The element to add
	 */
	public void add(Element e) {
		if (!elements.contains(e)) {
			elements.add(e);
			notify(e);
		}
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
	 * <p>
	 * The stream have the different elements in each direction (up, down, left, right)
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Arena clone() {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(this);

			ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
			ObjectInputStream ois = new ObjectInputStream(bais);
			return (Arena) ois.readObject();
		} catch (IOException e) {
			return null;
		} catch (ClassNotFoundException e) {
			return null;
		}
	}

}