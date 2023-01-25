package net.krlite.equator.animation;

import net.krlite.equator.animation.base.ValueAnimator;

/**
 * An animator that can be used to animate double values.
 *
 * @see ValueAnimator
 */
public class DoubleAnimator extends ValueAnimator<Double> {
	public DoubleAnimator(double start, double end, double delta) {
		super(start, end, delta);
	}

	public DoubleAnimator(double start, double end) {
		super(start, end);
	}

	public DoubleAnimator(double end) {
		this(0, end);
	}

	@Override
	public Double queue() {
		return value += (target - value) * delta;
	}

	@Override
	public boolean isFinished() {
		return Math.abs(target - value) < 0.001;
	}

	public boolean isNearFinished() {
		return Math.abs(target - value) < 0.1;
	}
}
