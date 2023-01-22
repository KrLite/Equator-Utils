package net.krlite.equator.animation;

import net.krlite.equator.animation.core.BasicAnimator;

public class LongAnimator implements BasicAnimator<Long> {
	private final DoubleAnimator animator;

	public LongAnimator(long start, long end, double delta) {
		animator = new DoubleAnimator(start, end, delta);
	}

	public LongAnimator(long start, long end) {
		animator = new DoubleAnimator(start, end);
	}

	public LongAnimator(long end) {
		this(0, end);
	}

	@Override
	public Long queue() {
		return animator.queue().longValue();
	}

	@Override
	public void forward() {
		animator.forward();
	}

	@Override
	public void backward() {
		animator.backward();
	}

	@Override
	public boolean isFinished() {
		return animator.isNearFinished();
	}
}
