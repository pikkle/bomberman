package ch.heigvd.bomberman.common.communication.requests;

import ch.heigvd.bomberman.common.communication.responses.Response;

import java.io.Serializable;

public abstract class Request implements Serializable {
    public abstract Response accept(RequestVisitor visitor);

}
