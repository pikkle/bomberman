package ch.heigvd.bomberman.common.communication.responses;

import ch.heigvd.bomberman.common.game.Element;

import java.util.UUID;

/**
 * Created by matthieu.villard on 29.05.2016.
 */
public class DestroyElementsResponse extends Response<Element> {

	private Element element;

	public DestroyElementsResponse(UUID uuid, Element element) {
		super(uuid);
		this.element = element;
	}

	public Element getElement() {
		return element;
	}

	@Override
	public Element accept(ResponseVisitor visitor) {
		return visitor.visit(this);
	}
}
