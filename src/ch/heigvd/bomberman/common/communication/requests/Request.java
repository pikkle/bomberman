package ch.heigvd.bomberman.common.communication.requests;

import java.io.Serializable;

public abstract class Request implements Serializable {
    private Type type;
    public Request(Type t){
        this.type = t;
    }

    public Type getType() {
        return type;
    }


    public enum Type{
        ACCOUNT_CREATION;
    }
}
