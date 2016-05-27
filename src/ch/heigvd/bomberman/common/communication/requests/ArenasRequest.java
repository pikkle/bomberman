package ch.heigvd.bomberman.common.communication.requests;

import ch.heigvd.bomberman.common.communication.responses.Response;
import ch.heigvd.bomberman.common.game.Arena.Arena;
import ch.heigvd.bomberman.common.game.Room;

import java.util.List;

/**
 * Created by matthieu.villard on 27.05.2016.
 */
public class ArenasRequest extends Request<List<Arena>> {

    @Override
    public Response<List<Room>> accept(RequestVisitor visitor) {
        return visitor.visit(this);
    }
}
