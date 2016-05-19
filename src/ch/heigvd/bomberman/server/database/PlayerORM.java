package ch.heigvd.bomberman.server.database;

import ch.heigvd.bomberman.common.game.Player;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;

/**
 * Created by matthieu.villard on 11.05.2016.
 */
public class PlayerORM extends MainORM<Player>
{
    private static PlayerORM instance;

    private PlayerORM() throws SQLException {
        super(Player.class);
        createTable();
    }

    public static synchronized PlayerORM getInstance() throws SQLException {
        if (instance  == null)
            instance  = new PlayerORM();
        return instance;
    }

    public Player findOneByPseudo(String pseudo) throws SQLException  {
        QueryBuilder querybuilder = dao.queryBuilder();
        Player player = (Player)querybuilder.where().eq("pseudo", pseudo).queryForFirst();
        return player;
    }
}
