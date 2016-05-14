package ch.heigvd.bomberman.server.database.arena;

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
public class ElementDao extends BaseDaoImpl<Element, Long>
{
	private static Logger logger = LoggerFactory.getLogger(ElementDao.class);

	public ElementDao() throws SQLException {
		super(Element.class);
	}

	public ElementDao(Class dataClass) throws SQLException {
		super(dataClass);
	}

	public ElementDao(ConnectionSource connectionSource, Class<Element> dataClass) throws SQLException {
		super(connectionSource, dataClass);
	}

	public ElementDao(ConnectionSource connectionSource, DatabaseTableConfig<Element> tableConfig) throws SQLException {
		super(connectionSource, tableConfig);
	}

	@Override
	public List<Element> query(PreparedQuery<Element> preparedQuery) throws SQLException {

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
		ElementIterator iterator = new ElementIterator(this.tableInfo.getDataClass(), this, preparedQuery, connectionSource, connectionSource.getReadOnlyConnection(), compiledStatement, preparedQuery.getStatement(), getObjectCache());

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
}
