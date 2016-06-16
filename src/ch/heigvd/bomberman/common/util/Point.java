package ch.heigvd.bomberman.common.util;

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

	/**
	 * Creates a point with x = 0, y = 0
	 */
	public Point() {
		this(0, 0);
	}

	/**
	 * Gets the x coordinate
	 *
	 * @return the x coordinate
	 */
	public int x() {
		return x;
	}

	/**
	 * Gets the y coordinate
	 *
	 * @return the y coordinate
	 */
	public int y() {
		return y;
	}

	/**
	 * Gets a new point with u and v values added to the current point.
	 *
	 * @param u the first value (x)
	 * @param v the second value (y)
	 * @return the new point
	 */
	public Point add(int u, int v) {
		return new Point(x + u, y + v);
	}

	/**
	 * Two points are equals if they have the same x and y value.
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object o) {
		return super.equals(o) || ((o instanceof Point) && ((Point) o).x == x && ((Point) o).y == y);
	}
}
