package ch.heigvd.bomberman.server.database.arena.elements;

import ch.heigvd.bomberman.common.game.Point;
import com.google.gson.Gson;
import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.field.types.StringType;
import javafx.geometry.Point2D;

/**
 * Created by matthieu.villard on 15.05.2016.
 */
public class PositionConverter extends StringType
{
	private static final PositionConverter instance = new PositionConverter();

	private PositionConverter() {
		super(SqlType.STRING, new Class<?>[] { Point2D.class });
	}

	public static PositionConverter getSingleton() {
		return instance;
	}

	@Override
	public Object javaToSqlArg(FieldType fieldType, Object javaObject) {
		Point position = (Point) javaObject;
		return position != null ? getJsonFromMyFieldClass(position) : null;
	}

	@Override
	public Object sqlArgToJava(FieldType fieldType, Object sqlArg, int columnPos) {
		return sqlArg != null ? getMyFieldClassFromJson((String) sqlArg) : null;
	}

	private String getJsonFromMyFieldClass(Point position) {
		Gson gson = new Gson();
		return gson.toJson(position);
	}

	private Point getMyFieldClassFromJson(String json) {
		Gson gson = new Gson();
		return gson.fromJson(json, Point.class);
	}
}
