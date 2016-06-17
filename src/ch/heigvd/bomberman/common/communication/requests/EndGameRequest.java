package ch.heigvd.bomberman.common.communication.requests;

import ch.heigvd.bomberman.common.communication.responses.Response;
import ch.heigvd.bomberman.common.game.Statistic;

/**
 * Created by matthieu.villard on 31.05.2016.
 */
public class EndGameRequest extends Request<Statistic> {
	@Override
	public Response<Statistic> accept(RequestVisitor visitor) {
		return visitor.visit(this);
	}

}
