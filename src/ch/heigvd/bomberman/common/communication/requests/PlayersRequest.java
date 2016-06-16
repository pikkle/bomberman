package ch.heigvd.bomberman.common.communication.requests;

import ch.heigvd.bomberman.common.communication.responses.Response;
import ch.heigvd.bomberman.common.game.Player;

import java.util.List;

/**
 * Created by matthieu.villard on 16.06.2016.
 */
public class PlayersRequest extends Request<List<Player>> {

	@Override
	public Response<List<Player>> accept(RequestVisitor visitor) {
		return visitor.visit(this);
	}
}
