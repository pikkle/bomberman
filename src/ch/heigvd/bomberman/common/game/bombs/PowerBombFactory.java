package ch.heigvd.bomberman.common.game.bombs;

import ch.heigvd.bomberman.common.game.Arena.Arena;
import ch.heigvd.bomberman.common.game.Point;

import java.util.Optional;

/**
 * Created by Adriano on 09.06.2016.
 */
public class PowerBombFactory extends BombFactory {

	public PowerBombFactory(Arena arena) {
		super(3, 0, arena, 1);
	}

	public PowerBombFactory(BombFactory bf) {
		super(bf.countdown, 0, bf.arena, bf.nbBomb);
	}

	@Override
	public Optional<PowerBomb> create(Point position) {
		if (nbBomb <= 0)
			return Optional.empty();

		PowerBomb b = new PowerBomb(position, countdown, arena);
		b.addObserver(this);
		nbBomb--;
		return Optional.of(b);
	}
}
