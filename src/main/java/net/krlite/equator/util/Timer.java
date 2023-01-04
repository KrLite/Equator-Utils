package net.krlite.equator.util;

/**
 * <h2>Timer</h2>
 * <h3>Countdown in Milliseconds</h3>
 * A timer class that can be used to countdown and to
 * measure the time between two events.
 */
public class Timer {
	/**
	 * The time to countdown, must be positive.
	 */
	public final long lasting;

	/**
	 * The time when the timer is started.
	 */
	private long origin;

	/**
	 * Creates a new timer with the origin time set to
	 * the current system time, in milliseconds.
	 * @param lasting	The lasting time, in milliseconds.
	 *                  Will take the absolute value.
	 */
	public Timer(long lasting) {
		this.origin = System.currentTimeMillis();
		this.lasting = Math.abs(lasting);
	}

	/**
	 * Queues the time to the origin time, won't exceed
	 * the lasting time.
	 * @return	The time, in milliseconds.
	 */
	public long queue() {
		return Math.min(queueElapsed(), lasting);
	}

	/**
	 * Queues the elapsed time to the origin time,
	 * ignores the lasting time.
	 * @return	The time elapsed, in milliseconds.
	 */
	public long queueElapsed() {
		return System.currentTimeMillis() - origin;
	}

	/**
	 * Queues the time to the origin time as percentage.
	 * @return	The time in the range of [0, 1].
	 */
	public double queueAsPercentage() {
		return (double) queue() / (double) lasting;
	}

	/**
	 * Checks whether the timer has reached the lasting
	 * time limit, and runs the runnable if it has.
	 * @param runnable	The runnable to be run if the
	 *                  timer has reached the lasting
	 *                  time limit.
	 * @return			<code>true</code> if the timer
	 * 					has reached the lasting time
	 * 					limit, which means the timer
	 * 					has already counted down, and
	 * 					the runnable has been run.
	 * 					otherwise <code>false</code>.
	 */
	public boolean run(Runnable runnable) {
		if (isDone()) {
			runnable.run();
			return true;
		} else return false;
	}

	/**
	 * Checks whether the timer has not reached the
	 * lasting time limit.
	 * @return	<code>true</code> if the timer has
	 * 			not reached the lasting time limit,
	 * 			which means the timer is still
	 * 			counting.
	 * 			otherwise <code>false</code>.
	 */
	public boolean isPresent() {
		return queueElapsed() >= 0 && queueElapsed() <= lasting;
	}

	/**
	 * Checks whether the timer has reached the lasting
	 * time limit.
	 * @return	<code>true</code> if the timer has
	 * 			reached the lasting time limit, which
	 * 			means the timer has already counted
	 * 			down.
	 * 			otherwise <code>false</code>.
	 */
	public boolean isDone() {
		return queueElapsed() > lasting;
	}

	/**
	 * Resets the timer without changing the lasting
	 * time.
	 * @return	The new timer with the same lasting
	 * 			time.
	 */
	public Timer reset() {
		this.origin = System.currentTimeMillis();
		return this;
	}
}
