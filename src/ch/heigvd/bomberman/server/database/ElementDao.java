package ch.heigvd.bomberman.server.database;

import ch.heigvd.bomberman.common.game.Element;

import java.sql.SQLException;

/**
 * Created by matthieu.villard on 30.05.2016.
 */
public class ElementDao<T extends Element> extends MainDao<T> {

    public ElementDao() throws SQLException {
        super();
    }
}
