package ch.heigvd.bomberman.common.communication.responses;

import java.io.Serializable;

public abstract class Response implements Serializable {
    private String message;
    private Type type;
    public Response(Type t, String message) {
        this.type = t;
        this.message = message;
    }

    public Type getType(){
        return type;
    }

    /**
     * Used to simplify the management of the different types of responses
     */
    public enum Type {
        NO_RESPONSE;
    }
}
