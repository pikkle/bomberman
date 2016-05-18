package ch.heigvd.bomberman.common.game;

import ch.heigvd.bomberman.common.game.Arena.Arena;
import ch.heigvd.bomberman.common.game.powerups.AddBombPowerUp;
import ch.heigvd.bomberman.common.game.powerups.PowerUp;
import ch.heigvd.bomberman.server.database.arena.elements.ElementDao;
import com.j256.ormlite.table.DatabaseTable;
import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;

import java.util.Optional;

/**
 * Projet : GEN_Projet
 * Créé le 05.05.2016.
 *
 * @author Adriano Ruberto
 */
@DatabaseTable(tableName = "wall", daoClass = ElementDao.class)
public class Box extends DestructibleElement {

    public Box() {
        super();
    }

    public Box(Point2D position, Arena arena) {
        super(position, arena);
    }

    @Override
    public ImageView render() {
        return new ImageView(new javafx.scene.image.Image("ch/heigvd/bomberman/client/img/box.png"));
    }

    public Optional<PowerUp> open() {
        // TODO return random powerup
        return Optional.of(new AddBombPowerUp(position, arena));
    }
}