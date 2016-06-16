package ch.heigvd.bomberman.common.communication.requests;

import ch.heigvd.bomberman.common.communication.Message;
import ch.heigvd.bomberman.common.communication.responses.Response;

/**
 * Created by matthieu.villard on 16.06.2016.
 */
public class PlayerStateRequest extends Request<Message> {

	private long id;

	private boolean isLocked;

	public PlayerStateRequest(long id, boolean isLocked){
		this.id = id;
		this.isLocked = isLocked;
	}

	public long getId(){
		return id;
	}

	public boolean isLocked(){
		return isLocked;
	}

	@Override
	public Response<Message> accept(RequestVisitor visitor) {
		return visitor.visit(this);
	}
}
