package ch.heigvd.bomberman.server.database.arena.elements;

import ch.heigvd.bomberman.common.game.Wall;

import java.sql.SQLException;

/**
 * Created by matthieu.villard on 18.05.2016.
 */
public class WallORM extends ElementORM<Wall>
{
	public WallORM() throws SQLException {
		super(Wall.class);
	}
}