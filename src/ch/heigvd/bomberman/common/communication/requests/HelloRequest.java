package ch.heigvd.bomberman.common.communication.requests;

public class HelloRequest extends Request {
    private String message;
    public HelloRequest(String message){
        this.message = message;
    }
    @Override
    public RequestType getType() {
        return RequestType.HELLO;
    }

    public String getMessage() {
        return message;
    }
}
