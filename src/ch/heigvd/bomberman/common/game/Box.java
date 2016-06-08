package ch.heigvd.bomberman.common.game;

import ch.heigvd.bomberman.common.game.Arena.Arena;
import ch.heigvd.bomberman.common.game.powerups.PowerUp;
import org.hibernate.annotations.Cascade;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.util.Optional;

/**
 * Projet : GEN_Projet
 * Créé le 05.05.2016.
 *
 * @author Adriano Ruberto
 */
@Entity
public class Box extends Element {

	@OneToOne(fetch = FetchType.EAGER) @JoinColumn(name = "powerup_id", nullable = true)
	@Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.DELETE})
	private PowerUp powerUp;

	public Box(Point position, Arena arena) {
		super(position, arena);
	}

	public Box() {}

	public Optional<PowerUp> getPowerUp() {
		// TODO return random powerup
		return Optional.ofNullable(powerUp);
	}

	public void setPowerUp(PowerUp powerUp) {
		this.powerUp = powerUp;
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

	@Override
	public void delete() {
		super.delete();
		getPowerUp().ifPresent(arena::add);
	}

	@Override
	protected String getPath() {
		return "ch/heigvd/bomberman/client/img/box.png";
	}
}
