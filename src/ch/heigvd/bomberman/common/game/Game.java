package ch.heigvd.bomberman.common.game;

import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * A Game is where the statics are handled
 */
@Entity
@Table(name = "game")
public class Game implements Serializable {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@OneToMany(targetEntity = Statistic.class, fetch = FetchType.EAGER, mappedBy = "game")
	@Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.DELETE})
	private List<Statistic> statistics = new LinkedList<>();

	public Game() {
	}

	/**
	 * Gets the id of the game.
	 *
	 * @return the id of the game
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Adds statistics to the game.
	 *
	 * @param statistic the statistic to add
	 */
	public void addStatistic(Statistic statistic) {
		if (!statistics.contains(statistic))
			statistics.add(statistic);
	}

	/**
	 * Remove a statistic from the statistics of the game.
	 *
	 * @param statistic the statistic to delete
	 */
	public void removeStatistic(Statistic statistic) {
		statistics.remove(statistic);
	}

	/**
	 * Gets the statistic of the game.
	 *
	 * @return the statistics
	 */
	public List<Statistic> getStatistics() {
		return statistics;
	}
}