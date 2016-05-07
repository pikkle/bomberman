package ch.heigvd.bomberman.common.communication.responses;

public class HelloResponse extends Response {
   private String message;

   public HelloResponse(String message) {
	  this.message = message;
   }

   public String message() {return message;}

   @Override
   public ResponseType getType() {
	  return ResponseType.HELLO_RESPONSE;
   }
}
