package ch.heigvd.bomberman.common.communication.requests;

/**
 * Projet : GEN_Projet
 * Créé le 05.05.2016.
 *
 * @author Adriano Ruberto
 */
public class MoveRequest extends Request {

   @Override
   public RequestType getType() {
	  return RequestType.MOVE_REQUEST;
   }
}
