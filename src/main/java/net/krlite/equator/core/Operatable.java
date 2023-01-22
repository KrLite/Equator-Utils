package net.krlite.equator.core;

import java.util.function.Function;

public interface Operatable<T, R> {
	/**
	 * Operates the current object.
	 * @param operation	The operation to perform.
	 * @return			The operated object.
	 */
	R operate(Function<T, T> operation);
}
