package ch.heigvd.bomberman.common.communication.responses;

import ch.heigvd.bomberman.common.util.Direction;

import java.util.UUID;

/**
 * Corresponds to an empty response.
 * Nothing will be sent to the client.
 */
public class NoResponse extends Response {
	public NoResponse(UUID uuid) {
		super(uuid);
	}

	@Override
	public boolean isSendable() {
		return false;
	}

	@Override
	public Direction accept(ResponseVisitor visitor) {
		visitor.visit(this);
		return null;
	}
}