package net.krlite.equator.animation;

import net.krlite.equator.animation.base.ValueAnimator;

public class FloatAnimator extends ValueAnimator<Float> {
	public FloatAnimator(float start, float end, double delta) {
		super(start, end, delta);
	}

	public FloatAnimator(float start, float end) {
		super(start, end);
	}

	public FloatAnimator(float end) {
		this(0, end);
	}

	@Override
	public Float queue() {
		return value += (target - value) * (float) delta;
	}

	@Override
	public boolean isFinished() {
		return Math.abs(target - value) < 0.001;
	}

	public boolean isNearFinished() {
		return Math.abs(target - value) < 0.1;
	}
}
