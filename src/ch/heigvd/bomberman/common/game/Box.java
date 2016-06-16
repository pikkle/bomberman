package ch.heigvd.bomberman.common.game;

import ch.heigvd.bomberman.common.game.powerups.PowerUp;
import ch.heigvd.bomberman.common.game.powerups.RandomPowerUpFactory;
import ch.heigvd.bomberman.common.game.util.Point;
import org.hibernate.annotations.Cascade;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.util.Optional;

/**
 * A box is a destructible element which can have a power up in it.
 * When destroyed, if there's a power up, it will be dropped.
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

	/**
	 * Drops an optional of a power up.
	 * It can be empty if no power up is in the box.
	 * <p>
	 * {@link ch.heigvd.bomberman.common.game.powerups.RandomPowerUpFactory#generate}
	 *
	 * @return an optional of a power up
	 */
	public Optional<PowerUp> dropPowerUp() {
		if (powerUp != null)
			return Optional.of(powerUp);

		return RandomPowerUpFactory.generate(position, arena);
	}

	/**
	 * Sets the power up.
	 *
	 * @param powerUp the new power up
	 */
	public void setPowerUp(PowerUp powerUp) {
		this.powerUp = powerUp;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isDestructible() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isBlastAbsorber() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isTraversable() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void delete() {
		super.delete();
		dropPowerUp().ifPresent(arena::add);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getPath() {
		return "ch/heigvd/bomberman/common/game/img/box.png";
	}
}
