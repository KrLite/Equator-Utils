package net.krlite.equator.animation;

import net.krlite.equator.animation.core.BasicAnimator;

public class IntegerAnimator implements BasicAnimator<Integer> {
	private final DoubleAnimator animator;

	public IntegerAnimator(int start, int end, double delta) {
		animator = new DoubleAnimator(start, end, delta);
	}

	public IntegerAnimator(int start, int end) {
		animator = new DoubleAnimator(start, end);
	}

	public IntegerAnimator(int end) {
		this(0, end);
	}

	@Override
	public Integer queue() {
		return animator.queue().intValue();
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
