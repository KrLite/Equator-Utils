package net.krlite.equator.util.pair;

import net.krlite.equator.base.HashCodeComparable;
import net.krlite.equator.core.ShortStringable;

/**
 * A pair of two elements.
 *
 * @param <F>	The type of the first element.
 * @param <S>	The type of the second element.
 */
public class Pair<F, S> extends HashCodeComparable implements ShortStringable, Cloneable {
	/**
	 * The first object.
	 */
	protected final F first;

	/**
	 * The second object.
	 */
	protected final S second;

	/**
	 * Creates a new pair.
	 *
	 * @param first		The first element.
	 * @param second	The second element.
	 */
	public Pair(F first, S second) {
		this.first = first;
		this.second = second;
	}

	/**
	 * Gets the first element.
	 *
	 * @return	The first element.
	 */
	public F getFirst() {
		return first;
	}

	/**
	 * Gets the second element.
	 *
	 * @return	The second element.
	 */
	public S getSecond() {
		return second;
	}

	/**
	 * Swaps the elements in the pair.
	 *
	 * @return	A new pair with the elements swapped.
	 */
	public Pair<S, F> swap() {
		return new Pair<S, F>(second, first);
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "{" + formatFields() + "}";
	}

	@Override
	@SuppressWarnings("unchecked")
	public Pair<F, S> clone() {
		try {
			return (Pair<F, S>) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}
}
