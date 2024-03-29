package ch.heigvd.bomberman.common.communication.requests;


import ch.heigvd.bomberman.common.communication.Message;
import ch.heigvd.bomberman.common.communication.responses.Response;

public final class AccountModifyRequest extends Request<Message> {
	private String username;
	private String password;

	/**
	 * Creates an AccountCreation request. The parameters should be encrypted.
	 *
	 * @param username of the account
	 * @param password of the account
	 */
	public AccountModifyRequest(String username, String password) {
		this.username = username;
		this.password = Password.generateHash(password);
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	@Override
	public Response<Message> accept(RequestVisitor visitor) {
		return visitor.visit(this);
	}
}