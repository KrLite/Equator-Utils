package net.krlite.equator.core;

import java.util.function.Consumer;

/**
 * An interface to define the basic operations
 * that can be performed on an object, but without
 * a return value.
 *
 * @param <T>	The type of the object.
 */
public interface OperatableVoid<T, R> {
	/**
	 * Operates the current object.
	 *
	 * @param operation	The operation to perform.
	 * @return			The operated object.
	 */
	R operate(Consumer<T> operation);

	/**
	 * An interface to define the basic operations
	 * that can be performed on an object, but without
	 * a return value.
	 *
	 * @param <S>	The type of the object and the result.
	 */
	interface Single<S> {
		/**
		 * Operates the current object.
		 *
		 * @param operation The operation to perform.
		 */
		void operate(Consumer<S> operation);
	}
}
