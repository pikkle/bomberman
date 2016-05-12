package ch.heigvd.bomberman.server.database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

/**
 * Created by matthieu.villard on 11.05.2016.
 */
public class MainORM {

    private static final String DATABASE_NAME = "bomberman.db";

    private ConnectionSource connectionSource;

    protected Dao initDao(Class model) {
        try {
            connectionSource =  new JdbcConnectionSource("jdbc:sqlite:" + DATABASE_NAME);
            return DaoManager.createDao(connectionSource, model);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected void doFinally() {
        try {
            connectionSource.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}