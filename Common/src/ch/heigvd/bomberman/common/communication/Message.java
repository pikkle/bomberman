package ch.heigvd.bomberman.common.communication;

public class Message {
	private boolean state;
	private String message;

	public Message(boolean state, String message) {
		this.state = state;
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public boolean state() {
		return state;
	}
}
