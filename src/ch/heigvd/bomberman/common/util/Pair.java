package ch.heigvd.bomberman.common.util;

/**
 * A basic implementation of a pair of two objects.
 *
 * @param <A> the type of the first object
 * @param <B> the type of the second object
 */
public class Pair<A, B> {

	private final A first;
	private final B second;

	private Pair(A first, B second) {
		this.first = first;
		this.second = second;
	}

	/**
	 * Creates a pair of 2 objects.
	 *
	 * @param a   the first object
	 * @param b   the second object
	 * @param <A> the type of the first object
	 * @param <B> the type of the second object
	 * @return the created pair
	 */
	public static <A, B> Pair<A, B> of(A a, B b) {
		return new Pair<>(a, b);
	}

	/**
	 * Gets the first object of the pair
	 *
	 * @return the first object of the pair
	 */
	public A first() {
		return first;
	}

	/**
	 * Gets the second object of the pair.
	 *
	 * @return the second object of the pair
	 */
	public B second() {
		return second;
	}
}
