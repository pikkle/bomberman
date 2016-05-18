package ch.heigvd.bomberman.server.database.arena.elements;

import ch.heigvd.bomberman.common.game.Element;
import com.j256.ormlite.misc.SqlExceptionUtil;
import com.j256.ormlite.table.DatabaseTableConfig;
import com.j256.ormlite.table.ObjectFactory;

import java.lang.reflect.Constructor;
import java.sql.SQLException;

/**
 * Created by matthieu.villard on 15.05.2016.
 */
public class ElementFactory<T extends Element> implements ObjectFactory<T>
{

	private Class<T> clazz;

	public ElementFactory(Class<T> clazz){
		this.clazz = clazz;
	}

	@Override
	public T createObject(Constructor<T> constructor, Class<T> aClass) throws SQLException {
		constructor = DatabaseTableConfig.findNoArgConstructor(clazz);
		try {
			return constructor.newInstance(new Object[0]);
		}  catch (Exception var3) {
			throw SqlExceptionUtil.create("Could not create object for " + constructor.getDeclaringClass(), var3);
		}
	}
}
