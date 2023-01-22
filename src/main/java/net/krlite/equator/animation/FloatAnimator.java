package net.krlite.equator.animation;

import net.krlite.equator.animation.base.ValueAnimator;
import net.krlite.equator.math.EasingFunctions;

public class FloatAnimator extends ValueAnimator<Float> {
	public FloatAnimator(float start, float end, long lasting) {
		super(start, end, lasting);
	}

	public FloatAnimator(float end, long lasting) {
		this(0, end, lasting);
	}

	public FloatAnimator(long lasting) {
		this(lasting, lasting);
	}

	@Override
	public Float queue() {
		return (float) EasingFunctions.Sinusoidal.ease(timer, end);
	}

	@Override
	public Float reverted() {
		return timer.getLasting() - queue();
	}
}
