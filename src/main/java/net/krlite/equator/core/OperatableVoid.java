package net.krlite.equator.core;

import java.util.function.Consumer;

public interface OperatableVoid<C> {
	C operate(Consumer<C> operation);
}
