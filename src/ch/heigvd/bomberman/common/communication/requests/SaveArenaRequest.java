package ch.heigvd.bomberman.common.communication.requests;

import ch.heigvd.bomberman.common.communication.Message;
import ch.heigvd.bomberman.common.communication.responses.Response;
import ch.heigvd.bomberman.common.game.Arena;

/**
 * Created by matthieu.villard on 03.06.2016.
 */
public class SaveArenaRequest extends Request<Message> {

    private Arena arena;

    public SaveArenaRequest(Arena arena){
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
