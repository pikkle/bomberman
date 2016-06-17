package ch.heigvd.bomberman.server.database;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

/**
 * Created by matthieu.villard on 30.05.2016.
 */
public abstract class MainDao<T> {
	protected Session session;
	protected Transaction tx;
	protected DBManager db;
	private static Log log = LogFactory.getLog(MainDao.class);

	public MainDao() {
		db = DBManager.getInstance();
	}

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

	protected void handleException(DataAccessLayerException e) {
		db.rollback(tx);
		log.fatal("Database error", e);
	}

	protected void startOperation() throws DataAccessLayerException {
		session = db.openSession();
		tx = session.beginTransaction();
	}
}