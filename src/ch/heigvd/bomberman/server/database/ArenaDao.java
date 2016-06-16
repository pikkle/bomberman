package ch.heigvd.bomberman.server.database;

import ch.heigvd.bomberman.common.game.Arena;

import java.util.List;
import java.util.Optional;

/**
 * Created by matthieu.villard on 30.05.2016.
 */
public class ArenaDao extends MainDao<Arena> {

    public ArenaDao() {
        super();
    }

    public Optional<Arena> find(Long id) {
        return super.find(Arena.class, id);
    }

    public List<Arena> findAll() {
        return super.findAll(Arena.class);
    }
}
