package ch.heigvd.bomberman.common.communication.requests;

import ch.heigvd.bomberman.common.communication.Message;
import ch.heigvd.bomberman.common.communication.responses.Response;
import ch.heigvd.bomberman.common.game.Room;

import java.util.List;

public class LoginRequest extends Request<Message> {
	private String username, password;
	public LoginRequest(String username, String password) {
		this.username = username;
		this.password = password;
	}

	@Override
	public Response<List<Room>> accept(RequestVisitor visitor) {
		return visitor.visit(this);
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}
}