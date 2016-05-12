package ch.heigvd.bomberman.common.communication.responses;

import java.io.Serializable;

public abstract class Response implements Serializable {
    public boolean isSendable() {
        return true;
    }
    public abstract void accept(ResponseVisitor visitor);
}
