package ch.heigvd.bomberman.server.database.arena;

import ch.heigvd.bomberman.common.game.Box;
import ch.heigvd.bomberman.common.game.Element;
import ch.heigvd.bomberman.common.game.Wall;
import com.j256.ormlite.misc.SqlExceptionUtil;
import com.j256.ormlite.table.ObjectFactory;

import java.lang.reflect.Constructor;
import java.sql.SQLException;

/**
 * Created by matthieu.villard on 15.05.2016.
 */
public class ElementFactory<T extends Element> implements ObjectFactory<Element>
{

	private Class clazz;

	public ElementFactory(Class<T> clazz){
		this.clazz = clazz;
	}

	@Override
	public Element createObject(Constructor<Element> constructor, Class<Element> aClass) throws SQLException {
		switch (clazz.getSimpleName()){
			case "Wall":
				return new Wall();
			case "Box":
				return new Box();
		}
		try {
			return constructor.newInstance(new Object[0]);
		}  catch (Exception var3) {
			throw SqlExceptionUtil.create("Could not create object for " + constructor.getDeclaringClass(), var3);
		}
	}
}
