package ch.heigvd.bomberman.common.communication.responses;

import ch.heigvd.bomberman.common.game.Direction;

public interface ResponseVisitor {
	Object visit(NoResponse noResponse);
	Direction visit(MoveResponse moveResponse);
	Boolean visit(SuccessResponse successResponse);
	String visit(HelloResponse helloResponse);
	Boolean visit(ErrorResponse errorResponse);
}
