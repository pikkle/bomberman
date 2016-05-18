package ch.heigvd.bomberman.server.database.arena;

import ch.heigvd.bomberman.common.game.Arena.Arena;
import ch.heigvd.bomberman.server.database.MainORM;
import ch.heigvd.bomberman.server.database.arena.elements.ElementORM;

import java.sql.SQLException;

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
		ElementORM orm = new ElementORM();
		arena.getElements().forEach(element -> {
			try {
				orm.create(element);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
		return id;
	}

	@Override
	public int delete(Arena arena) throws SQLException {
		ElementORM orm = new ElementORM();
		arena.getElements().forEach(element -> {
			try {
				orm.delete(element);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
		return super.delete(arena);
	}
}
