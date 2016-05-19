package ch.heigvd.bomberman.common.game;

import ch.heigvd.bomberman.common.game.Arena.Arena;
import ch.heigvd.bomberman.common.game.powerups.AddBombPowerUp;
import ch.heigvd.bomberman.common.game.powerups.PowerUp;
import ch.heigvd.bomberman.server.database.arena.elements.ElementDao;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Optional;

/**
 * Projet : GEN_Projet
 * Créé le 05.05.2016.
 *
 * @author Adriano Ruberto
 */
@DatabaseTable(tableName = "wall", daoClass = ElementDao.class)
public class Box extends Element {

    public Box() {
        super();
    }

    public Box(Point position, Arena arena) {
        super(position, arena);
        arena.add(this);
    }
    
    public Optional<PowerUp> open() {
        // TODO return random powerup
        return Optional.of(new AddBombPowerUp(position, arena));
    }

    @Override
    public void accept(ElementVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean isDestructible() {
        return true;
    }

    @Override
    public boolean isBlastAbsorber() {
        return true;
    }

    @Override
    public boolean isTraversable() {
        return false;
    }
}
