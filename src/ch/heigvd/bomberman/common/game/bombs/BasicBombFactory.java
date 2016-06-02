package ch.heigvd.bomberman.common.game.bombs;

import ch.heigvd.bomberman.common.game.Arena.Arena;
import ch.heigvd.bomberman.common.game.Point;

import java.util.Optional;

/**
 * Created by Adriano on 12.05.2016.
 */
public class BasicBombFactory extends BombFactory {
	public BasicBombFactory(Arena arena) {
		super(3, 10, arena, 1);
	}

	@Override
	public Optional<BasicBomb> create(Point position) {
		if (nbBomb <= 0)
			return Optional.empty();

		try {
			BasicBomb b = new BasicBomb(position, countdown, blastRange, arena);
			b.addObserver(this);
			nbBomb--;
			return Optional.of(b);
		} catch (RuntimeException e) {
			return Optional.empty();
		}
	}
}