package ch.heigvd.bomberman.common.game;

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

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name="position")
    protected Point position;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "arena_id", nullable = true)
    protected Arena arena;

    private UUID uuid;

    public Element() {
        this(new Point(), null);
    }

    public Element(Point position, Arena arena) {
        this.position = position;
        this.arena = arena;
        uuid = UUID.randomUUID();
    }

    public UUID getUuid(){
        return uuid;
    }

    public Long getId() {
        return id;
    }

    public void setArena(Arena arena){
        if(this.arena != arena) {
            if (this.arena != null)
                this.arena.remove(this);
            this.arena = arena;
            arena.add(this);
        }
    }

    public Arena getArena(){
        return arena;
    }

	public int x() {return position.x();}

	public int y() {return position.y();}

	public Point position() {return position;}

    @Override
    public boolean equals(Object obj) {
	    return obj != null && (this == obj || (obj instanceof Element && (getId() != null && getId().equals(((Element) obj).getId())) || ((Element) obj).getUuid().equals(getUuid())));
    }

    public abstract void accept(ElementVisitor visitor);

    public abstract boolean isDestructible();
    public abstract boolean isBlastAbsorber();
    public abstract boolean isTraversable();
}