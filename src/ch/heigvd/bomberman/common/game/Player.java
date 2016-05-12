package ch.heigvd.bomberman.common.game;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Projet : GEN_Projet
 * Créé le 07.05.2016.
 *
 * @author Adriano Ruberto
 */

@DatabaseTable(tableName = "player")
public class Player implements KeyListener {

	@DatabaseField(generatedId = true) private int id;

	private Bomberman bomberman;

	@DatabaseField(columnName = "pseudo", canBeNull = false) private String pseudo;

	@DatabaseField(columnName = "password", canBeNull = false) private String password;

	Player() {
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

	@Override
	public void keyTyped(KeyEvent e) {


	}

	@Override
	public void keyPressed(KeyEvent e) {

	}

	@Override
	public void keyReleased(KeyEvent e) {

	}
}
