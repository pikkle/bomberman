package ch.heigvd.bomberman.common.communication.requests;

import ch.heigvd.bomberman.common.communication.responses.Response;
import ch.heigvd.bomberman.common.game.Direction;

/**
 * Projet : GEN_Projet
 * Créé le 05.05.2016.
 *
 * @author Adriano Ruberto
 */
public class MoveRequest extends Request<Direction> {
	private Direction direction;

	public MoveRequest(Direction direction) {this.direction = direction;}

	public Direction getDirection() {
	  return direction;
   }

	@Override
	public Response accept(RequestVisitor visitor) {
		return visitor.visit(this);
	}
}
