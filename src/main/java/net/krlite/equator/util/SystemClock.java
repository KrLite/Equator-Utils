package net.krlite.equator.util;

import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * <h2>SystemClock</h2>
 * A class that provides the current system time in
 * milliseconds.
 *
 * @see System#currentTimeMillis()
 */
public class SystemClock {
	/**
	 * The volatile variable that stores the current
	 * system time. Queuing this variable will not block
	 * the system's internal clock.
	 */
	private volatile long currentTimeMillis;

	/**
	 * The origin time of this {@link SystemClock}.
	 */
	private final long origin;

	private SystemClock() {
		this.currentTimeMillis = this.origin = System.currentTimeMillis();
		syncScheduled();
	}

	/**
	 * Syncs the current time with the system's internal
	 * clock.
	 */
	private void syncScheduled() {
		new ScheduledThreadPoolExecutor(1, runnable -> {
			Thread thread = new Thread(runnable, "SystemClock");
			thread.setDaemon(true);
			return thread;
		}).scheduleAtFixedRate(() -> currentTimeMillis = System.currentTimeMillis(),
				1, 1, java.util.concurrent.TimeUnit.MILLISECONDS);
	}

	/**
	 * Gets the {@link SystemClock} instance.
	 * @return	The instance.
	 */
	public static SystemClock instance() {
		return InstanceHolder.INSTANCE;
	}

	/**
	 * Queues the current system time in milliseconds.
	 * @return	The current system time in milliseconds.
	 */
	public static long queue() {
		return instance().currentTimeMillis;
	}

	/**
	 * Queues the elapsed time since the origin time.
	 * @return	The elapsed time since the origin time.
	 */
	public static long queueElapsed() {
		return queue() - instance().origin;
	}

	/**
	 * Provides the {@link SystemClock} instance.
	 */
	private static class InstanceHolder {
		private static final SystemClock INSTANCE = new SystemClock();
	}
}
