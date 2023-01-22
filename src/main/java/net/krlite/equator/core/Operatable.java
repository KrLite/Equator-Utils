package net.krlite.equator.core;

import java.util.function.Function;

public interface Operatable<C> {
	/**
	 * Operates the current object.
	 * @param operation	The operation to perform.
	 * @return			The operated object.
	 */
	C operate(Function<C, C> operation);
}
