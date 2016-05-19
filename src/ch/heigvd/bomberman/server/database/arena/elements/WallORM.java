package ch.heigvd.bomberman.server.database.arena.elements;

import ch.heigvd.bomberman.common.game.Wall;

import java.sql.SQLException;

/**
 * Created by matthieu.villard on 18.05.2016.
 */
public class WallORM extends ElementORM<Wall>
{
	private static WallORM instance;

	private WallORM() throws SQLException {
		super(Wall.class);
	}

	public static synchronized ElementORM getInstance() throws SQLException {
		if (instance  == null)
			instance  = new WallORM();
		return instance;
	}
}