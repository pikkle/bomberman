package ch.heigvd.bomberman.server.database.arena.elements;

import ch.heigvd.bomberman.common.game.Arena.Arena;
import ch.heigvd.bomberman.common.game.Element;
import ch.heigvd.bomberman.server.database.MainORM;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by matthieu.villard on 13.05.2016.
 */

public class ElementORM<T extends Element> extends MainORM<T>
{

	public ElementORM(ConnectionSource connectionSource, Class<T> clazz) throws SQLException {
		super(connectionSource, clazz);
	}

	public ElementORM(ConnectionSource connectionSource) throws SQLException {
		this(connectionSource, (Class<T>) Element.class);
	}

	@Override
	public int delete(T element) throws SQLException {
		element.getArena().remove(element);
		return super.delete(element);
	}

	public List<T> findByArena(Arena arena) throws SQLException
	{
		return dao.queryForEq("arena", arena.getId());
	}
}


