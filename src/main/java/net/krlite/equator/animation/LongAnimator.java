package net.krlite.equator.animation;

import net.krlite.equator.animation.base.ValueAnimator;
import net.krlite.equator.math.EasingFunctions;

public class LongAnimator extends ValueAnimator<Long> {
	public LongAnimator(long start, long end, long lasting) {
		super(start, end, lasting);
	}

	public LongAnimator(long end, long lasting) {
		this(0, end, lasting);
	}

	public LongAnimator(long lasting) {
		this(Math.toIntExact(lasting), lasting);
	}

	@Override
	public Long queue() {
		return (long) EasingFunctions.Sinusoidal.ease(timer, end);
	}

	@Override
	public Long reverted() {
		return timer.getLasting() - queue();
	}
}
