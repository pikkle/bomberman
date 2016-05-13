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
    private Dao<Player, Long> dao;

    public PlayerORM() throws SQLException {
        super();
        createTable();
        dao = initDao();
    }

    public void create(Player player) throws SQLException {
        dao.create(player);
    }

    public List<Player> findAll() throws SQLException  {
        List<Player> players = dao.queryForAll();
        return players;
    }

    public Player find(long id) throws SQLException {
        Player player = dao.queryForId(id);
        return player;
    }

    public Player finByPseudo(String pseudo) throws SQLException  {
        QueryBuilder querybuilder = dao.queryBuilder();
        Player player = (Player)querybuilder.where().eq("pseudo", pseudo).queryForFirst();
        return player;
    }

    @Override
    protected Class getTableClass() {
        return Player.class;
    }
}
