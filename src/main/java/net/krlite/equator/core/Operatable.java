package net.krlite.equator.core;

import java.util.function.Function;

/**
 * An interface to define the basic operations
 * that can be performed on an object.
 *
 * @param <T>	The type of the object.
 * @param <R>	The type of the result.
 */
public interface Operatable<T, R> {
	/**
	 * Operates the current object.
	 *
	 * @param operation	The operation to perform.
	 * @return			The operated object.
	 */
	R operate(Function<T, T> operation);

	/**
	 * An interface to define the basic operations
	 * that can be performed on an object.
	 *
	 * @param <S>	The type of the object and the result.
	 */
	interface Single<S> {
		/**
		 * Operates the current object.
		 *
		 * @param operation The operation to perform.
		 * @return The operated object.
		 */
		S operate(Function<S, S> operation);
	}
}
