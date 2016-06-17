package ch.heigvd.bomberman.common.communication.responses;

import ch.heigvd.bomberman.common.game.Statistic;

import java.util.UUID;

/**
 * Created by matthieu.villard on 31.05.2016.
 */
public class EndGameResponse extends Response<Statistic> {

	private Statistic statistic;

	public EndGameResponse(UUID uuid, Statistic statistic) {
		super(uuid);
		this.statistic = statistic;
	}

	public Statistic getStatistic() {
		return statistic;
	}

	@Override
	public Statistic accept(ResponseVisitor visitor) {
		return visitor.visit(this);
	}
}
