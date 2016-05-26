package ch.heigvd.bomberman.common.game;

import ch.heigvd.bomberman.common.game.Arena.Arena;
import ch.heigvd.bomberman.server.database.arena.elements.ElementDao;
import ch.heigvd.bomberman.server.database.arena.elements.PositionConverter;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Observable;

/**
 * Created by matthieu.villard on 09.05.2016.
 */
@DatabaseTable(tableName = "element", daoClass = ElementDao.class)
public abstract class Element extends Observable {
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(columnName = "discr", canBeNull = false)
    private String discr;

    @DatabaseField(columnName = "position", canBeNull = false, persisterClass = PositionConverter.class)
    protected Point position;

    @DatabaseField (foreign = true, foreignAutoRefresh = true, columnName = "arena")
    protected Arena arena;

    public Element() {
        this(new Point(), null);
    }

    public Element(Point position, Arena arena) {
        discr = getClass().getName();
        this.position = position;
        this.arena = arena;
    }

    public int getId() {
        return id;
    }

    public String getDiscr(){
        return discr;
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
	    return this == obj || (obj instanceof Element && getId() != 0 && ((Element) obj).getId() == getId());
    }

    public abstract void accept(ElementVisitor visitor);

    public abstract boolean isDestructible();
    public abstract boolean isBlastAbsorber();
    public abstract boolean isTraversable();
}