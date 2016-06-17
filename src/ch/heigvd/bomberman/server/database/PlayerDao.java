package ch.heigvd.bomberman.server.database;

import ch.heigvd.bomberman.common.game.Player;
import org.hibernate.Query;

import java.util.List;
import java.util.Optional;

/**
 * Created by matthieu.villard on 30.05.2016.
 */
public class PlayerDao extends MainDao<Player> {

	public PlayerDao() {
		super();
	}

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

	public Optional<Player> find(long id) {
		return super.find(Player.class, id);
	}

	public List<Player> findAll() {
		return super.findAll(Player.class);
	}
}
