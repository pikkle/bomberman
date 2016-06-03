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
	@Column(name="id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	private Bomberman bomberman;

	@Column(name="pseudo", nullable = false)
	private String pseudo;

	@Column(name="password", nullable = true)
	private String password;

	@Column(name="isAdmin", nullable = false)
	private boolean isAdmin = false;

	protected Player() {
		// all persisted classes must define a no-arg constructor with at least package visibility
	}

	public Player(String pseudo, String password) {
		this.pseudo = pseudo;
		this.password = password;
	}

	public int getId() {
		return id;
	}

	public String getPseudo() {
		return pseudo;
	}

	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isAdmin(){
		return isAdmin;
	}
}
