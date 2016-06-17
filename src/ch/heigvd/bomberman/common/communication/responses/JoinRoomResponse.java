package ch.heigvd.bomberman.common.communication.responses;

import ch.heigvd.bomberman.common.game.Room;

import java.util.UUID;

/**
 * Created by matthieu.villard on 27.05.2016.
 */
public class JoinRoomResponse extends Response<Room> {

	private Room room;

	public JoinRoomResponse(UUID uuid, Room room) {
		super(uuid);
		this.room = room;
	}

	public Room getRoom() {
		return room;
	}

	@Override
	public Room accept(ResponseVisitor visitor) {
		return visitor.visit(this);
	}
}
