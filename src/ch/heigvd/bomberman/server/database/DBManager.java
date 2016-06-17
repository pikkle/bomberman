package ch.heigvd.bomberman.server.database;

import ch.heigvd.bomberman.server.Server;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

/**
 * Gives functions to manage a database.
 */
public class DBManager {
	private static DBManager instance;
	private static Log log = LogFactory.getLog(DBManager.class);
	private SessionFactory sessionFactory;

	public static synchronized DBManager getInstance() {
		if (instance == null)
			instance = new DBManager();
		return instance;
	}

	/**
	 * Gets a new {@link PlayerDao}.
	 *
	 * @return the new PlayerDao
	 */
	public PlayerDao players() {
		return new PlayerDao();
	}

	/**
	 * Gets a new {@link ArenaDao}.
	 *
	 * @return the new ArenaDao
	 */
	public ArenaDao arenas() {
		return new ArenaDao();
	}

	/**
	 * Gets a new {@link GameDao}.
	 *
	 * @return the new GameDao
	 */
	public GameDao games() {
		return new GameDao();
	}

	/**
	 * Constructs a new Singleton SessionFactory
	 *
	 * @return the configureSessionFactory
	 * @throws DataAccessLayerException
	 */
	public SessionFactory buildSessionFactory() throws DataAccessLayerException {
		if (sessionFactory != null) {
			closeFactory();
		}
		return configureSessionFactory();
	}

	/**
	 * Builds a SessionFactory, if it hasn't been already.
	 *
	 * @return the configureSessionFactory
	 * @throws DataAccessLayerException
	 */
	public SessionFactory buildIfNeeded() throws DataAccessLayerException {
		if (sessionFactory != null) {
			return sessionFactory;
		}
		return configureSessionFactory();
	}

	/**
	 * Gets the {@link SessionFactory}.
	 *
	 * @return the SessionFactory
	 */
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	/**
	 * Opens a {@link Session}, build it if necessary.
	 *
	 * @return the session
	 * @throws DataAccessLayerException
	 */
	public Session openSession() throws DataAccessLayerException {
		buildIfNeeded();
		return sessionFactory.openSession();
	}

	/**
	 * Closes the factory
	 */
	public void closeFactory() {
		if (sessionFactory != null) {
			try {
				sessionFactory.close();
			} catch (Throwable ignored) {
				log.fatal("Couldn't close SessionFactory", ignored);
			}
		}
	}

	/**
	 * Closes the given session
	 *
	 * @param session the session
	 */
	public void close(Session session) {
		if (session != null) {
			try {
				session.close();
			} catch (Throwable ignored) {
				log.fatal("Couldn't close Session", ignored);
			}
		}
	}

	/**
	 * Rollback the transaction
	 *
	 * @param tx the transaction
	 */
	public void rollback(Transaction tx) {
		try {
			if (tx != null) {
				tx.rollback();
			}
		} catch (Throwable ignored) {
			log.fatal("Couldn't rollback Transaction", ignored);
		}
	}

	/**
	 * Configures the session factory with the file at resources/hibernate.cfg.xml
	 *
	 * @return the SessionFactory
	 * @throws DataAccessLayerException
	 */
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