package ch.heigvd.bomberman.common.communication.requests;

public class ReadyRequest extends Request {
	private boolean state;
	public ReadyRequest(boolean state) {
		this.state = state;
	}

	@Override
	public RequestType getType() {
		return RequestType.READY;
	}

	public boolean getState() {
		return state;
	}
}
