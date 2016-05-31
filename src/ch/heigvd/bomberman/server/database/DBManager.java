package ch.heigvd.bomberman.server.database;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.sql.SQLException;

/**
 * Created by matthieu.villard on 26.05.2016.
 */
public class DBManager {
    private static DBManager instance;
    private static Log log = LogFactory.getLog(DBManager.class);
    private SessionFactory sessionFactory;

    private DBManager() throws SQLException {
        initDB();
    }

    public static synchronized DBManager getInstance() throws SQLException {
        if (instance  == null)
            instance  = new DBManager();
        return instance;
    }

    public void initDB() throws SQLException {
        //new ArenaDao().createTable();
    }

    public PlayerDao players() throws SQLException {
        return new PlayerDao();
    }

    public ArenaDao arenas() throws SQLException {
        return new ArenaDao();
    }

    /**
     * Constructs a new Singleton SessionFactory
     * @return
     * @throws HibernateException
     */
    public SessionFactory buildSessionFactory() throws HibernateException {
        if (sessionFactory != null) {
            closeFactory();
        }
        return configureSessionFactory();
    }

    /**
     * Builds a SessionFactory, if it hasn't been already.
     */
    public SessionFactory buildIfNeeded() throws DataAccessLayerException{
        if (sessionFactory != null) {
            return sessionFactory;
        }
        try {
            return configureSessionFactory();
        } catch (HibernateException e) {
            throw new DataAccessLayerException(e);
        }
    }
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }


    public Session openSession() throws HibernateException {
        buildIfNeeded();
        return sessionFactory.openSession();
    }

    public void closeFactory() {
        if (sessionFactory != null) {
            try {
                sessionFactory.close();
            } catch (HibernateException ignored) {
                log.error("Couldn't close SessionFactory", ignored);
            }
        }
    }

    public void close(Session session) {
        if (session != null) {
            try {
                session.close();
            } catch (HibernateException ignored) {
                log.error("Couldn't close Session", ignored);
            }
        }
    }

    public void rollback(Transaction tx) {
        try {
            if (tx != null) {
                tx.rollback();
            }
        } catch (HibernateException ignored) {
            log.error("Couldn't rollback Transaction", ignored);
        }
    }
    /**
     *
     * @return
     * @throws HibernateException
     */
    private SessionFactory configureSessionFactory() throws HibernateException {
        Configuration configuration = new Configuration();
        configuration.configure("ch/heigvd/bomberman/server/resources/hibernate.cfg.xml");
        sessionFactory = configuration.buildSessionFactory();
        return sessionFactory;
    }
}