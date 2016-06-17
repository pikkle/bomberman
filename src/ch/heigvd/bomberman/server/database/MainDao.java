package ch.heigvd.bomberman.server.database;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

/**
 * The main data access.
 *
 * @param <T> any elements of the game
 */
public abstract class MainDao<T> {
	private static Log log = LogFactory.getLog(MainDao.class);
	protected Session session;
	protected Transaction tx;
	protected DBManager db;

	public MainDao() {
		db = DBManager.getInstance();
	}

	/**
	 * Creates the object, save it in the session and commit the transaction.
	 *
	 * @param obj the created object
	 */
	public void create(T obj) {
		try {
			startOperation();
			session.save(obj);
			tx.commit();
		} catch (DataAccessLayerException e) {
			handleException(e);
		} finally {
			db.close(session);
		}
	}

	/**
	 * Updates the object.
	 *
	 * @param obj the object to update
	 */
	public void update(T obj) {
		try {
			startOperation();
			session.update(obj);
			tx.commit();
		} catch (DataAccessLayerException e) {
			handleException(e);
		} finally {
			db.close(session);
		}
	}

	/**
	 * Creates or updates the object.
	 *
	 * @param obj the object
	 */
	public void createOrUpdate(T obj) {
		try {
			startOperation();
			session.saveOrUpdate(obj);
			tx.commit();
		} catch (DataAccessLayerException e) {
			handleException(e);
		} finally {
			db.close(session);
		}
	}

	/**
	 * Deletes the object.
	 *
	 * @param obj the object
	 */
	public void delete(T obj) {
		try {
			startOperation();
			session.delete(obj);
			tx.commit();
		} catch (DataAccessLayerException e) {
			handleException(e);
		} finally {
			db.close(session);
		}
	}

	/**
	 * Finds the object with the same id and the same class.
	 *
	 * @param clazz the class of the object
	 * @param id    the id of the object
	 * @return the Object if he finds it
	 */
	public Optional<T> find(Class<T> clazz, Long id) {
		T obj = null;
		try {
			startOperation();
			Query query = session.createQuery("from " + clazz.getSimpleName() + " where id=:id");
			query.setParameter("id", id);
			obj = (T) query.uniqueResult();
			tx.commit();
		} catch (DataAccessLayerException e) {
			handleException(e);
		} finally {
			db.close(session);
		}
		return Optional.ofNullable(obj);
	}

	/**
	 * Finds all the object of the given class.
	 *
	 * @param clazz the class
	 * @return a list of all the objects
	 */
	public List<T> findAll(Class<T> clazz) {
		List<T> objects = null;
		try {
			startOperation();
			Query query = session.createQuery("from " + clazz.getSimpleName());
			objects = query.list();
			tx.commit();
		} catch (DataAccessLayerException e) {
			handleException(e);
		} finally {
			db.close(session);
		}
		return objects;
	}

	/**
	 * Handles an exception
	 *
	 * @param e the exception
	 */
	protected void handleException(DataAccessLayerException e) {
		db.rollback(tx);
		log.fatal("Database error", e);
	}

	/**
	 * Starts an operation
	 *
	 * @throws DataAccessLayerException
	 */
	protected void startOperation() throws DataAccessLayerException {
		session = db.openSession();
		tx = session.beginTransaction();
	}
}