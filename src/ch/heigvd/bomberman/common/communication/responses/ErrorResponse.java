package ch.heigvd.bomberman.common.communication.responses;

import java.util.UUID;

public class ErrorResponse extends Response<Boolean>  {
	private String message;
	public ErrorResponse(UUID uuid, String message) {
		super(uuid);
		this.message = message;
	}

	@Override
	public Boolean accept(ResponseVisitor visitor) {
		return visitor.visit(this);
	}
}
