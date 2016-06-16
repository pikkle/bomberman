package ch.heigvd.bomberman.common.game;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Duration;
import java.util.Comparator;
import java.util.stream.Collectors;

/**
 * A statistic is a data for a game and a player.
 */
@Entity
@Table(name = "statistic")
public class Statistic implements Serializable {

	@Id
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "game_id", nullable = false)
	private Game game;

	@Id
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "player_id", nullable = false)
	private Player player;

	@Column(name = "suvivalTime", nullable = false)
	private Duration survivalTime;

	@Column(name = "kills")
	private int kills = 0;

	@Column(name = "bombs")
	private int bombs = 0;

	/**
	 * Constructor for Hibernate
	 */
	public Statistic() {
	}

	public Statistic(Player player, Game game) {
		this.player = player;
		this.game = game;
		game.addStatistic(this);
	}

	/**
	 * Gets the game.
	 *
	 * @return the game
	 */
	public Game getGame() {
		return game;
	}

	/**
	 * Sets the survival time.
	 *
	 * @param survivalTime the survival time
	 */
	public void setSurvivalTime(Duration survivalTime) {
		this.survivalTime = survivalTime;
	}

	/**
	 * Gets the survival time.
	 *
	 * @return the survival time
	 */
	public Duration getSurvivalTime() {
		return survivalTime;
	}

	/**
	 * Increments the number of kills.
	 */
	public void kill() {
		kills++;
	}

	/**
	 * Gets the number of kills
	 *
	 * @return the number of kills
	 */
	public int getKills() {
		return kills;
	}

	/**
	 * Increments the number of dropped bombs.
	 */
	public void dropBomb() {
		bombs++;
	}

	/**
	 * Gets the number of bomb dropped.
	 *
	 * @return the number of bomb dropped
	 */
	public int getBombs() {
		return bombs;
	}

	/**
	 * Gets a rank based on the statistic.
	 *
	 * @return the rank
	 */
	public int getRank() {
		return game.getStatistics().stream().sorted(Comparator.nullsFirst((a, b) -> (a.survivalTime != null && b
				.survivalTime != null) ? b.survivalTime.compareTo(a.survivalTime) : 1)).collect(Collectors.toList()).indexOf(this) + 1;
	}
}
