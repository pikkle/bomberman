package ch.heigvd.bomberman.server.database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by matthieu.villard on 11.05.2016.
 */
public abstract class MainORM {

    private static final String DATABASE_NAME = "bomberman.db";

    private ConnectionSource connectionSource;

    public MainORM() throws SQLException {
        connectionSource =  new JdbcConnectionSource("jdbc:sqlite:" + DATABASE_NAME);
    }

    protected void createTable() throws SQLException {
        TableUtils.createTableIfNotExists(connectionSource, getTableClass());
    }

    protected Dao initDao() throws SQLException {
        return DaoManager.createDao(connectionSource, getTableClass());
    }

    protected void close() {
        try {
            connectionSource.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected abstract Class getTableClass();
}