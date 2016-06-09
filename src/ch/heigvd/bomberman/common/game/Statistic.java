package ch.heigvd.bomberman.common.game;

import java.io.Serializable;

/**
 * Created by matthieu.villard on 31.05.2016.
 */
public class Statistic implements Serializable {

	private long rank;

	/**
	 * TODO
	 *
	 * @param rank
	 */
	public Statistic(long rank) {
		this.rank = rank;
	}

	/**
	 * TODO
	 *
	 * @return
	 */
	public long getRank() {
		return rank;
	}
}
