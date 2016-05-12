package ch.heigvd.bomberman.common.communication.responses;

import java.util.UUID;

/**
 * Projet : GEN_Projet
 * Créé le 05.05.2016.
 *
 * @author Adriano Ruberto
 */
public class MoveResponse extends Response {

	public MoveResponse(UUID uuid) {
		super(uuid);
	}

	@Override
	public void accept(ResponseVisitor visitor) {
		visitor.visit(this);
	}
}
