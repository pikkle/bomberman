package ch.heigvd.bomberman.common.game;

import ch.heigvd.bomberman.client.views.render.ImageViewPane;
import ch.heigvd.bomberman.common.game.Arena.Arena;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Observable;
import java.util.UUID;

/**
 * Created by matthieu.villard on 09.05.2016.
 */
@Entity
@Table(name = "element")
public abstract class Element extends Observable implements Serializable {

	@Column(name = "sprite")  private ImageViewPane sprite;
	@Column(name = "position") protected Point position;
	@ManyToOne(fetch = FetchType.EAGER) @JoinColumn(name = "arena_id", nullable = true) protected Arena arena;
	@Id @Column(name = "id") @GeneratedValue(strategy = GenerationType.AUTO) private Long id;
	private UUID uuid;

	public Element(Point position, Arena arena) {
		this.position = position;
		this.arena = arena;
		uuid = UUID.randomUUID();
		arena.add(this);
	}

	protected Element() {
	}

	public UUID getUuid() {
		return uuid;
	}

	public Long getId() {
		return id;
	}

	/**
	 * Set the arena.
	 *
	 * @param arena the arena.
	 */
	public void setArena(Arena arena) {
		if (this.arena != arena) {
			if (this.arena != null) this.arena.remove(this);
			this.arena = arena;
			arena.add(this);
		}
	}

	/**
	 * Get the arena where the element is
	 *
	 * @return the arena
	 */
	public Arena arena() {
		return arena;
	}

	/**
	 * Get the x value of the position of the element.
	 *
	 * @return the x value of the position
	 */
	public int x() {return position.x();}

	/**
	 * Get the y value of the position of the element.
	 *
	 * @return the y value of the position
	 */
	public int y() {return position.y();}

	/**
	 * Get the position of the element.
	 *
	 * @return the position
	 */
	public Point position() {return position;}

	@Override
	public boolean equals(Object obj) {
		return obj != null && (this == obj || (obj instanceof Element &&
		                                       (getId() != null && getId().equals(((Element) obj).getId())) ||
		                                       ((Element) obj).getUuid().equals(getUuid())));
	}


	/**
	 * If an element can be destroyed
	 *
	 * @return true if the element can be destroyed, false if it's not
	 */
	public abstract boolean isDestructible();

	/**
	 * If the blast can't go through the element
	 *
	 * @return true if the blast can, false otherwise
	 */
	public abstract boolean isBlastAbsorber();

	/**
	 * If the element can be traversed.
	 *
	 * @return true if he can
	 */
	public abstract boolean isTraversable();

	/**
	 * Remove the element from the arena
	 */
	public void delete() {
		arena.remove(this);
	}

	public ImageViewPane getSprite() {
		if (sprite == null) sprite = new ImageViewPane(getPath());
		return sprite;
	}

	/**
	 * Gets the path of the image.
	 *
	 * @return the path of the image
	 */
	protected abstract String getPath();
}