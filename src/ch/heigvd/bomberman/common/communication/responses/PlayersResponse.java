package ch.heigvd.bomberman.common.communication.responses;

import ch.heigvd.bomberman.common.game.Player;

import java.util.List;
import java.util.UUID;

/**
 * Created by matthieu.villard on 16.06.2016.
 */
public class PlayersResponse extends Response<List<Player>> {

	private List<Player> players;

	public PlayersResponse(UUID uuid, List<Player> players) {
		super(uuid);
		this.players = players;
	}

	public List<Player> getPlayers(){
		return players;
	}

	@Override
	public List<Player> accept(ResponseVisitor visitor) {
		return visitor.visit(this);
	}
}
