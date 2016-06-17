package ch.heigvd.bomberman.common.communication.requests;

import ch.heigvd.bomberman.common.communication.responses.Response;

import java.io.Serializable;
import java.util.UUID;

public abstract class Request<T> implements Serializable {
	private UUID uuid = UUID.randomUUID();

	public abstract Response<T> accept(RequestVisitor visitor);


	public UUID getID() {
		return uuid;
	}
}