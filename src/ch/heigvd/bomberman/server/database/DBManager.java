package ch.heigvd.bomberman.server.database;

import ch.heigvd.bomberman.common.game.Arena.Arena;
import ch.heigvd.bomberman.common.game.Box;
import ch.heigvd.bomberman.common.game.Element;
import ch.heigvd.bomberman.common.game.Player;
import ch.heigvd.bomberman.server.database.arena.ArenaORM;
import ch.heigvd.bomberman.server.database.arena.elements.ElementORM;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

/**
 * Created by matthieu.villard on 26.05.2016.
 */
public class DBManager
{
    private static final String DATABASE_NAME = "bomberman.db";
    private static DBManager instance;
    private ConnectionSource connectionSource;

    private DBManager() throws SQLException {
        connectionSource =  new JdbcConnectionSource("jdbc:sqlite:" + DATABASE_NAME);
        initialize();
    }

    public static synchronized DBManager getInstance() throws SQLException {
        if (instance  == null)
            instance  = new DBManager();
        return instance;
    }

    public <T extends MainORM> T getOrm(Class<T> clazz) throws SQLException {
        Constructor<T> constructor = findConstructor(clazz);
        try {
            return constructor.newInstance(connectionSource);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public MainORM getOrm(Arena arena) throws SQLException {
        return new ArenaORM(connectionSource);
    }

    public ElementORM getOrm(Element element) throws SQLException {
        ElementOrmSelector selector = new ElementOrmSelector(connectionSource);
        element.accept(selector);
        return selector.getOrm();
    }

    public MainORM getOrm(Player player) throws SQLException {
        return new PlayerORM(connectionSource);
    }

    public void close() {
        try {
            connectionSource.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void initialize() throws SQLException {
        new PlayerORM(connectionSource).createTable();
        new ElementORM<Box>(connectionSource, Box.class).createTable();
        new ArenaORM(connectionSource).createTable();
    }

    private <T extends MainORM> Constructor<T> findConstructor(Class<T> dataClass){
        Constructor[] constructors;
        Constructor[] arr$;
        try {
            arr$ = (Constructor[])dataClass.getDeclaredConstructors();
            constructors = arr$;
        } catch (Exception var8) {
            throw new IllegalArgumentException("Can\'t lookup declared constructors for " + dataClass, var8);
        }

        arr$ = constructors;
        int len$ = constructors.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            Constructor con = arr$[i$];
            if(con.getParameterTypes().length == 1) {
                if(!con.isAccessible()) {
                    try {
                        con.setAccessible(true);
                    } catch (SecurityException var7) {
                        throw new IllegalArgumentException("Could not open access to constructor for " + dataClass);
                    }
                }

                return con;
            }
        }

        if(dataClass.getEnclosingClass() == null) {
            throw new IllegalArgumentException("Can\'t find a constructor for " + dataClass);
        } else {
            throw new IllegalArgumentException("Can\'t find a constructor for " + dataClass + ".  Missing static on inner class?");
        }
    }
}
