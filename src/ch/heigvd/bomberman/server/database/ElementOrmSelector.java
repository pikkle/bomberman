package ch.heigvd.bomberman.server.database;

import ch.heigvd.bomberman.common.game.*;
import ch.heigvd.bomberman.common.game.bombs.Bomb;
import ch.heigvd.bomberman.common.game.powerups.AddBombPowerUp;
import ch.heigvd.bomberman.server.database.arena.elements.BoxORM;
import ch.heigvd.bomberman.server.database.arena.elements.ElementORM;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

/**
 * Created by matthieu.villard on 26.05.2016.
 */
public class ElementOrmSelector implements ElementVisitor {

    private ElementORM orm;
    private ConnectionSource connectionSource;

    public ElementOrmSelector(ConnectionSource connectionSource){
        this.connectionSource = connectionSource;
    }

    @Override
    public void visit(Wall wall) {
        try {
            orm = new ElementORM<Wall>(connectionSource, Wall.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void visit(Box box) {
        try {
            orm = new BoxORM(connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void visit(AddBombPowerUp powerUp) {
        try {
            orm = new ElementORM<AddBombPowerUp>(connectionSource, AddBombPowerUp.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void visit(Bomb bomb) {
        orm = null;
    }

    @Override
    public void visit(Bomberman bomberman) {
        orm = null;
    }

    @Override
    public void visit(StartPoint startPoint) {
        try {
            orm = new ElementORM<StartPoint>(connectionSource, StartPoint.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ElementORM getOrm(){
        return orm;
    }
}
