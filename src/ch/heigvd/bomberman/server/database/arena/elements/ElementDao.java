package ch.heigvd.bomberman.server.database.arena.elements;

import ch.heigvd.bomberman.common.game.Element;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.logger.Logger;
import com.j256.ormlite.logger.LoggerFactory;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.StatementBuilder;
import com.j256.ormlite.support.CompiledStatement;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by matthieu.villard on 13.05.2016.
 */
public class ElementDao<T extends Element> extends BaseDaoImpl<T, Long>
{
	private static Logger logger = LoggerFactory.getLogger(ElementDao.class);

	public ElementDao(Class<T> dataClass) throws SQLException {
		super(dataClass);
	}

	public ElementDao(ConnectionSource connectionSource, Class<T> dataClass) throws SQLException {
		super(connectionSource, dataClass);
	}

	public ElementDao(ConnectionSource connectionSource, DatabaseTableConfig<T> tableConfig) throws SQLException {
		super(connectionSource, tableConfig);
	}

	@Override
	public List<T> query(PreparedQuery<T> preparedQuery) throws SQLException {

		/*setObjectFactory(new ObjectFactory<Element>() {
			@Override
			public Element createObject(Constructor<Element> constructor, Class<Element> aClass) throws SQLException {
				try {
					return new Wall();
				} catch (Exception var3) {
					throw SqlExceptionUtil.create("Could not create object for " + constructor.getDeclaringClass(), var3);
				}
			}
		});*/
		CompiledStatement compiledStatement = preparedQuery.compile(connectionSource.getReadOnlyConnection(), StatementBuilder.StatementType.SELECT, -1);
		ElementIterator<T> iterator = new ElementIterator<T>(this.tableInfo.getDataClass(), this, preparedQuery, connectionSource, connectionSource.getReadOnlyConnection(), compiledStatement, preparedQuery.getStatement(), getObjectCache());

		try {
			ArrayList results = new ArrayList();

			while(iterator.hasNextThrow()) {
				results.add(iterator.nextThrow());
			}

			logger.debug("query of \'{}\' returned {} results", preparedQuery.getStatement(), Integer.valueOf(results.size()));
			ArrayList var6 = results;
			return var6;
		} finally {
			iterator.close();
		}
	}

	public T queryForFirst(PreparedQuery<T> preparedQuery) throws SQLException {
		return query(preparedQuery).stream().findFirst().orElse(null);
	}
}
