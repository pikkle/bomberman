package ch.heigvd.bomberman.server;

import ch.heigvd.bomberman.common.communication.requests.*;
import ch.heigvd.bomberman.common.communication.responses.HelloResponse;
import ch.heigvd.bomberman.common.communication.responses.NoResponse;
import ch.heigvd.bomberman.common.communication.responses.Response;

public class RequestProcessor implements RequestVisitor {
	private static RequestProcessor instance = new RequestProcessor();

	private RequestProcessor(){	}

	public static RequestVisitor getInstance() {
		return instance;
	}

	public Response visit(HelloRequest r){
		HelloRequest request = (HelloRequest) r;
		System.out.println("Received message: ");
		System.out.println(request.getMessage());
		return new HelloResponse("Hello !");
	}

	@Override
	public Response visit(MoveRequest request) {
		return null;
	}

	@Override
	public Response visit(ReadyRequest request) {
		return null;
	}

	@Override
	public Response visit(AccountCreationRequest request) {
		return null;
	}

}
