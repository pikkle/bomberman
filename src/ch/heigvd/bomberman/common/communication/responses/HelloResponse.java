package ch.heigvd.bomberman.common.communication.responses;

import ch.heigvd.bomberman.common.game.Direction;

import java.util.UUID;

public class HelloResponse extends Response<String> {
	private String message;

	public HelloResponse(UUID uuid, String message) {
		super(uuid);
		this.message = message;
	}

	public String message() {return message;}

	@Override
	public String accept(ResponseVisitor visitor) {
		return visitor.visit(this);
	}
}