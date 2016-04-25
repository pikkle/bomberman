package ch.heigvd.bomberman.common.communication.responses;

/**
 * Corresponds to an empty response.
 * Nothing will be sent to the client.
 */
public class NoResponse extends Response {
    public NoResponse() {
        super(Type.NO_RESPONSE, "");
    }

}
