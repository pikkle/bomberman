package ch.heigvd.bomberman.common.communication.responses;

import java.util.UUID;

public class SuccessResponse extends Response {
	private String message;
	public SuccessResponse(UUID uuid, String message) {
		super(uuid);
		this.message = message;
	}

	@Override
	public void accept(ResponseVisitor visitor) {
		visitor.visit(this);
	}
}
