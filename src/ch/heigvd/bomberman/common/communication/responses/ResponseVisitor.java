package ch.heigvd.bomberman.common.communication.responses;

public interface ResponseVisitor {
	void visit(NoResponse noResponse);
	void visit(MoveResponse moveResponse);
	void visit(SuccessResponse successResponse);
	void visit(HelloResponse helloResponse);
}
