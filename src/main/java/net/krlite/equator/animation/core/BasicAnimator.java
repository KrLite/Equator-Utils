package net.krlite.equator.animation.core;

public interface BasicAnimator<A> {
	/**
	 * Queues the animator's current value.
	 * If the animator is in time based mode, the
	 * action should be calculated based on the
	 * current system time.
	 * Otherwise, the action should return the
	 * calculated value of the next frame.
	 * @return	The animator's current value.
	 */
	A queue();

	void forward();

	void backward();

	/**
	 * Checks if the animator has finished its
	 * animation.
	 * @return	<code>true</code> if the animator has
	 * 			finished its animation.
	 * 			Otherwise <code>false</code>.
	 */
	boolean isFinished();
}
