package ch.heigvd.bomberman.server.database.arena.elements;

import ch.heigvd.bomberman.common.game.Box;

import java.sql.SQLException;

/**
 * Created by matthieu.villard on 18.05.2016.
 */
public class BoxORM extends ElementORM<Box>
{
	public BoxORM() throws SQLException {
		super(Box.class);
	}
}