package ch.heigvd.bomberman.common.communication.responses;

import ch.heigvd.bomberman.common.game.Direction;

import java.util.UUID;

/**
 * Projet : GEN_Projet
 * Créé le 05.05.2016.
 *
 * @author Adriano Ruberto
 */
public class MoveResponse extends Response<Direction> {

	public MoveResponse(UUID uuid) {
		super(uuid);
	}

	@Override
	public Direction accept(ResponseVisitor visitor) {
		return visitor.visit(this);
	}
}