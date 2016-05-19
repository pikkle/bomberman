package ch.heigvd.bomberman.server.database.arena.elements;

import com.google.gson.Gson;
import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.field.types.StringType;
import javafx.geometry.Point2D;

import java.util.Collection;

/**
 * Created by matthieu.villard on 15.05.2016.
 */
public class PositionsConverter extends StringType
{
    private static final PositionsConverter instance = new PositionsConverter();

    private PositionsConverter() {
        super(SqlType.STRING, new Class<?>[] { Point2D.class });
    }

    public static PositionsConverter getSingleton() {
        return instance;
    }

    @Override
    public Object javaToSqlArg(FieldType fieldType, Object javaObject) {
        Collection<Point2D> positions = (Collection) javaObject;
        return positions != null ? getJsonFromMyFieldClass(positions) : null;
    }

    @Override
    public Object sqlArgToJava(FieldType fieldType, Object sqlArg, int columnPos) {
        return sqlArg != null ? getMyFieldClassFromJson((String) sqlArg) : null;
    }

    private String getJsonFromMyFieldClass(Collection<Point2D> positions) {
        Gson gson = new Gson();
        return gson.toJson(positions);
    }

    private Collection<Point2D> getMyFieldClassFromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, Collection.class);
    }
}
