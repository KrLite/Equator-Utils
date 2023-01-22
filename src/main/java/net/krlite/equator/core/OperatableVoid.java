package net.krlite.equator.core;

import java.util.function.Consumer;

public interface OperatableVoid<T, R> {
	R operate(Consumer<T> operation);
}
