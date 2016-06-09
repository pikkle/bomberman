package ch.heigvd.bomberman.common.game;

/**
 * Projet : GEN_Projet
 * Créé le 09.06.2016.
 *
 * @author Adriano Ruberto
 */
public class Pair<A, B> {

	private final A first;
	private final B second;

	private Pair(A first, B second) {
		this.first = first;
		this.second = second;
	}

	/**
	 * TODO
	 * @param a
	 * @param b
	 * @param <A>
	 * @param <B>
	 * @return
	 */
	public static <A, B> Pair<A, B> of(A a, B b) {
		return new Pair<>(a, b);
	}

	/**
	 * TODO
	 * @return
	 */
	public A first() {
		return first;
	}

	/**
	 * TODO
	 * @return
	 */
	public B second() {
		return second;
	}
}
