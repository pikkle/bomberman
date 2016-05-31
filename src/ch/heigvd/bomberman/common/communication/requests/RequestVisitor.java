package ch.heigvd.bomberman.common.communication.requests;

import ch.heigvd.bomberman.common.communication.responses.Response;

public interface RequestVisitor {
	Response visit(HelloRequest request);
	Response visit(ReadyRequest request);
	Response visit(AccountCreationRequest request);
	Response visit(LoginRequest loginRequest);
	Response visit(ArenasRequest request);
	Response visit(CreateRoomRequest request);
	Response visit(RoomsRequest request);
	Response visit(JoinRoomRequest request);
	Response visit(MoveRequest request);
	Response visit(AddElementRequest request);
	Response visit(DestroyElementsRequest request);
	Response visit(DropBombRequest request);
	Response visit(EndGameRequest request);
}