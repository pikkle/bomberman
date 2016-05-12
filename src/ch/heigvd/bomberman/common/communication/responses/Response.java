package ch.heigvd.bomberman.common.communication.responses;

import java.io.Serializable;
import java.util.UUID;

public abstract class Response implements Serializable {
    private UUID uuid;
    public Response(UUID uuid) {
        this.uuid = uuid;
    }

    public boolean isSendable() {
        return true;
    }
    public abstract void accept(ResponseVisitor visitor);
    public UUID getID() {
        return uuid;
    }
}
