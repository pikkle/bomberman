package ch.heigvd.bomberman.common.communication.responses;

import ch.heigvd.bomberman.common.game.Bomberman;

import java.util.UUID;

/**
 * Projet : GEN_Projet
 * Créé le 05.05.2016.
 *
 * @author Adriano Ruberto
 */
public class MoveResponse extends Response<Bomberman> {

	private Bomberman bomberman;

	public MoveResponse(UUID uuid, Bomberman bomberman) {
		super(uuid);
		this.bomberman = bomberman;
	}

	public Bomberman getBomberman(){
		return bomberman;
	}

	@Override
	public Bomberman accept(ResponseVisitor visitor) {
		return visitor.visit(this);
	}
}