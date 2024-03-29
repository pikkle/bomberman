package ch.heigvd.bomberman.common.communication.responses;

import ch.heigvd.bomberman.common.communication.Message;

import java.util.UUID;

public class SuccessResponse extends Response<Message> {
	private String message;

	public SuccessResponse(UUID uuid, String message) {
		super(uuid);
		this.message = message;
	}

	@Override
	public Message accept(ResponseVisitor visitor) {
		return visitor.visit(this);
	}

	public String getMessage() {
		return message;
	}
}
