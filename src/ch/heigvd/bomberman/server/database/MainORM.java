package ch.heigvd.bomberman.server.database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.logger.LocalLog;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by matthieu.villard on 11.05.2016.
 */
public abstract class MainORM<T> {

    private static final String DATABASE_NAME = "bomberman.db";
    private ConnectionSource connectionSource;
    private Class<T> clazz;
    protected Dao<T, Long> dao;

    public MainORM(Class<T> clazz) throws SQLException {
        System.setProperty(LocalLog.LOCAL_LOG_LEVEL_PROPERTY, "ERROR");
        this.clazz = clazz;
        connectionSource =  new JdbcConnectionSource("jdbc:sqlite:" + DATABASE_NAME);
        dao = DaoManager.createDao(connectionSource, clazz);
    }

    public void createTable() throws SQLException {
        TableUtils.createTableIfNotExists(connectionSource, clazz);
    }

    public int create(T object) throws SQLException {
        return dao.create(object);
    }

    public int delete(T object) throws SQLException {
        return dao.delete(object);
    }

    public List<T> findAll() throws SQLException  {
        List<T> objects = dao.queryForAll();
        return objects;
    }

    public T find(long id) throws SQLException {
        T object = dao.queryForId(id);
        return object;
    }

    public void close() {
        try {
            connectionSource.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}