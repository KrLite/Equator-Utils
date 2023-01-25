package net.krlite.equator.animation.base;

import net.krlite.equator.animation.core.BasicAnimator;

/**
 * An abstract animator that can be used to animate any object
 * that has an abstract value.
 *
 * @param <A>	The type of the value.
 */
public abstract class ValueAnimator<A> implements BasicAnimator<A> {
	/**
	 * The animator's start and end value.
	 */
	protected final A start, end;

	/**
	 * The animator's current and target value.
	 */
	protected A value, target;

	/**
	 * The animator's current animation ratio.
	 */
	protected final double delta;

	/**
	 * Creates a new value animator.
	 *
	 * @param start		The animator's start value.
	 * @param end		The animator's end value.
	 * @param delta		The animator's animation ratio.
	 */
	public ValueAnimator(A start, A end, double delta) {
		this.value = this.start = start;
		this.target = this.end = end;
		this.delta = Math.max(0, Math.min(1, delta));
	}

	/**
	 * Creates a new value animator with the default
	 * animation ratio of 0.075.
	 *
	 * @param start		The animator's start value.
	 * @param end		The animator's end value.
	 */
	public ValueAnimator(A start, A end) {
		this(start, end, 0.075);
	}

	/**
	 * Starts the animator.
	 */
	@Override
	public void forward() {
		target = end;
	}

	/**
	 * Reverses and starts the animator.
	 */
	@Override
	public void backward() {
		target = start;
	}
}
