package ch.heigvd.bomberman.server.database.arena.elements;

import ch.heigvd.bomberman.common.game.Element;
import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.ObjectCache;
import com.j256.ormlite.logger.Logger;
import com.j256.ormlite.logger.LoggerFactory;
import com.j256.ormlite.misc.SqlExceptionUtil;
import com.j256.ormlite.stmt.GenericRowMapper;
import com.j256.ormlite.support.CompiledStatement;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.support.DatabaseConnection;
import com.j256.ormlite.support.DatabaseResults;

import java.sql.SQLException;

/**
 * Created by matthieu.villard on 14.05.2016.
 */
public class ElementIterator<T extends Element> implements CloseableIterator<T> {
	private static final Logger logger = LoggerFactory.getLogger(ElementIterator.class);
	private final Class<?> dataClass;
	private final Dao<T, Long> classDao;
	private final ConnectionSource connectionSource;
	private final DatabaseConnection connection;
	private final CompiledStatement compiledStmt;
	private final DatabaseResults results;
	private final GenericRowMapper<T> rowMapper;
	private final String statement;
	private boolean first = true;
	private boolean closed;
	private boolean alreadyMoved;
	private T last;
	private int rowC;

	public ElementIterator(Class<?> dataClass, Dao<T, Long> classDao, GenericRowMapper<T> rowMapper, ConnectionSource connectionSource, DatabaseConnection connection, CompiledStatement compiledStmt, String statement, ObjectCache objectCache) throws SQLException {
		this.dataClass = dataClass;
		this.classDao = classDao;
		this.rowMapper = rowMapper;
		this.connectionSource = connectionSource;
		this.connection = connection;
		this.compiledStmt = compiledStmt;
		this.results = compiledStmt.runQuery(objectCache);
		this.statement = statement;
		if(statement != null) {
			logger.debug("starting iterator @{} for \'{}\'", Integer.valueOf(this.hashCode()), statement);
		}

	}

	public boolean hasNextThrow() throws SQLException {
		if(this.closed) {
			return false;
		} else if(this.alreadyMoved) {
			return true;
		} else {
			boolean result;
			if(this.first) {
				this.first = false;
				result = this.results.first();
			} else {
				result = this.results.next();
			}

			if(!result) {
				this.close();
			}

			this.alreadyMoved = true;
			return result;
		}
	}

	public boolean hasNext() {
		try {
			return this.hasNextThrow();
		} catch (SQLException var2) {
			this.last = null;
			this.closeQuietly();
			throw new IllegalStateException("Errors getting more results of " + this.dataClass, var2);
		}
	}

	public T first() throws SQLException {
		if(this.closed) {
			return null;
		} else {
			this.first = false;
			return this.results.first()?this.getCurrent():null;
		}
	}

	public T previous() throws SQLException {
		if(this.closed) {
			return null;
		} else {
			this.first = false;
			return this.results.previous()?this.getCurrent():null;
		}
	}

	public T current() throws SQLException {
		return this.closed?null:(this.first?this.first():this.getCurrent());
	}

	public T nextThrow() throws SQLException {
		if(this.closed) {
			return null;
		} else {
			if(!this.alreadyMoved) {
				boolean hasResult;
				if(this.first) {
					this.first = false;
					hasResult = this.results.first();
				} else {
					hasResult = this.results.next();
				}

				if(!hasResult) {
					this.first = false;
					return null;
				}
			}

			this.first = false;
			return this.getCurrent();
		}
	}

	public T next() {
		SQLException sqlException = null;

		try {
			T e = this.nextThrow();
			if(e != null) {
				return e;
			}
		} catch (SQLException var3) {
			sqlException = var3;
		}

		this.last = null;
		this.closeQuietly();
		throw new IllegalStateException("Could not get next result for " + this.dataClass, sqlException);
	}

	public T moveRelative(int offset) throws SQLException {
		if(this.closed) {
			return null;
		} else {
			this.first = false;
			return this.results.moveRelative(offset)?this.getCurrent():null;
		}
	}

	public void removeThrow() throws SQLException {
		if(this.last == null) {
			throw new IllegalStateException("No last " + this.dataClass + " object to remove. Must be called after a call to next.");
		} else if(this.classDao == null) {
			throw new IllegalStateException("Cannot remove " + this.dataClass + " object because classDao not initialized");
		} else {
			try {
				this.classDao.delete(this.last);
			} finally {
				this.last = null;
			}

		}
	}

	public void remove() {
		try {
			this.removeThrow();
		} catch (SQLException var2) {
			this.closeQuietly();
			throw new IllegalStateException("Could not delete " + this.dataClass + " object " + this.last, var2);
		}
	}

	public void close() throws SQLException {
		if(!this.closed) {
			this.compiledStmt.close();
			this.closed = true;
			this.last = null;
			if(this.statement != null) {
				logger.debug("closed iterator @{} after {} rows", Integer.valueOf(this.hashCode()), Integer.valueOf(this.rowC));
			}

			this.connectionSource.releaseConnection(this.connection);
		}

	}

	public void closeQuietly() {
		try {
			this.close();
		} catch (SQLException var2) {
			;
		}

	}

	public DatabaseResults getRawResults() {
		return this.results;
	}

	public void moveToNext() {
		this.last = null;
		this.first = false;
		this.alreadyMoved = false;
	}

	private T getCurrent() throws SQLException {
		if(this.classDao != null) {
			try {
				Class clazz = Class.forName(this.results.getString(this.results.findColumn("discr")));
				classDao.setObjectFactory(new ElementFactory<T>(clazz));
			} catch (ClassNotFoundException e) {
				throw SqlExceptionUtil.create("Could not create object for " +this.results.getString(this.results.findColumn("discr")), e);
			}
		}
		this.last = this.rowMapper.mapRow(this.results);
		this.alreadyMoved = false;
		++this.rowC;
		return this.last;
	}
}
