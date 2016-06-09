package ch.heigvd.bomberman.common.game;

import ch.heigvd.bomberman.common.game.Arena.Arena;
import ch.heigvd.bomberman.common.game.powerups.*;
import org.hibernate.annotations.Cascade;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.util.Optional;
import java.util.Random;

/**
 * Projet : GEN_Projet
 * Créé le 05.05.2016.
 *
 * @author Adriano Ruberto
 */
@Entity
public class Box extends Element {

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "powerup_id", nullable = true)
	@Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.DELETE})
	private PowerUp powerUp;

	public Box(Point position, Arena arena) {
		super(position, arena);
	}

	public Box() {
	}

	public Optional<PowerUp> getPowerUp() {

		if (powerUp != null)
			return Optional.empty();

		int gaps = 25;
		int p = new Random().nextInt(gaps * 10);

		if (p < gaps) {
			return Optional.of(new BombDownPowerUp(position, arena));
		} else if (p < gaps * 2) {
			return Optional.of(new BombUpPowerUp(position, arena));
		} else if (p < gaps * 3) {
			return Optional.of(new FireDownPowerUp(position, arena));
		} else if (p < gaps * 4) {
			return Optional.of(new FireUpPowerUp(position, arena));
		} else if (p < gaps * 5) {
			return Optional.of(new FullFirePowerUp(position, arena));
		} else if (p < gaps * 6) {
			return Optional.of(new PowerBombPowerUp(position, arena));
		} else if (p < gaps * 7) {
			return Optional.of(new RedBombPowerUp(position, arena));
		} else {
			return Optional.empty();
		}
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
	public String getPath() {
		return "ch/heigvd/bomberman/client/img/box.png";
	}
}
