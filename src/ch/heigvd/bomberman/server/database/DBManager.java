package ch.heigvd.bomberman.server.database;

import ch.heigvd.bomberman.server.Server;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

/**
 * Created by matthieu.villard on 26.05.2016.
 */
public class DBManager {
    private static DBManager instance;
    private static Log log = LogFactory.getLog(DBManager.class);
    private SessionFactory sessionFactory;

    public static synchronized DBManager getInstance() {
        if (instance  == null)
            instance  = new DBManager();
        return instance;
    }

    public PlayerDao players() {
        return new PlayerDao();
    }

    public ArenaDao arenas() {
        return new ArenaDao();
    }

    public GameDao games() {
        return new GameDao();
    }

    /**
     * Constructs a new Singleton SessionFactory
     * @return
     * @throws HibernateException
     */
    public SessionFactory buildSessionFactory() throws DataAccessLayerException {
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
        return configureSessionFactory();
    }
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }


    public Session openSession() throws DataAccessLayerException {
        buildIfNeeded();
        return sessionFactory.openSession();
    }

    public void closeFactory() {
        if (sessionFactory != null) {
            try {
                sessionFactory.close();
            } catch (Throwable ignored) {
                log.fatal("Couldn't close SessionFactory", ignored);
            }
        }
    }

    public void close(Session session) {
        if (session != null) {
            try {
                session.close();
            } catch (Throwable ignored) {
                log.fatal("Couldn't close Session", ignored);
            }
        }
    }

    public void rollback(Transaction tx) {
        try {
            if (tx != null) {
                tx.rollback();
            }
        } catch (Throwable ignored) {
            log.fatal("Couldn't rollback Transaction", ignored);
        }
    }

    private SessionFactory configureSessionFactory() throws DataAccessLayerException {
        try {
            Configuration configuration = new Configuration();
            configuration.configure(Server.class.getResource("resources/hibernate.cfg.xml"));
            sessionFactory = configuration.buildSessionFactory();
            return sessionFactory;
        } catch (Throwable ignored) {
            throw new DataAccessLayerException(ignored);
        }
    }
}