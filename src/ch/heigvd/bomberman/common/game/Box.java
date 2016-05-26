package ch.heigvd.bomberman.common.game;

import ch.heigvd.bomberman.common.game.Arena.Arena;
import ch.heigvd.bomberman.common.game.powerups.PowerUp;
import ch.heigvd.bomberman.server.database.arena.elements.ElementDao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Optional;

/**
 * Projet : GEN_Projet
 * Créé le 05.05.2016.
 *
 * @author Adriano Ruberto
 */
@DatabaseTable(tableName = "element", daoClass = ElementDao.class)
public class Box extends Element {

    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "powerUp", canBeNull = true)
    private PowerUp powerUp;

    public Box() {
        super();
    }

    public Box(Point position, Arena arena) {
        super(position, arena);
        arena.add(this);
    }

    public void setPowerUp(PowerUp powerUp){
        this.powerUp = powerUp;
    }

    public Optional<PowerUp> getPowerUp() {
        // TODO return random powerup
        return Optional.of(powerUp);
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
