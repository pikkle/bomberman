package ch.heigvd.bomberman.common.game.Arena;


import ch.heigvd.bomberman.common.game.*;
import ch.heigvd.bomberman.common.game.bombs.Bomb;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Observable;
import java.util.stream.Collectors;

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

	public Arena() throws URISyntaxException {
		this(0, 0);
	}

	public Arena(int width, int height, Arena arena) throws URISyntaxException {
		this(width, height);
		if (arena != null) id = arena.getId();
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

	public Long getId() {
		return id;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
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
	 * Add the element to the arena
	 *
	 * @param e The element to add
	 * @throws RuntimeException if the cell is already occuped
	 */
	public void add(Element e) {
		elements.add(e);
		setChanged();
		notifyObservers(e);
	}

	public void remove(Element e) {
		elements.remove(e);
		setChanged();
		notifyObservers(e);
	}

	public void change(Element e) {
		setChanged();
		notifyObservers(e);
	}
}