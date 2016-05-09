package ch.heigvd.bomberman.common.communication.responses;

import java.io.Serializable;

public abstract class Response implements Serializable {
    private String message;
    private ResponseType type;
    public Response(ResponseType t, String message) {
        this.type = t;
        this.message = message;
    }

    public ResponseType getType(){
        return type;
    }
}
