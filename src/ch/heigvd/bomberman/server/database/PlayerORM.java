package ch.heigvd.bomberman.server.database;

import ch.heigvd.bomberman.common.game.Player;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

/**
 * Created by matthieu.villard on 11.05.2016.
 */
public class PlayerORM extends MainORM<Player>
{

    public PlayerORM(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, Player.class);
    }

    public Player findOneByPseudo(String pseudo) throws SQLException  {
        QueryBuilder querybuilder = dao.queryBuilder();
        Player player = (Player)querybuilder.where().eq("pseudo", pseudo).queryForFirst();
        return player;
    }
}
