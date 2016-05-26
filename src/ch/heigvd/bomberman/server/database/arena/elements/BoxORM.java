package ch.heigvd.bomberman.server.database.arena.elements;

import ch.heigvd.bomberman.common.game.Box;
import ch.heigvd.bomberman.server.database.DBManager;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

/**
 * Created by matthieu.villard on 26.05.2016.
 */
public class BoxORM extends ElementORM<Box> {

    public BoxORM(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, Box.class);
    }

    @Override
    public int create(Box box) throws SQLException {
        if(box.getPowerUp().isPresent()) {
            ElementORM orm = DBManager.getInstance().getOrm(ElementORM.class);
            orm.create(box.getPowerUp().get());
        }
        return super.create(box);
    }

    @Override
    public int delete(Box box) throws SQLException {
        if(box.getPowerUp().isPresent()) {
            ElementORM orm = DBManager.getInstance().getOrm(ElementORM.class);
            orm.delete(box.getPowerUp().get());
        }
        return super.delete(box);
    }

    @Override
    public int update(Box box) throws SQLException {
        return super.update(box);
    }
}
