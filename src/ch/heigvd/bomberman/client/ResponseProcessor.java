package ch.heigvd.bomberman.client;

import ch.heigvd.bomberman.common.communication.responses.*;

public class ResponseProcessor implements ResponseVisitor {
	private static ResponseProcessor instance = new ResponseProcessor();

	private ResponseProcessor(){	}

	public static ResponseProcessor getInstance() {
		return instance;
	}


	// TODO: implement processing of responses
	@Override
	public void visit(NoResponse noResponse) {

	}

	@Override
	public void visit(MoveResponse moveResponse) {

	}

	@Override
	public void visit(SuccessResponse successResponse) {

	}

	@Override
	public void visit(HelloResponse helloResponse) {

	}
}
