package ch.heigvd.bomberman.common.communication.responses;

import java.util.UUID;

public class HelloResponse extends Response {
   private String message;

   public HelloResponse(UUID uuid, String message) {
	   super(uuid);
	   this.message = message;
   }

   public String message() {return message;}

	@Override
	public void accept(ResponseVisitor visitor) {
		visitor.visit(this);
	}
}
