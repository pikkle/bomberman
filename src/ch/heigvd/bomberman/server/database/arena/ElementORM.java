package ch.heigvd.bomberman.server.database.arena;

import ch.heigvd.bomberman.common.game.Element;
import ch.heigvd.bomberman.server.database.MainORM;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by matthieu.villard on 13.05.2016.
 */
public class ElementORM extends MainORM
{
	private Dao<Element, Long> dao;

	public ElementORM() throws SQLException {
		super();
		createTable();
		dao = initDao();
	}

	public void create(Element element) throws SQLException {
		dao.create(element);
	}

	public List<Element> findAll() throws SQLException  {
		return dao.queryForAll();
	}

	public Element find(long id) throws SQLException {
		return dao.queryForId(id);
	}

	@Override
	protected Class getTableClass() {
		return Element.class;
	}
}
