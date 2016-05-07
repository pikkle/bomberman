package ch.heigvd.bomberman.common.communication.responses;

import java.io.Serializable;

public abstract class Response implements Serializable {
    public abstract ResponseType getType();
}
