package ch.heigvd.bomberman.common.communication.requests;

import ch.heigvd.bomberman.common.communication.responses.Response;
import ch.heigvd.bomberman.common.game.Room;

/**
 * Created by matthieu.villard on 27.05.2016.
 */
public class JoinRoomRequest extends Request<Room> {

    private Room room;

    public JoinRoomRequest(Room room){
        this.room = room;
    }

    public Room getRoom(){
        return room;
    }

    @Override
    public Response<Room> accept(RequestVisitor visitor) {
        return visitor.visit(this);
    }
}
