package ch.heigvd.bomberman.server.database;

import ch.heigvd.bomberman.common.game.powerups.AddBombPowerUp;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Created by matthieu.villard on 30.05.2016.
 */
public class AddBombPowerUpDao extends ElementDao<AddBombPowerUp> {

    public AddBombPowerUpDao() throws SQLException {
        super();
    }

    public Optional<AddBombPowerUp> find(long id) {
        return super.find(AddBombPowerUp.class, id);
    }

    public List<AddBombPowerUp> findAll() {
        return super.findAll(AddBombPowerUp.class);
    }
}
