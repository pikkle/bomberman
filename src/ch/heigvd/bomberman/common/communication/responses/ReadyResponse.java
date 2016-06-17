package ch.heigvd.bomberman.common.communication.responses;

import ch.heigvd.bomberman.common.game.Bomberman;

import java.util.UUID;

/**
 * Created by matthieu.villard on 27.05.2016.
 */
public class ReadyResponse extends Response<Bomberman> {

	private Bomberman bomberman;

	public ReadyResponse(UUID uuid, Bomberman bomberman) {
		super(uuid);
		this.bomberman = bomberman;
	}

	public Bomberman getBomberman() {
		return bomberman;
	}

	@Override
	public Bomberman accept(ResponseVisitor visitor) {
		return visitor.visit(this);
	}
}
