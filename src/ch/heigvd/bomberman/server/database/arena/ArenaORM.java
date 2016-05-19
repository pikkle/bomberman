package ch.heigvd.bomberman.server.database.arena;

import ch.heigvd.bomberman.common.game.Arena.Arena;
import ch.heigvd.bomberman.common.game.Element;
import ch.heigvd.bomberman.server.database.MainORM;
import ch.heigvd.bomberman.server.database.arena.elements.ElementORM;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by matthieu.villard on 13.05.2016.
 */
public class ArenaORM extends MainORM<Arena>
{
	public ArenaORM() throws SQLException {
		super(Arena.class);
		createTable();
	}

	@Override
	public int create(Arena arena) throws SQLException {
		int id = super.create(arena);
		updateElements(arena);
		return id;
	}

	@Override
	public int delete(Arena arena) throws SQLException {
		ElementORM orm = new ElementORM();
		List<Element> elements = orm.findByArena(arena);
		elements.forEach(element -> {
			try {
				orm.delete(element);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
		return super.delete(arena);
	}

	@Override
	public int update(Arena arena) throws SQLException {
		updateElements(arena);

		return dao.update(arena);
	}

	private void updateElements(Arena arena) throws SQLException{
		ElementORM<Element> orm = new ElementORM();
		List<Element> elements = orm.findByArena(arena);

		// delete elements removed
		elements.stream().filter(element -> !arena.getElements().contains(element)).forEach(element -> {
			try {
				orm.delete(element);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});

		arena.getElements().stream().forEach(element -> {
			try {
				if(element.getId() == 0) {
					orm.create(element);
				}
				else
					orm.update(element);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
	}
}
