package ch.heigvd.bomberman.common.communication.requests;

import java.io.Serializable;

public abstract class Request implements Serializable {
    public abstract RequestType getType();
}
