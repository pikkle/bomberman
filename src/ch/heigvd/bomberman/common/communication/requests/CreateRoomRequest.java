package ch.heigvd.bomberman.common.communication.requests;

import ch.heigvd.bomberman.common.communication.Message;
import ch.heigvd.bomberman.common.communication.responses.Response;

/**
 * Created by matthieu.villard on 26.05.2016.
 */
public class CreateRoomRequest extends Request<Message> {

	private String name;
	private long arena;
	private int minPlayer;
	private String password;

	public CreateRoomRequest(String name, long arena, int minPlayer) {
		this.name = name;
		this.arena = arena;
		this.minPlayer = minPlayer;
	}

	/**
	 * Creates a room
	 *
	 * @param name
	 * @param arena
	 * @param minPlayer
	 * @param password
	 */
	public CreateRoomRequest(String name, long arena, int minPlayer, String password) {
		this.name = name;
		this.arena = arena;
		this.minPlayer = minPlayer;
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public long getArena() {
		return arena;
	}

	public int getMinPlayer() {
		return minPlayer;
	}

	public String getPassword() {
		return password;
	}

	@Override
	public Response<Message> accept(RequestVisitor visitor) {
		return visitor.visit(this);
	}
}
