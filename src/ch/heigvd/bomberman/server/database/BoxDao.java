package ch.heigvd.bomberman.server.database;

import ch.heigvd.bomberman.common.game.Box;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Created by matthieu.villard on 30.05.2016.
 */
public class BoxDao extends ElementDao<Box> {

    public BoxDao() throws SQLException {
        super();
    }

    public Optional<Box> find(long id) {
        return super.find(Box.class, id);
    }

    public List<Box> findAll() {
        return super.findAll(Box.class);
    }
}
