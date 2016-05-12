package ch.heigvd.bomberman.common.communication.requests;

import ch.heigvd.bomberman.common.communication.responses.Response;

public class HelloRequest extends Request<String> {
    private String message;

    public HelloRequest(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public Response accept(RequestVisitor visitor) {
        return visitor.visit(this);
    }

}
