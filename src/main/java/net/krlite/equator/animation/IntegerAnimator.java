package net.krlite.equator.animation;

import net.krlite.equator.animation.base.ValueAnimator;
import net.krlite.equator.math.EasingFunctions;

public class IntegerAnimator extends ValueAnimator<Integer> {
	public IntegerAnimator(int start, int end, long lasting) {
		super(start, end, lasting);
	}

	public IntegerAnimator(int end, long lasting) {
		this(0, end, lasting);
	}

	public IntegerAnimator(long lasting) {
		this(Math.toIntExact(lasting), lasting);
	}

	@Override
	public Integer queue() {
		return (int) EasingFunctions.Sinusoidal.ease(timer, end);
	}

	@Override
	public Integer reverted() {
		return Math.toIntExact(timer.getLasting() - queue());
	}
}
