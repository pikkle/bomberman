package ch.heigvd.bomberman.server.database;

import ch.heigvd.bomberman.common.game.Game;

import java.util.List;
import java.util.Optional;

/**
 * Created by matthieu.villard on 09.06.2016.
 */
public class GameDao extends MainDao<Game> {

	public GameDao() {
		super();
	}

	public Optional<Game> find(Long id) {
		return super.find(Game.class, id);
	}

	public List<Game> findAll() {
		return super.findAll(Game.class);
	}
}