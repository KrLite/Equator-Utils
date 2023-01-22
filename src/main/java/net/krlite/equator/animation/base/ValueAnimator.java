package net.krlite.equator.animation.base;

import net.krlite.equator.animation.core.BasicAnimator;
import net.krlite.equator.util.Timer;

@SuppressWarnings("allJavadoc")
public abstract class ValueAnimator<A extends Number> implements BasicAnimator<A> {
	protected final A start, end;
	protected final Timer timer;

	public ValueAnimator(A start, A end, long lasting) {
		this.start = start;
		this.end = end;
		this.timer = new Timer(lasting).operate(Timer::enterStepping);
	}

	@Override
	public final void reset() {
		timer.reset();
	}

	@Override
	public final void enableTimeBasedAnimation() {
		timer.quitStepping();
	}

	@Override
	public final void disableTimeBasedAnimation() {
		timer.enterStepping();
	}

	@Override
	public final boolean isTimeBasedAnimationEnabled() {
		return !timer.isStepping();
	}

	@Override
	public boolean isFinished() {
		return timer.isFinished();
	}
}
