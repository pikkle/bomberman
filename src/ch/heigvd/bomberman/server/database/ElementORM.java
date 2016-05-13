package ch.heigvd.bomberman.server.database;

import ch.heigvd.bomberman.common.game.Element;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by matthieu.villard on 13.05.2016.
 */
public class ElementORM<T extends Element> extends MainORM
{
	private Class<T> type;
	private Dao<T, Long> dao;

	public ElementORM(Class<T> type) throws SQLException {
		super();
		this.type = type;
		createTable();
		dao = initDao();
	}

	public void create(T element) throws SQLException {
		dao.create(element);
	}

	public List<T> findAll() throws SQLException  {
		return dao.queryForAll();
	}

	public T find(long id) throws SQLException {
		return dao.queryForId(id);
	}

	@Override
	protected Class getTableClass() {
		return type;
	}
}
