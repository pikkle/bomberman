package ch.heigvd.bomberman.common.communication.responses;

import ch.heigvd.bomberman.common.communication.Message;
import ch.heigvd.bomberman.common.game.*;
import ch.heigvd.bomberman.common.game.Arena.Arena;

import java.util.List;

public interface ResponseVisitor {
	Object visit(NoResponse noResponse);
	Bomberman visit(MoveResponse moveResponse);
	String visit(HelloResponse helloResponse);
	Message visit(SuccessResponse successResponse);
	Message visit(ErrorResponse errorResponse);
	Bomberman visit(ReadyResponse response);
	List<Arena> visit(ArenasResponse response);
	Room visit(JoinRoomResponse response);
	List<Room> visit(RoomsResponse response);
	Element visit(AddElementResponse response);
	Element visit(DestroyElementsResponse response);
	Statistic visit(EndGameResponse response);
}