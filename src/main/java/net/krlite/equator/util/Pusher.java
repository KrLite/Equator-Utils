package net.krlite.equator.util;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * <h2>Pusher</h2>
 * <h3>Consumer / Single Thread Supplier</h3>
 * A class works as a consumer and a supplier that allows
 * for a single thread to pull the value which pushed by
 * others.
 * @param ready	Indicates whether the value is ready to be
 *              pulled. The value is disposed after it is
 *              pulled.
 */
public record Pusher(@NotNull AtomicBoolean ready) {
	/**
	 * Creates a new Pusher with a ready flag.
	 * @param ready	The ready flag.
	 */
	public Pusher(boolean ready) {
		this(new AtomicBoolean(ready));
	}

	/**
	 * Creates a new Pusher with a ready flag set to false.
	 */
	public Pusher() {
		this(false);
	}

	/**
	 * Pushes the consumer, allows it to be pulled.
	 */
	public void push() {
		ready.set(true);
	}

	/**
	 * Pushes the consumer, allows it to be pulled.
	 * @param runnable	The runnable to be run after the
	 *                  value is disposed.
	 */
	public void push(@NotNull Runnable runnable) {
		push();
		runnable.run();
	}

	/**
	 * Pulls the consumer, disposes the value.
	 * @return	Whether the value is ready to be pulled.
	 * 			The value is disposed after it is pulled.
	 */
	public boolean pull() {
		return ready.get() && ready.getAndSet(false);
	}

	/**
	 * Runs the runnable after the supplier is
	 * pulled. The runnable is not run if the
	 * value is not ready to be pulled, and the
	 * value is disposed after it is pulled.
	 * @param runnable	The runnable to be run after the
	 *                  value is disposed.
	 */
	public void run(@NotNull Runnable runnable) {
		if (pull()) runnable.run();
	}

	/**
	 * Short-circuits the runnable if the parameter
	 * is <code>false</code>. The runnable only runs
	 * if the value is ready to be pulled and the
	 * parameter doesn't short-circuit the supplier.
	 * The value is disposed after it is pulled.
	 * @param or		The boolean to short-circuit
	 *                  the supplier.
	 * @param runnable	The runnable to be run after the
	 *                  value is disposed.
	 */
	public void or(boolean or, @NotNull Runnable runnable) {
		if (or || pull()) runnable.run();
	}

	/**
	 * Short-circuits the runnable if the parameter
	 * is <code>true</code>. The runnable only runs
	 * if the value is ready to be pulled and the
	 * parameter doesn't short-circuit the supplier.
	 * The value is disposed after it is pulled.
	 * @param and		The boolean to short-circuit
	 *            		the supplier.
	 * @param runnable	The runnable to be run after the
	 *                  value is disposed.
	 */
	public void and(boolean and, @NotNull Runnable runnable) {
		if (and && pull()) runnable.run();
	}

	/**
	 * Pastes the pusher instance.
	 * @return	A new pusher instance with the same
	 * 			ready flag.
	 */
	public Pusher paste() {
		return new Pusher(new AtomicBoolean(ready.get()));
	}
}
