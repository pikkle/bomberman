package ch.heigvd.bomberman.common.communication.requests;

import ch.heigvd.bomberman.common.communication.Message;
import ch.heigvd.bomberman.common.communication.responses.Response;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginRequest extends Request<Message> {
	private String username, password;
	public LoginRequest(String username, String password) {
		this.username = username;
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			this.password = new String(digest.digest(password.getBytes(StandardCharsets.UTF_8)));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Response<Message> accept(RequestVisitor visitor) {
		return visitor.visit(this);
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

}