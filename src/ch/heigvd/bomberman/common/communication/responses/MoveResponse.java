package ch.heigvd.bomberman.common.communication.responses;

/**
 * Projet : GEN_Projet
 * Créé le 05.05.2016.
 *
 * @author Adriano Ruberto
 */
public class MoveResponse extends Response {

   @Override
   public ResponseType getType() {
	  return ResponseType.MOVE_RESPONSE;
   }
}
