package net.krlite.equator.animation;

import net.krlite.equator.animation.base.ValueAnimator;

public class LongAnimator extends ValueAnimator<Long> {
	private final DoubleAnimator animator;

	public LongAnimator(long start, long end, double delta) {
		super(start, end, delta);
		animator = new DoubleAnimator(start, end, delta);
	}

	public LongAnimator(long start, long end) {
		super(start, end);
		animator = new DoubleAnimator(start, end);
	}

	public LongAnimator(long end) {
		this(0, end);
	}

	@Override
	public Long queue() {
		return value = animator.queue().longValue();
	}

	@Override
	public void forward() {
		super.forward();
		animator.forward();
	}

	@Override
	public void backward() {
		super.backward();
		animator.backward();
	}

	@Override
	public boolean isFinished() {
		return animator.isNearFinished();
	}
}
