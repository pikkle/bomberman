package ch.heigvd.bomberman.common.communication.responses;

public class HelloResponse extends Response {
   String message;

   public HelloResponse(String message) {
	  super(ResponseType.HELLO_RESPONSE, message);
	  this.message = message;
   }

   public String message() {return message;}
}
