package ch.heigvd.bomberman.server.database;

import ch.heigvd.bomberman.common.game.Player;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by matthieu.villard on 11.05.2016.
 */
public class PlayerORM extends MainORM
{
    public void createPlayer(Player player) throws SQLException {
        Dao<Player, Long> playerDao = initDao(Player.class);
        playerDao.create(player);
    }

    @SuppressWarnings("unchecked")
    public List<Player> getPlayers() throws SQLException  {
        Dao<Player, Long> playerDao = initDao(Player.class);
        List<Player> players = playerDao.queryForAll();
        doFinally();
        return players;
    }

    public Player getPlayer(long playerId) throws SQLException {
        Dao<Player, Long> playerDao = initDao(Player.class);
        Player player = playerDao.queryForId(playerId);
        doFinally();
        return player;
    }

    public Player finByPseudo(String pseudo) throws SQLException  {
        Dao<Player, String> playerDao = initDao(Player.class);
        QueryBuilder querybuilder = playerDao.queryBuilder();
        Player player = (Player)querybuilder.where().eq("pseudo", pseudo).queryForFirst();
        doFinally();
        return player;
    }
}
