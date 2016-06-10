package ch.heigvd.bomberman.common.game;

import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

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
	private Long id;

	private Bomberman bomberman;

	@Column(name="pseudo", nullable = false)
	private String pseudo;

	@Column(name="password", nullable = true)
	private String password;

	@Column(name="isAdmin", nullable = false)
	private Boolean isAdmin = false;

	@OneToMany(targetEntity = Statistic.class, fetch = FetchType.EAGER, mappedBy="player")
	@Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.DELETE})
	private List<Statistic> statistics = new LinkedList<>();

	Player() {
		// all persisted classes must define a no-arg constructor with at least package visibility
	}

	public Player(String pseudo, String password) {
		this.pseudo = pseudo;
		this.password = password;
	}

	public Long getId() {
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
		return isAdmin != null && isAdmin;
	}

	public  void addStatistic(Statistic statistic){
		if(!statistics.contains(statistic)){
			statistics.add(statistic);
		}
	}

	public List<Statistic> getStatistics(){
		return statistics;
	}
}
