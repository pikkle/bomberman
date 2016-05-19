package ch.heigvd.bomberman.server.database.arena.elements;

import ch.heigvd.bomberman.common.game.Box;

import java.sql.SQLException;

/**
 * Created by matthieu.villard on 18.05.2016.
 */
public class BoxORM extends ElementORM<Box>
{
	private static BoxORM instance;

	private BoxORM() throws SQLException {
		super(Box.class);
	}

	public static synchronized ElementORM getInstance() throws SQLException {
		if (instance  == null)
			instance  = new BoxORM();
		return instance;
	}
}