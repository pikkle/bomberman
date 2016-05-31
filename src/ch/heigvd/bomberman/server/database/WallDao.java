package ch.heigvd.bomberman.server.database;

import ch.heigvd.bomberman.common.game.Wall;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Created by matthieu.villard on 30.05.2016.
 */
public class WallDao extends ElementDao<Wall> {

    public WallDao() throws SQLException {
        super();
    }

    public Optional<Wall> find(long id) {
        return super.find(Wall.class, id);
    }

    public List<Wall> findAll() {
        return super.findAll(Wall.class);
    }
}
