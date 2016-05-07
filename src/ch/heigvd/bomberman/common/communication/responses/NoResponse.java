package ch.heigvd.bomberman.common.communication.responses;

/**
 * Corresponds to an empty response.
 * Nothing will be sent to the client.
 */
public class NoResponse extends Response {

   @Override
   public ResponseType getType() {
	  return ResponseType.NO_RESPONSE;
   }
}
