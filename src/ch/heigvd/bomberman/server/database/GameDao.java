package ch.heigvd.bomberman.server.database;

import ch.heigvd.bomberman.common.game.Game;

import java.util.List;
import java.util.Optional;

/**
 * Data access for the games
 */
public class GameDao extends MainDao<Game> {

	public GameDao() {
		super();
	}

	/**
	 * Finds the game by an id.
	 *
	 * @param id the id of the game
	 * @return the founded game, can't be empty if no game match
	 */
	public Optional<Game> find(Long id) {
		return super.find(Game.class, id);
	}

	/**
	 * Gets all the game.
	 *
	 * @return all the game
	 */
	public List<Game> findAll() {
		return super.findAll(Game.class);
	}
}