package net.krlite.equator.animation.base;

import net.krlite.equator.animation.core.BasicAnimator;

@SuppressWarnings("allJavadoc")
public abstract class ValueAnimator<A extends Number> implements BasicAnimator<A> {
	protected final A start, end;
	protected A value, target;
	protected final double delta;

	public ValueAnimator(A start, A end, double delta) {
		this.value = this.start = start;
		this.target = this.end = end;
		this.delta = Math.max(0, Math.min(1, delta));
	}

	public ValueAnimator(A start, A end) {
		this(start, end, 0.1);
	}

	@Override
	public void forward() {
		target = end;
	}

	@Override
	public void backward() {
		target = start;
	}
}
