package ch.heigvd.bomberman.common.communication.requests;

import ch.heigvd.bomberman.common.communication.responses.Response;
import ch.heigvd.bomberman.common.game.Player;

/**
 * Created by matthieu.villard on 02.06.2016.
 */
public class PlayerRequest extends Request<Player> {

    @Override
    public Response<Player> accept(RequestVisitor visitor) {
        return visitor.visit(this);
    }
}
