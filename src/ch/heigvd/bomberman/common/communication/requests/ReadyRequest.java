package ch.heigvd.bomberman.common.communication.requests;

import ch.heigvd.bomberman.common.communication.responses.Response;
import ch.heigvd.bomberman.common.game.Bomberman;

public class ReadyRequest extends Request<Bomberman> {

	private boolean state;

	public ReadyRequest(boolean state) {
		this.state = state;
	}

	public boolean getState() {
		return state;
	}

	@Override
	public Response<Bomberman> accept(RequestVisitor visitor) {
		return visitor.visit(this);
	}

}