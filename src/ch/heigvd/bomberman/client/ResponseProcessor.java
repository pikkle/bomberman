package ch.heigvd.bomberman.client;

import ch.heigvd.bomberman.common.communication.Message;
import ch.heigvd.bomberman.common.communication.responses.*;
import ch.heigvd.bomberman.common.game.Direction;

public class ResponseProcessor implements ResponseVisitor {
	private static ResponseProcessor instance = new ResponseProcessor();

	private ResponseProcessor(){	}

	public static ResponseProcessor getInstance() {
		return instance;
	}


	// TODO: implement processing of responses
	@Override
	public Object visit(NoResponse noResponse) {
		return null;
	}

	@Override
	public Direction visit(MoveResponse moveResponse) {
		return null;
	}

	@Override
	public String visit(HelloResponse helloResponse) {
		return null;
	}

	@Override
	public Message visit(SuccessResponse successResponse) {
		return new Message(true, successResponse.getMessage());
	}

	@Override
	public Message visit(ErrorResponse errorResponse) {
		return new Message(false, errorResponse.getMessage());
	}
}