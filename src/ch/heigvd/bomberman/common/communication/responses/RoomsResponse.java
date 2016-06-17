package ch.heigvd.bomberman.common.communication.responses;

import ch.heigvd.bomberman.common.game.Room;

import java.util.List;
import java.util.UUID;

/**
 * Created by matthieu.villard on 27.05.2016.
 */
public class RoomsResponse extends Response<List<Room>> {

	private List<Room> rooms;
	private int n;

	public RoomsResponse(UUID uuid, List<Room> rooms) {
		super(uuid);
		this.rooms = rooms;
	}

	public List<Room> getRooms() {
		return rooms;
	}

	@Override
	public List<Room> accept(ResponseVisitor visitor) {
		return visitor.visit(this);
	}
}
