package ch.heigvd.bomberman.common.communication.requests;

import ch.heigvd.bomberman.common.communication.responses.Response;
import ch.heigvd.bomberman.common.game.Room;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

public abstract class Request<T> implements Serializable {
    private UUID uuid = UUID.randomUUID();
    public abstract Response<List<Room>> accept(RequestVisitor visitor);


    public UUID getID() {
        return uuid;
    }
}