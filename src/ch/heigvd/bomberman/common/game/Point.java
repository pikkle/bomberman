package ch.heigvd.bomberman.common.game;

import java.io.Serializable;

/**
 * Projet : GEN_Projet
 * Créé le 19.05.2016.
 *
 * @author Adriano Ruberto
 */
public class Point implements Serializable {

	private final int x;
	private final int y;

	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Point() {
		this(0, 0);
	}

	public int x() {return x;}

	public int y() {return y;}

	public Point add(int u, int v) { return new Point(x + u, y + v);}

	public boolean equals(Object o) {
		return o == this || (o instanceof Point) && ((Point) o).x == x && ((Point) o).y == y;
	}
}
