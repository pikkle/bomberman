package ch.heigvd.bomberman.common.communication.requests;

import ch.heigvd.bomberman.common.communication.responses.Response;

public interface RequestVisitor {
	// simple requests
	Response visit(HelloRequest request);
	Response visit(AccountCreationRequest request);
	Response visit(AccountModifyRequest request);
	Response visit(LoginRequest request);
	Response visit(PlayerRequest request);
	Response visit(PlayersRequest request);
	Response visit(PlayerStateRequest request);
	Response visit(ArenasRequest request);
	Response visit(SaveArenaRequest request);
	Response visit(RemoveArenaRequest request);
	Response visit(CreateRoomRequest request);
	Response visit(RoomsRequest request);
	Response visit(JoinRoomRequest request);

	// game requests
	Response visit(ReadyRequest request);
	Response visit(MoveRequest request);
	Response visit(AddElementRequest request);
	Response visit(DestroyElementsRequest request);
	Response visit(DropBombRequest request);
	Response visit(EndGameRequest request);
	Response visit(EjectRequest request);
}