package ch.heigvd.bomberman.server.database;

import ch.heigvd.bomberman.common.game.Arena.Arena;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by matthieu.villard on 13.05.2016.
 */
public class ArenaORM extends MainORM
{
	private Dao<Arena, Long> dao;

	public ArenaORM() throws SQLException {
		super();
		createTable();
		dao = initDao();
	}

	public void create(Arena arena) throws SQLException {
		dao.create(arena);
		arena.getElements().forEach(element -> {
			try {
				ElementORM orm = new ElementORM(element.getClass());
				orm.create(element);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
	}

	public List<Arena> findAll() throws SQLException  {
		return dao.queryForAll();
	}

	public Arena find(long id) throws SQLException {
		return dao.queryForId(id);
	}

	@Override
	protected Class getTableClass() {
		return Arena.class;
	}
}
