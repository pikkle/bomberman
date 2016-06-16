package ch.heigvd.bomberman.common.communication.requests;

import ch.heigvd.bomberman.common.communication.Message;
import ch.heigvd.bomberman.common.communication.responses.Response;

/**
 * Created by matthieu.villard on 16.06.2016.
 */
public class EjectRequest extends Request<Message> {

	private String player;

	public EjectRequest(String player){
		this.player = player;
	}

	@Override
	public Response<Message> accept(RequestVisitor visitor) {
		return visitor.visit(this);
	}

	public String getPlayer(){
		return this.player;
	}
}
