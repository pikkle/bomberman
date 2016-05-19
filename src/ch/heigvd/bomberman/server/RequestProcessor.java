package ch.heigvd.bomberman.server;

import ch.heigvd.bomberman.common.communication.requests.*;
import ch.heigvd.bomberman.common.communication.responses.HelloResponse;
import ch.heigvd.bomberman.common.communication.responses.Response;
import ch.heigvd.bomberman.common.communication.responses.SuccessResponse;

public class RequestProcessor implements RequestVisitor {
	private static RequestProcessor instance = new RequestProcessor();

	private RequestProcessor(){	}

	public static RequestVisitor getInstance() {
		return instance;
	}

	public Response visit(HelloRequest request){
		System.out.println("Received message: ");
		System.out.println(request.getMessage());
		return new HelloResponse(request.getID(), "Hello !");
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

	@Override
	public Response visit(LoginRequest loginRequest) {
		System.out.println("Connection from user");
		System.out.println("Username: " + loginRequest.getUsername());
		System.out.println("Password: " + loginRequest.getPassword());
		return new SuccessResponse(loginRequest.getID(), "Connection successful");
	}

}