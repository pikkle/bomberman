package ch.heigvd.bomberman.common.communication.requests;

import ch.heigvd.bomberman.common.communication.responses.Response;
import ch.heigvd.bomberman.common.game.Element;

/**
 * Created by matthieu.villard on 29.05.2016.
 */
public class DestroyElementsRequest extends Request<Element> {
    @Override
    public Response<Element> accept(RequestVisitor visitor) {
        return visitor.visit(this);
    }

}
