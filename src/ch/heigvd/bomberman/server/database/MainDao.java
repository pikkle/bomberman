package ch.heigvd.bomberman.server.database;

import org.hibernate.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Created by matthieu.villard on 30.05.2016.
 */
public abstract class MainDao<T> {
    protected Session session;
    protected Transaction tx;
    protected DBManager db;

    public MainDao() throws SQLException {
        db = DBManager.getInstance();
        db.buildIfNeeded();
    }

    public void create(T obj) {
        try {
            startOperation();
            session.save(obj);
            tx.commit();
        } catch (HibernateException e) {
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
        } catch (HibernateException e) {
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
        } catch (HibernateException e) {
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
        } catch (HibernateException e) {
            handleException(e);
        } finally {
            db.close(session);
        }
    }

    public Optional<T> find(Class<T> clazz, Long id) {
        T obj = null;
        try {
            startOperation();
            obj = (T)session.get(clazz, id);
            tx.commit();
        } catch (HibernateException e) {
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
            Query query = session.createQuery("from " + clazz.getName());
            objects = query.list();
            tx.commit();
        } catch (HibernateException e) {
            handleException(e);
        } finally {
            db.close(session);
        }
        return objects;
    }

    protected void handleException(HibernateException e) throws DataAccessLayerException {
        db.rollback(tx);
        throw new DataAccessLayerException(e);
    }

    protected void startOperation() throws HibernateException {
        session = db.openSession();
        tx = session.beginTransaction();
    }
}