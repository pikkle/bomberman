package ch.heigvd.bomberman.common.communication.responses;

public class SuccessResponse extends Response {
	private String message;
	public SuccessResponse(String message) {
		this.message = message;
	}

	@Override
	public void accept(ResponseVisitor visitor) {
		visitor.visit(this);
	}
}
