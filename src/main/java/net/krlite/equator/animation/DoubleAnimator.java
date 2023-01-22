package net.krlite.equator.animation;

import net.krlite.equator.animation.base.ValueAnimator;
import net.krlite.equator.math.EasingFunctions;

public class DoubleAnimator extends ValueAnimator<Double> {
	public DoubleAnimator(double start, double end, long lasting) {
		super(start, end, lasting);
	}

	public DoubleAnimator(double end, long lasting) {
		this(0, end, lasting);
	}

	public DoubleAnimator(long lasting) {
		this(lasting, lasting);
	}

	@Override
	public Double queue() {
		return EasingFunctions.Sinusoidal.ease(timer, end);
	}

	@Override
	public Double reverted() {
		return timer.getLasting() - queue();
	}
}
