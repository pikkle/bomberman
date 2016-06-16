package ch.heigvd.bomberman.client;

import ch.heigvd.bomberman.common.communication.Message;
import ch.heigvd.bomberman.common.communication.responses.*;
import ch.heigvd.bomberman.common.game.*;
import ch.heigvd.bomberman.common.game.Arena;

import java.util.List;

public class ResponseProcessor implements ResponseVisitor {
	private static ResponseProcessor instance = new ResponseProcessor();

	private ResponseProcessor(){	}

	public static ResponseProcessor getInstance() {
		return instance;
	}


	// TODO: implement processing of responses
	@Override
	public Object visit(NoResponse noResponse) {
		return null;
	}

	@Override
	public Bomberman visit(MoveResponse moveResponse) {
		return moveResponse.getBomberman();
	}

	@Override
	public String visit(HelloResponse helloResponse) {
		return null;
	}

	@Override
	public Message visit(SuccessResponse successResponse) {
		return new Message(true, successResponse.getMessage());
	}

	@Override
	public Message visit(ErrorResponse errorResponse) {
		return new Message(false, errorResponse.getMessage());
	}

	@Override
	public Player visit(PlayerResponse response) {
		return response.getPlayer();
	}

	@Override
	public List<Player> visit(PlayersResponse response) {
		return response.getPlayers();
	}

	@Override
	public Bomberman visit(ReadyResponse response) {
		return response.getBomberman();
	}

	@Override
	public List<Arena> visit(ArenasResponse response) {
		return response.getArenas();
	}

	@Override
	public Room visit(JoinRoomResponse response) {
		return response.getRoom();
	}

	@Override
	public List<Room> visit(RoomsResponse response) {
		return response.getRooms();
	}

	@Override
	public Element visit(AddElementResponse response) {
		return response.getElement();
	}

	@Override
	public Element visit(DestroyElementsResponse response) {
		return response.getElement();
	}

	@Override
	public Statistic visit(EndGameResponse response) {
		return response.getStatistic();
	}
}