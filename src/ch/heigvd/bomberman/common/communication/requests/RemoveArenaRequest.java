package ch.heigvd.bomberman.common.communication.requests;

import ch.heigvd.bomberman.common.communication.Message;
import ch.heigvd.bomberman.common.communication.responses.Response;
import ch.heigvd.bomberman.common.game.Arena.Arena;

/**
 * Created by matthieu.villard on 03.06.2016.
 */
public class RemoveArenaRequest extends Request<Message> {

    private Arena arena;

    public RemoveArenaRequest(Arena arena){
        this.arena = arena;
    }

    public Arena getArena(){
        return arena;
    }


    @Override
    public Response<Message> accept(RequestVisitor visitor) {
        return visitor.visit(this);
    }
}