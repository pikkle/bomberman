package ch.heigvd.bomberman.server.database;

import ch.heigvd.bomberman.common.game.Arena;

import java.util.List;
import java.util.Optional;

/**
 * Data access for the arena
 */
public class ArenaDao extends MainDao<Arena> {

	public ArenaDao() {
		super();
	}

	/**
	 * Finds the arena by an id.
	 *
	 * @param id the id of the arena
	 * @return an arena, can't be empty if no arena match
	 */
	public Optional<Arena> find(Long id) {
		return super.find(Arena.class, id);
	}

	/**
	 * Gets all the arena.
	 *
	 * @return all the arena
	 */
	public List<Arena> findAll() {
		return super.findAll(Arena.class);
	}
}
