package ch.heigvd.bomberman.server.database.arena.elements;

import ch.heigvd.bomberman.common.game.Arena.Arena;
import ch.heigvd.bomberman.common.game.Element;
import ch.heigvd.bomberman.server.database.MainORM;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by matthieu.villard on 13.05.2016.
 */

public class ElementORM<T extends Element> extends MainORM<T>
{
	private static ElementORM instance;

	protected ElementORM(Class<T> clazz) throws SQLException {
		super(clazz);
		if(instance == null)
			createTable();
	}

	protected ElementORM() throws SQLException {
		this((Class<T>) Element.class);
	}

	public static synchronized ElementORM getInstance() throws SQLException {
		if (instance  == null)
			instance  = new ElementORM();
		return instance;
	}

	public List<T> findByArena(Arena arena) throws SQLException
	{
		return dao.queryForEq("arena", arena.getId());
	}

	public T findOneByArena(Arena arena) throws SQLException
	{
		QueryBuilder querybuilder = dao.queryBuilder();
		T element = (T)querybuilder.where().eq("arena", arena.getId()).queryForFirst();
		return element;
	}
}


