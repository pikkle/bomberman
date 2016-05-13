package ch.heigvd.bomberman.common.game;

import ch.heigvd.bomberman.common.game.Arena.Arena;
import com.j256.ormlite.table.DatabaseTable;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URISyntaxException;

/**
 * Created by matthieu.villard on 09.05.2016.
 */
@DatabaseTable(tableName = "wall")
public class Wall extends Element
{
    public Wall() {

    }

    public Wall(Point2D position, Arena arena) throws URISyntaxException {
        super(position, arena);
    }

    @Override
    public ImageView render() {
        return new ImageView(new Image("ch/heigvd/bomberman/client/img/wall.png"));
    }
}