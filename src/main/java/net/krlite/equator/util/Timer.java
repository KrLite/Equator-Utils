package net.krlite.equator.util;

import net.krlite.equator.core.OperatableVoid;

import java.util.function.Consumer;

/**
 * <h2>Timer</h2>
 * A timer class that can be used to countdown and to
 * measure the time between two events.
 */
public class Timer implements OperatableVoid<Timer> {
	/**
	 * The time to countdown, must be positive.
	 */
	private final long lasting;

	/**
	 * The time when the timer is started.
	 */
	private long origin;

	/**
	 * The time elapsed at the timer's last step.
	 */
	private long lastStep;

	/**
	 * The stepping mode of the timer.
	 */
	private boolean stepping = false;

	/**
	 * Gets the lasting time of the timer.
	 * @return	The lasting time.
	 */
	public long getLasting() {
		return lasting;
	}

	/**
	 * Creates a new timer with the origin time set to
	 * the current system time, in milliseconds.
	 * @param lasting	The lasting time, in milliseconds.
	 *                  Will take the absolute value.
	 */
	public Timer(long lasting) {
		this.origin = SystemClock.queueElapsed();
		this.lasting = Math.abs(lasting);
	}

	/**
	 * Enters the stepping mode of the timer.
	 */
	public void enterStepping() {
		if (!isStepping()) {
			this.lastStep = queueElapsed();
			this.stepping = true;
		}
	}

	/**
	 * Exits the stepping mode of the timer.
	 */
	public void quitStepping() {
		if (isStepping()) this.stepping = false;
	}

	public void step(long step) {
		if (isStepping()) this.lastStep += step;
	}

	public void step() {
		step(1);
	}

	/**
	 * Queues the time to the origin time, won't exceed
	 * the lasting time.
	 * @param countStepping	Whether to count this step
	 *                      in the stepping mode.
	 * @return				The time, in milliseconds.
	 */
	public long queue(boolean countStepping) {
		return Math.min(queueElapsed(countStepping), lasting);
	}

	/**
	 * Queues the elapsed time to the origin time,
	 * ignores the lasting time.
	 * @param countStepping	Whether to count this step
	 *                      in the stepping mode.
	 * @return				The time elapsed, in
	 * 						milliseconds.
	 */
	public long queueElapsed(boolean countStepping) {
		if (countStepping && isStepping()) origin = SystemClock.queueElapsed() - ++lastStep;
		return SystemClock.queueElapsed() - origin;
	}

	/**
	 * Queues the time to the origin time as percentage.
	 * @param countStepping	Whether to count this step
	 *                      in the stepping mode.
	 * @return				The time in the range of [0, 1].
	 */
	public double queueAsPercentage(boolean countStepping) {
		return (double) queue(countStepping) / (double) lasting;
	}

	/**
	 * Queues the time to the origin time, won't exceed
	 * the lasting time.
	 * @return	The time, in milliseconds.
	 */
	public long queue() {
		return queue(true);
	}

	/**
	 * Queues the elapsed time to the origin time,
	 * ignores the lasting time.
	 * @return	The time elapsed, in milliseconds.
	 */
	public long queueElapsed() {
		return queueElapsed(true);
	}

	/**
	 * Queues the time to the origin time as percentage.
	 * @return	The time in the range of [0, 1].
	 */
	public double queueAsPercentage() {
		return queueAsPercentage(true);
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
		if (isFinished()) {
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
	public boolean isFinished() {
		return queueElapsed() > lasting;
	}

	/**
	 * Checks whether the timer is in the stepping mode.
	 * @return	<code>true</code> if the timer is in
	 * 			the stepping mode.
	 * 			otherwise <code>false</code>.
	 */
	public boolean isStepping() {
		return stepping;
	}

	/**
	 * Resets the timer without changing the lasting
	 * time.
	 */
	public void reset() {
		this.origin = SystemClock.queue();
	}

	@Override
	public Timer operate(Consumer<Timer> operation) {
		operation.accept(this);
		return this;
	}
}
