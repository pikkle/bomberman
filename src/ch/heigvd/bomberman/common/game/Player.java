package ch.heigvd.bomberman.common.game;

import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 *
 */
@Entity
@Table(name = "player")
public class Player implements Serializable {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private Bomberman bomberman;

	@Column(name = "pseudo", nullable = false)
	private String pseudo;

	@Column(name = "password", nullable = true)
	private String password;

	@Column(name = "isAdmin", nullable = false)
	private Boolean isAdmin = false;

	@Column(name = "isLocked", nullable = false)
	private Boolean isLocked = false;

	@OneToMany(targetEntity = Statistic.class, fetch = FetchType.EAGER, mappedBy = "player")
	@Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.DELETE})
	private List<Statistic> statistics = new LinkedList<>();

	/**
	 * Constructor for Hibernate
	 */
	protected Player() {
	}

	public Player(String pseudo, String password) {
		this.pseudo = pseudo;
		this.password = password;
	}

	/**
	 * Gets the ID of the player.
	 *
	 * @return the ID of the player
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Gets the pseudo of the player.
	 *
	 * @return the pseudo
	 */
	public String getPseudo() {
		return pseudo;
	}

	/**
	 * Sets the pseudo of the player.
	 *
	 * @param pseudo the pseudo
	 */
	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}

	/**
	 * Gets the password of the player.
	 *
	 * @return the password of the player.
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets the password of the player.
	 *
	 * @param password the password of the player.
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Gets if the player is an admin
	 *
	 * @return true if it's an admin
	 */
	public boolean isAdmin() {
		return isAdmin != null && isAdmin;
	}

	/**
	 * Gets if the player is an admin
	 *
	 * @return true if it's an admin
	 */
	public boolean isLocked() {
		return isLocked != null && isLocked;
	}

	/**
	 * Sets the isLocked of the player.
	 *
	 * @param isLocked the isLocked of the player.
	 */
	public void setIsLocked(boolean isLocked) {
		this.isLocked = isLocked;
	}

	/**
	 * Adds statistics at a player.
	 *
	 * @param statistic the statistics to add
	 */
	public void addStatistic(Statistic statistic) {
		if (!statistics.contains(statistic)) {
			statistics.add(statistic);
		}
	}

	/**
	 * Gets the static of the player
	 *
	 * @return the statistics
	 */
	public List<Statistic> getStatistics() {
		return statistics;
	}
}
