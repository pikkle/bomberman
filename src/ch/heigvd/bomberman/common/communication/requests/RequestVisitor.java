package ch.heigvd.bomberman.common.communication.requests;

import ch.heigvd.bomberman.common.communication.responses.Response;

public interface RequestVisitor {
	Response visit(HelloRequest request);
	Response visit(MoveRequest request);
	Response visit(ReadyRequest request);
	Response visit(AccountCreationRequest request);
	Response visit(LoginRequest loginRequest);
	Response visit(CreateRoomRequest request);
}