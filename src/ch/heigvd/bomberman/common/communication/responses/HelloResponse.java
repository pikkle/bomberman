package ch.heigvd.bomberman.common.communication.responses;

public class HelloResponse extends Response {
    public HelloResponse(String message) {
        super(ResponseType.HELLO_RESPONSE, message);
    }
}
