package ch.heigvd.bomberman.common.game;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Projet : GEN_Projet
 * Créé le 07.05.2016.
 *
 * @author Adriano Ruberto
 */

@Entity
@Table(name = "player")
public class Player implements Serializable {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	private Bomberman bomberman;

	@Column(name = "pseudo", nullable = false)
	private String pseudo;

	@Column(name = "password", nullable = true)
	private String password;

	@Column(name = "isAdmin", nullable = false)
	private Boolean isAdmin = false;

	/**
	 * Constructor for Hibernate
	 */
	protected Player() {
		// all persisted classes must define a no-arg constructor with at least package visibility
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
	public int id() {
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
}
