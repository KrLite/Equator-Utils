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

	/**
	 * Reverts the value.
	 * @return	The reverted value.
	 */
	A reverted();

	/**
	 * Resets the animator's value to its
	 * starting value.
	 */
	void reset();

	/**
	 * Enables time based mode.
	 * The time based mode should be disabled by
	 * default.
	 */
	void enableTimeBasedAnimation();

	/**
	 * Disables time based mode.
	 */
	void disableTimeBasedAnimation();

	/**
	 * Checks if the animator is in time based mode.
	 * @return	<code>true</code> if the animator is
	 * 			in time based mode.
	 * 			Otherwise <code>false</code>.
	 */
	boolean isTimeBasedAnimationEnabled();

	/**
	 * Checks if the animator has finished its
	 * animation.
	 * @return	<code>true</code> if the animator has
	 * 			finished its animation.
	 * 			Otherwise <code>false</code>.
	 */
	boolean isFinished();
}
