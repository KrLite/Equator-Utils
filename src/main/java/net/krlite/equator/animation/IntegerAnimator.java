package net.krlite.equator.animation;

import net.krlite.equator.animation.base.ValueAnimator;

public class IntegerAnimator extends ValueAnimator<Integer> {
	private final DoubleAnimator animator;

	public IntegerAnimator(int start, int end, double delta) {
		super(start, end, delta);
		animator = new DoubleAnimator(start, end, delta);
	}

	public IntegerAnimator(int start, int end) {
		super(start, end);
		animator = new DoubleAnimator(start, end);
	}

	public IntegerAnimator(int end) {
		this(0, end);
	}

	@Override
	public Integer queue() {
		return value = animator.queue().intValue();
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
