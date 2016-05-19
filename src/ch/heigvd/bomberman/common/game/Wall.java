package ch.heigvd.bomberman.common.game;

import ch.heigvd.bomberman.common.game.Arena.Arena;
import ch.heigvd.bomberman.server.database.arena.elements.ElementDao;
import com.j256.ormlite.table.DatabaseTable;

import java.net.URISyntaxException;

/**
 * Created by matthieu.villard on 09.05.2016.
 */
@DatabaseTable(tableName = "wall", daoClass = ElementDao.class)
public class Wall extends Element
{
    public Wall() {
        super();
    }

    public Wall(Point position, Arena arena) throws URISyntaxException {
        super(position, arena);
        arena.add(this);
    }

    @Override
    public void accept(ElementVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean isDestructible() {
        return false;
    }

    @Override
    public boolean isBlastAbsorber() {
        return true;
    }

    @Override
    public boolean isTraversable() {
        return false;
    }
}