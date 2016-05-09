package ch.heigvd.bomberman.common.communication.requests;

import ch.heigvd.bomberman.common.game.Direction;

/**
 * Projet : GEN_Projet
 * Créé le 05.05.2016.
 *
 * @author Adriano Ruberto
 */
public class MoveRequest extends Request {

   private Direction direction;

   public MoveRequest(Direction direction) {this.direction = direction;}

   @Override
   public RequestType getType() {
	  return RequestType.MOVE;
   }

   public Direction getDirection() {
	  return direction;
   }
}
