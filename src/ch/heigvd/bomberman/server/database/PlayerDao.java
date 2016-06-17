package ch.heigvd.bomberman.server.database;

import ch.heigvd.bomberman.common.game.Player;
import org.hibernate.Query;

import java.util.List;
import java.util.Optional;

/**
 * Data access for the player
 */
public class PlayerDao extends MainDao<Player> {

	public PlayerDao() {
		super();
	}

	/**
	 * Finds a player by is pseudo.
	 *
	 * @param pseudo the pseudo of the player
	 * @return the player if he is founded
	 */
	public Optional<Player> findOneByPseudo(String pseudo) {
		Player player = null;
		try {
			startOperation();
			Query query = session.createQuery("from Player where pseudo=:pseudo");
			query.setParameter("pseudo", pseudo);
			player = (Player) query.uniqueResult();
			tx.commit();
		} catch (DataAccessLayerException e) {
			handleException(e);
		} finally {
			db.close(session);
		}
		return Optional.ofNullable(player);
	}

	/**
	 * Finds a player by his id.
	 *
	 * @param id the id
	 * @return the player if he is founded
	 */
	public Optional<Player> find(long id) {
		return super.find(Player.class, id);
	}

	/**
	 * Finds all the players.
	 *
	 * @return the player
	 */
	public List<Player> findAll() {
		return super.findAll(Player.class);
	}
}
