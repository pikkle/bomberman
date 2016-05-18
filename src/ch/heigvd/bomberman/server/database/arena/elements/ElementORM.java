package ch.heigvd.bomberman.server.database.arena.elements;

import ch.heigvd.bomberman.common.game.Arena.Arena;
import ch.heigvd.bomberman.common.game.Element;
import ch.heigvd.bomberman.server.database.MainORM;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by matthieu.villard on 13.05.2016.
 */
public class ElementORM<T extends Element> extends MainORM<T>
{
	public ElementORM(Class<T> clazz) throws SQLException {
		super(clazz);
		createTable();
	}

	public ElementORM() throws SQLException {
		this((Class<T>) Element.class);
	}

	public List<T> findOneByArena(Arena arena) throws SQLException
	{
		return dao.queryForEq("arena", arena.getId());
	}
}
