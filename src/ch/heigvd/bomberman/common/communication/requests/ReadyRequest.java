package ch.heigvd.bomberman.common.communication.requests;

import ch.heigvd.bomberman.common.communication.responses.Response;

public class ReadyRequest extends Request {
	private boolean state;
	public ReadyRequest(boolean state) {
		this.state = state;
	}

	public boolean getState() {
		return state;
	}

	@Override
	public Response accept(RequestVisitor visitor) {
		return visitor.visit(this);
	}

}
