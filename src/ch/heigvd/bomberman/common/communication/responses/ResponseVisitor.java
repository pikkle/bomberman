package ch.heigvd.bomberman.common.communication.responses;

import ch.heigvd.bomberman.common.communication.Message;
import ch.heigvd.bomberman.common.game.Direction;

public interface ResponseVisitor {
	Object visit(NoResponse noResponse);
	Direction visit(MoveResponse moveResponse);
	String visit(HelloResponse helloResponse);
	Message visit(SuccessResponse successResponse);
	Message visit(ErrorResponse errorResponse);
}