package net.krlite.equator.math;

import net.krlite.equator.util.SystemClock;
import net.krlite.equator.util.Timer;
import net.krlite.equator.util.list.PairList;
import net.krlite.equator.util.pair.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicLong;

/**
 * <h2>Easing Functions</h2>
 * A class that provides different kinds of easing functions.
 */
public class EasingFunctions {
	protected interface MultiFunctionable {
		double apply(double progress, double origin, double shift, double duration);

		default double apply(double percentage) {
			return apply(percentage, 0, 1, 1);
		}

		default double apply(Timer timer, double shift) {
			return apply(timer.queue(), 0, shift, timer.getLasting());
		}

		default double apply(Timer timer) {
			return apply(timer, 1);
		}
	}

	/**
	 * A functional interface representing an operation
	 * that processes 4 {@code double}-valued arguments and
	 * produces a {@code double}-valued result.
	 */
	@FunctionalInterface
	public interface QuadDoubleFunction {
		/**
		 * Applies the function to the given arguments.
		 *
		 * @param progress	The progress of the operation, relative to the
		 *                 	duration.
		 * @param origin	The origin of the operation.
		 * @param shift		The shift of the operation.
		 * @param duration	The duration of the operation.
		 * @return			The function result.
		 */
		double apply(double progress, double origin, double shift, double duration);

		/**
		 * A {@link QuadDoubleFunction} that returns zero.
		 */
		QuadDoubleFunction NONE = (p, o, s, d) -> 0;
	}

	/**
	 * A class to combine multiple {@link QuadDoubleFunction}s.
	 */
	public static class Combined implements MultiFunctionable {
		/**
		 * The {@link PairList} of {@link QuadDoubleFunction}s.
		 */
		private @NotNull final PairList<QuadDoubleFunction, Integer> functions;

		/**
		 * Gets the total weight.
		 *
		 * @return	The total weight.
		 */
		private long getTotalWeight() {
			return functions.stream().mapToInt(Pair::getSecond).sum();
		}

		/**
		 * Queues the current {@link QuadDoubleFunction}.
		 *
		 * @param percentage	The percentage of the operation.
		 * @return				The queued {@link QuadDoubleFunction}.
		 */
		private QuadDoubleFunction current(double percentage) {
			AtomicLong accumulatedWeight = new AtomicLong(0);
			final long totalWeight = getTotalWeight();
			final double percentageWeight = percentage * totalWeight;
			return functions.stream().filter(entry -> accumulatedWeight.addAndGet(entry.getSecond()) >= percentageWeight)
						   .findFirst().map(entry -> (QuadDoubleFunction) (p, o, s, d) ->
											  entry.getFirst().apply((percentageWeight - (accumulatedWeight.get() - entry.getSecond())) / (entry.getSecond().doubleValue() / totalWeight), o, s, totalWeight)
						   ).orElse(QuadDoubleFunction.NONE);
		}

		/**
		 * Creates a new {@link Combined} instance.
		 */
		public Combined() {
			this.functions = new PairList<>();
		}

		/**
		 * Adds a {@link QuadDoubleFunction} to the {@link Combined}.
		 *
		 * @param function	The {@link QuadDoubleFunction} to add.
		 * @param weight	The weight of the {@link QuadDoubleFunction}.
		 * @return			The {@link Combined} instance.
		 */
		public Combined append(@NotNull QuadDoubleFunction function, int weight) {
			functions.add(new Pair<>(function, weight));
			return this;
		}

		/**
		 * Adds a negate {@link QuadDoubleFunction} to the {@link Combined}.
		 *
		 * @param function	The {@link QuadDoubleFunction} to add, which will
		 *                  be negated.
		 * @param weight	The weight of the {@link QuadDoubleFunction}.
		 * @return			The {@link Combined} instance.
		 */
		public Combined appendNegate(@NotNull QuadDoubleFunction function, int weight) {
			return append((p, o, s, d) -> function.apply(p, o + s, -s, d), weight);
		}

		/**
		 * Applies the function to the given arguments.
		 *
		 * @param progress	The progress of the operation, relative to the
		 *                  duration.
		 * @param origin	The origin of the operation.
		 * @param shift		The shift of the operation.
		 * @param duration	The duration of the operation.
		 * @return			The function result.
		 */
		public double apply(double progress, double origin, double shift, double duration) {
			return current(progress / duration).apply(progress, origin, shift, duration);
		}
	}

	/**
	 * A class to concur multiple {@link MultiFunctionable}s.
	 */
	public static class Concurred implements MultiFunctionable {
		/**
		 * The two {@link QuadDoubleFunction}s to concur.
		 */
		private final @NotNull QuadDoubleFunction functionFirst, functionSecond;

		/**
		 * Creates a new {@link Concurred} instance with two {@link QuadDoubleFunction}s.
		 *
		 * @param functionFirst		The first {@link QuadDoubleFunction}.
		 * @param functionSecond	The second {@link QuadDoubleFunction}.
		 */
		public Concurred(@NotNull QuadDoubleFunction functionFirst, @NotNull QuadDoubleFunction functionSecond) {
			this.functionFirst = functionFirst;
			this.functionSecond = functionSecond;
		}

		/**
		 * Creates a new {@link Concurred} instance with one {@link QuadDoubleFunction},
		 * concurs with the negated self.
		 * @param function	The {@link QuadDoubleFunction}.
		 */
		public Concurred(@NotNull QuadDoubleFunction function) {
			this(function, (p, o, s, d) -> function.apply(d - p, o, s, d));
		}

		/**
		 * Applies the function to the given arguments.
		 *
		 * @param progress	The progress of the operation, relative to the
		 *                  duration.
		 * @param origin	The origin of the operation.
		 * @param shift		The shift of the operation.
		 * @param duration	The duration of the operation.
		 * @return			The function result.
		 */
		public double apply(double progress, double origin, double shift, double duration) {
			return functionFirst.apply(progress, origin, shift, duration) + functionSecond.apply(progress, origin, shift, duration);
		}
	}

	/**
	 * Powers the value by an integer.
	 *
	 * @param value The dedicated value.
	 * @param exp   The power.
	 * @return 		The powered value.
	 */
	private static double pow(double value, int exp) {
		return Math.pow(value, exp);
	}

	/**
	 * Powers the value by 2.
	 *
	 * @param value The dedicated value.
	 * @return 		The powered value.
	 */
	private static double pow(double value) {
		return pow(value, 2);
	}

	/**
	 * Sinusoidal reciprocating function based on the
	 * system time.
	 *
	 * @param speed The speed of the reciprocation.
	 * @return 		The sinusoidal reciprocating value.
	 */
	public static double sin(double speed) {
		return Math.sin(SystemClock.queueElapsed() * 0.001 * speed);
	}

	/**
	 * Sinusoidal reciprocating function based on the
	 * system time, with a period of 1 second.
	 *
	 * @return	The sinusoidal reciprocating value.
	 */
	public static double sin() {
		return sin(1);
	}

	/**
	 * Cosine reciprocating function based on the
	 * system time.
	 *
	 * @param speed The speed of the reciprocation.
	 * @return 		The cosine reciprocating value.
	 */
	public static double cos(double speed) {
		return Math.cos(SystemClock.queueElapsed() * 0.001 * speed);
	}

	/**
	 * Cosine reciprocating function based on the
	 * system time, with a period of 1 second.
	 *
	 * @return	The cosine reciprocating value.
	 */
	public static double cos() {
		return cos(1);
	}

	/**
	 * Tangent reciprocating function based on the
	 * system time.
	 *
	 * @param speed The speed of the reciprocation.
	 * @return 		The tangent reciprocating value.
	 */
	public static double tan(double speed) {
		return Math.tan(SystemClock.queueElapsed() * 0.001 * speed);
	}

	/**
	 * Tangent reciprocating function based on the
	 * system time, with a period of 1 second.
	 *
	 * @return	The tangent reciprocating value.
	 */
	public static double tan() {
		return tan(1);
	}

	/**
	 * Sinusoidal reciprocating function (absolute
	 * value) based on the system time.
	 *
	 * @param speed The speed of the reciprocation.
	 * @return 		The sinusoidal reciprocating value.
	 */
	public static double sinPositive(double speed) {
		return Math.abs(sin(speed));
	}

	/**
	 * Sinusoidal reciprocating function (absolute
	 * value) based on the system time, with a period
	 * of 1 second.
	 *
	 * @return	The sinusoidal reciprocating value.
	 */
	public static double sinPositive() {
		return sinPositive(1);
	}

	/**
	 * Cosine reciprocating function (absolute value)
	 * based on the system time.
	 *
	 * @param speed The speed of the reciprocation.
	 * @return 		The cosine reciprocating value.
	 */
	public static double cosPositive(double speed) {
		return Math.abs(cos(speed));
	}

	/**
	 * Cosine reciprocating function (absolute value)
	 * based on the system time, with a period of 1
	 * second.
	 *
	 * @return	The cosine reciprocating value.
	 */
	public static double cosPositive() {
		return cosPositive(1);
	}

	/**
	 * Sinusoidal reciprocating function (normal value
	 * in [0, 1]) based on the system time.
	 *
	 * @param speed The speed of the reciprocation.
	 * @return 		The sinusoidal reciprocating value.
	 */
	public static double sinNormal(double speed) {
		return sin(speed) / 2 + 0.5;
	}

	/**
	 * Sinusoidal reciprocating function (normal value
	 * in [0, 1]) based on the system time, with a
	 * period of 1 second.
	 *
	 * @return	The sinusoidal reciprocating value.
	 */
	public static double sinNormal() {
		return sinNormal(1);
	}

	/**
	 * Cosine reciprocating function (normal value in
	 * [0, 1]) based on the system time.
	 *
	 * @param speed The speed of the reciprocation.
	 * @return 		The cosine reciprocating value.
	 */
	public static double cosNormal(double speed) {
		return cos(speed) / 2 + 0.5;
	}

	/**
	 * Cosine reciprocating function (normal value in
	 * [0, 1]) based on the system time, with a period
	 * of 1 second.
	 *
	 * @return	The cosine reciprocating value.
	 */
	public static double cosNormal() {
		return cosNormal(1);
	}

	/**
	 * Tangent reciprocating function (reciprocated,
	 * divided by 1) based on the system time.
	 *
	 * @param speed The speed of the reciprocation.
	 * @return 		The tangent reciprocating value.
	 */
	public static double tanReciprocal(double speed) {
		return 1 / tan(speed);
	}

	/**
	 * Tangent reciprocating function (reciprocated,
	 * divided by 1) based on the system time, with a
	 * period of 1 second.
	 *
	 * @return	The tangent reciprocating value.
	 */
	public static double tanReciprocal() {
		return tanReciprocal(1);
	}


	/**
	 * Linear easing function.
	 * <code>f(x)=x</code>
	 */
	public static class Linear {
		/**
		 * <code>f(x)=x</code><br />
		 * Linear easing function taking zero as the origin.
		 *
		 * @param timer The dedicated {@link Timer}.
		 * @param shift The distance to shift the value.
		 * @return The eased linear value.
		 */
		public static double ease(@NotNull Timer timer, double shift) {
			return ease(timer.queueAsPercentage(), 0, shift);
		}

		/**
		 * <code>f(x)=x</code><br />
		 * Linear easing function, a simple version of
		 * {@link #ease(double, double, double, double) easeLinear.}
		 *
		 * @param percentage 	Current progress as percentage value between zero and one.
		 * @param origin		The original value.
		 * @param shift			The distance to shift the value.
		 * @return The linear eased value.
		 */
		public static double ease(double percentage, double origin, double shift) {
			return shift * percentage + origin;
		}

		/**
		 * <code>f(x)=x</code><br />
		 * Linear easing function.
		 *
		 * @param progress Current progress.
		 * @param origin   The original value.
		 * @param shift    The distance to shift the value.
		 * @param duration The duration time.
		 * @return The linear eased value.
		 */
		public static double ease(double progress, double origin, double shift, double duration) {
			return shift * (progress / duration) + origin;
		}
	}

	/**
	 * Quadratic easing function.
	 * <code>f(x)=x^2</code>
	 */
	public static class Quadratic {
		/**
		 * <code>f(x)=x^2</code><br />
		 * Quadratic easing function taking zero as the origin.
		 *
		 * @param timer The dedicated {@link Timer}.
		 * @param shift The distance to shift the value.
		 * @return The eased quadratic value.
		 */
		public static double ease(@NotNull Timer timer, double shift) {
			return ease(timer.queue(), 0, shift, timer.getLasting());
		}

		/**
		 * <code>f(x)=x^2</code><br />
		 * Quadratic easing function in taking zero as the origin.
		 *
		 * @param timer The dedicated {@link Timer}.
		 * @param shift The distance to shift the value.
		 * @return The eased quadratic value.
		 */
		public static double easeIn(@NotNull Timer timer, double shift) {
			return easeIn(timer.queue(), 0, shift, timer.getLasting());
		}

		/**
		 * <code>f(x)=x^2</code><br />
		 * Quadratic easing function out taking zero as the origin.
		 *
		 * @param timer The dedicated {@link Timer}.
		 * @param shift The distance to shift the value.
		 * @return The eased quadratic value.
		 */
		public static double easeOut(@NotNull Timer timer, double shift) {
			return easeOut(timer.queue(), 0, shift, timer.getLasting());
		}

		/**
		 * <code>f(x)=x^2</code><br />
		 * Quadratic easing function.
		 *
		 * @param progress Current progress.
		 * @param origin   The original value.
		 * @param shift    The distance to shift the value.
		 * @param duration The duration time.
		 * @return The quadratic eased value.
		 */
		public static double ease(double progress, double origin, double shift, double duration) {
			return (progress /= (duration / 2)) < 1
						   ? shift / 2 * pow(progress) + origin
						   : -shift / 2 * ((--progress) * (progress - 2) - 1) + origin;
		}

		/**
		 * <code>f(x)=x^2</code><br />
		 * Quadratic easing function in.
		 *
		 * @param progress Current progress.
		 * @param origin   The original value.
		 * @param shift    The distance to shift the value.
		 * @param duration The duration time.
		 * @return The quadratic in eased value.
		 */
		public static double easeIn(double progress, double origin, double shift, double duration) {
			return shift * (progress /= duration) * progress + origin;
		}

		/**
		 * <code>f(x)=x^2</code><br />
		 * Quadratic easing function out.
		 *
		 * @param progress Current progress.
		 * @param origin   The original value.
		 * @param shift    The distance to shift the value.
		 * @param duration The duration time.
		 * @return The quadratic out eased value.
		 */
		public static double easeOut(double progress, double origin, double shift, double duration) {
			return -shift * (progress /= duration) * (progress - 2) + origin;
		}
	}

	/**
	 * Cubic easing function.
	 * <code>f(x)=x^3</code>
	 */
	public static class Cubic {
		/**
		 * <code>f(x)=x^3</code><br />
		 * Cubic easing function taking zero as the origin.
		 *
		 * @param timer The dedicated {@link Timer}.
		 * @param shift The distance to shift the value.
		 * @return The eased cubic value.
		 */
		public static double ease(@NotNull Timer timer, double shift) {
			return ease(timer.queue(), 0, shift, timer.getLasting());
		}

		/**
		 * <code>f(x)=x^3</code><br />
		 * Cubic easing function in taking zero as the origin.
		 *
		 * @param timer The dedicated {@link Timer}.
		 * @param shift The distance to shift the value.
		 * @return The eased cubic value.
		 */
		public static double easeIn(@NotNull Timer timer, double shift) {
			return easeIn(timer.queue(), 0, shift, timer.getLasting());
		}

		/**
		 * <code>f(x)=x^3</code><br />
		 * Cubic easing function out taking zero as the origin.
		 *
		 * @param timer The dedicated {@link Timer}.
		 * @param shift The distance to shift the value.
		 * @return The eased cubic value.
		 */
		public static double easeOut(@NotNull Timer timer, double shift) {
			return easeOut(timer.queue(), 0, shift, timer.getLasting());
		}

		/**
		 * <code>f(x)=x^3</code><br />
		 * Easing cubic function.
		 *
		 * @param progress Current progress.
		 * @param origin   The original value.
		 * @param shift    The distance to shift the value.
		 * @param duration The duration time.
		 * @return The cubic eased value.
		 */
		public static double ease(double progress, double origin, double shift, double duration) {
			return (progress /= (duration / 2)) < 1
						   ? shift / 2 * pow(progress, 3) + origin
						   : -shift / 2 * ((progress -= 2) * pow(progress) + 2) + origin;
		}

		/**
		 * <code>f(x)=x^3</code><br />
		 * Easing cubic function in.
		 *
		 * @param progress Current progress.
		 * @param origin   The original value.
		 * @param shift    The distance to shift the value.
		 * @param duration The duration time.
		 * @return The cubic in eased value.
		 */
		public static double easeIn(double progress, double origin, double shift, double duration) {
			return shift * (progress /= duration) * pow(progress) + origin;
		}

		/**
		 * <code>f(x)=x^3</code><br />
		 * Easing cubic function out.
		 *
		 * @param progress Current progress.
		 * @param origin   The original value.
		 * @param shift    The distance to shift the value.
		 * @param duration The duration time.
		 * @return The cubic out eased value.
		 */
		public static double easeOut(double progress, double origin, double shift, double duration) {
			return -shift * ((progress = progress / duration - 1) * pow(progress) + 1) + origin;
		}
	}

	/**
	 * Quartic easing function.
	 * <code>f(x)=x^4</code>
	 */
	public static class Quartic {
		/**
		 * <code>f(x)=x^4</code><br />
		 * Quartic easing function taking zero as the origin.
		 *
		 * @param timer The dedicated {@link Timer}.
		 * @param shift The distance to shift the value.
		 * @return The eased quartic value.
		 */
		public static double ease(@NotNull Timer timer, double shift) {
			return ease(timer.queue(), 0, shift, timer.getLasting());
		}

		/**
		 * <code>f(x)=x^4</code><br />
		 * Quartic easing function in taking zero as the origin.
		 *
		 * @param timer The dedicated {@link Timer}.
		 * @param shift The distance to shift the value.
		 * @return The eased quartic value.
		 */
		public static double easeIn(@NotNull Timer timer, double shift) {
			return easeIn(timer.queue(), 0, shift, timer.getLasting());
		}

		/**
		 * <code>f(x)=x^4</code><br />
		 * Quartic easing function out taking zero as the origin.
		 *
		 * @param timer The dedicated {@link Timer}.
		 * @param shift The distance to shift the value.
		 * @return The eased quartic value.
		 */
		public static double easeOut(@NotNull Timer timer, double shift) {
			return easeOut(timer.queue(), 0, shift, timer.getLasting());
		}

		/**
		 * <code>f(x)=x^4</code><br />
		 * Easing quartic function.
		 *
		 * @param progress Current progress.
		 * @param origin   The original value.
		 * @param shift    The distance to shift the value.
		 * @param duration The duration time.
		 * @return The quartic eased value.
		 */
		public static double ease(double progress, double origin, double shift, double duration) {
			return (progress /= (duration / 2)) < 1
						   ? shift / 2 * pow(progress, 4) + origin
						   : -shift / 2 * ((progress -= 2) * pow(progress, 3) - 2) + origin;
		}

		/**
		 * <code>f(x)=x^4</code><br />
		 * Easing quartic function in.
		 *
		 * @param progress Current progress.
		 * @param origin   The original value.
		 * @param shift    The distance to shift the value.
		 * @param duration The duration time.
		 * @return The quartic in eased value.
		 */
		public static double easeIn(double progress, double origin, double shift, double duration) {
			return shift * (progress /= duration) * pow(progress, 3) + origin;
		}

		/**
		 * <code>f(x)=x^4</code><br />
		 * Easing quartic function out.
		 *
		 * @param progress Current progress.
		 * @param origin   The original value.
		 * @param shift    The distance to shift the value.
		 * @param duration The duration time.
		 * @return The quartic out eased value.
		 */
		public static double easeOut(double progress, double origin, double shift, double duration) {
			return -shift * ((progress = progress / duration - 1) * pow(progress, 3) - 1) + origin;
		}
	}

	/**
	 * Quintic easing function.
	 * <code>f(x)=x^5</code>
	 */
	public static class Quintic {
		/**
		 * <code>f(x)=x^5</code><br />
		 * Quintic easing function taking zero as the origin.
		 *
		 * @param timer The dedicated {@link Timer}.
		 * @param shift The distance to shift the value.
		 * @return The eased quintic value.
		 */
		public static double ease(@NotNull Timer timer, double shift) {
			return ease(timer.queue(), 0, shift, timer.getLasting());
		}

		/**
		 * <code>f(x)=x^5</code><br />
		 * Quintic easing function in taking zero as the origin.
		 *
		 * @param timer The dedicated {@link Timer}.
		 * @param shift The distance to shift the value.
		 * @return The eased quintic value.
		 */
		public static double easeIn(@NotNull Timer timer, double shift) {
			return easeIn(timer.queue(), 0, shift, timer.getLasting());
		}

		/**
		 * <code>f(x)=x^5</code><br />
		 * Quintic easing function out taking zero as the origin.
		 *
		 * @param timer The dedicated {@link Timer}.
		 * @param shift The distance to shift the value.
		 * @return The eased quintic value.
		 */
		public static double easeOut(@NotNull Timer timer, double shift) {
			return easeOut(timer.queue(), 0, shift, timer.getLasting());
		}

		/**
		 * <code>f(x)=x^5</code><br />
		 * Easing quintic function.
		 *
		 * @param progress Current progress.
		 * @param origin   The original value.
		 * @param shift    The distance to shift the value.
		 * @param duration The duration time.
		 * @return The quintic eased value.
		 */
		public static double ease(double progress, double origin, double shift, double duration) {
			return (progress /= (duration / 2)) < 1
						   ? shift / 2 * pow(progress, 4) + origin
						   : -shift / 2 * (progress -= 2) * pow(progress, 3) - 2 + origin;
		}

		/**
		 * <code>f(x)=x^5</code><br />
		 * Easing quintic function in.
		 *
		 * @param progress Current progress.
		 * @param origin   The original value.
		 * @param shift    The distance to shift the value.
		 * @param duration The duration time.
		 * @return The quintic in eased value.
		 */
		public static double easeIn(double progress, double origin, double shift, double duration) {
			return shift * (progress /= duration) * pow(progress, 3) + origin;
		}

		/**
		 * <code>f(x)=x^5</code><br />
		 * Easing quintic function out.
		 *
		 * @param progress Current progress.
		 * @param origin   The original value.
		 * @param shift    The distance to shift the value.
		 * @param duration The duration time.
		 * @return The quintic out eased value.
		 */
		public static double easeOut(double progress, double origin, double shift, double duration) {
			return -shift * (progress = progress / duration - 1) * pow(progress, 3) - 1 + origin;
		}
	}

	/**
	 * Sinusoidal easing function.
	 * <code>f(x)=sin(x)</code>
	 */
	public static class Sinusoidal {
		/**
		 * <code>f(x)=sin(x)</code><br />
		 * Sinusoidal easing function taking zero as the origin.
		 *
		 * @param timer The dedicated {@link Timer}.
		 * @param shift The distance to shift the value.
		 * @return The eased sinusoidal value.
		 */
		public static double ease(@NotNull Timer timer, double shift) {
			return ease(timer.queue(), 0, shift, timer.getLasting());
		}

		/**
		 * <code>f(x)=sin(x)</code><br />
		 * Sinusoidal easing function in taking zero as the origin.
		 *
		 * @param timer The dedicated {@link Timer}.
		 * @param shift The distance to shift the value.
		 * @return The eased sinusoidal value.
		 */
		public static double easeIn(@NotNull Timer timer, double shift) {
			return easeIn(timer.queue(), 0, shift, timer.getLasting());
		}

		/**
		 * <code>f(x)=sin(x)</code><br />
		 * Sinusoidal easing function out taking zero as the origin.
		 *
		 * @param timer The dedicated {@link Timer}.
		 * @param shift The distance to shift the value.
		 * @return The eased sinusoidal value.
		 */
		public static double easeOut(@NotNull Timer timer, double shift) {
			return easeOut(timer.queue(), 0, shift, timer.getLasting());
		}

		/**
		 * <code>f(x)=sin(x)</code><br />
		 * Easing sinusoidal function.
		 *
		 * @param progress Current progress.
		 * @param origin   The original value.
		 * @param shift    The distance to shift the value.
		 * @param duration The duration time.
		 * @return The sinusoidal eased value.
		 */
		public static double ease(double progress, double origin, double shift, double duration) {
			return -shift / 2 * Math.cos(Math.PI * progress / duration - 1) + origin;
		}

		/**
		 * <code>f(x)=sin(x)</code><br />
		 * Easing sinusoidal function in.
		 *
		 * @param progress Current progress.
		 * @param origin   The original value.
		 * @param shift    The distance to shift the value.
		 * @param duration The duration time.
		 * @return The sinusoidal in eased value.
		 */
		public static double easeIn(double progress, double origin, double shift, double duration) {
			return -shift * (Math.cos(progress / duration) * (Math.PI / 2) - 1) + origin;
		}

		/**
		 * <code>f(x)=sin(x)</code><br />
		 * Easing sinusoidal function out.
		 *
		 * @param progress Current progress.
		 * @param origin   The original value.
		 * @param shift    The distance to shift the value.
		 * @param duration The duration time.
		 * @return The sinusoidal out eased value.
		 */
		public static double easeOut(double progress, double origin, double shift, double duration) {
			return shift * Math.cos(progress / duration) * (Math.PI / 2) + origin;
		}
	}

	/**
	 * Exponential easing function.
	 * <code>f(x)=2^(10(x-1))</code>
	 */
	public static class Exponential {
		/**
		 * <code>f(x)=2^(10(x-1))</code><br />
		 * Exponential easing function taking zero as the origin.
		 *
		 * @param timer The dedicated {@link Timer}.
		 * @param shift The distance to shift the value.
		 * @return The eased exponential value.
		 */
		public static double ease(@NotNull Timer timer, double shift) {
			return ease(timer.queue(), 0, shift, timer.getLasting());
		}

		/**
		 * <code>f(x)=2^(10(x-1))</code><br />
		 * Exponential easing function in taking zero as the origin.
		 *
		 * @param timer The dedicated {@link Timer}.
		 * @param shift The distance to shift the value.
		 * @return The eased exponential value.
		 */
		public static double easeIn(@NotNull Timer timer, double shift) {
			return easeIn(timer.queue(), 0, shift, timer.getLasting());
		}

		/**
		 * <code>f(x)=2^(10(x-1))</code><br />
		 * Exponential easing function out taking zero as the origin.
		 *
		 * @param timer The dedicated {@link Timer}.
		 * @param shift The distance to shift the value.
		 * @return The eased exponential value.
		 */
		public static double easeOut(@NotNull Timer timer, double shift) {
			return easeOut(timer.queue(), 0, shift, timer.getLasting());
		}

		/**
		 * <code>f(x)=2^(10(x-1))</code><br />
		 * Easing exponential function.
		 *
		 * @param progress Current progress.
		 * @param origin   The original value.
		 * @param shift    The distance to shift the value.
		 * @param duration The duration time.
		 * @return The exponential eased value.
		 */
		public static double ease(double progress, double origin, double shift, double duration) {
			return progress == 0
						   ? origin
						   : progress == duration
									 ? origin + shift
									 : (progress /= (duration / 2)) < 1
											   ? shift / 2 * Math.pow(2, 10 * (progress - 1)) + origin
											   : shift / 2 * -Math.pow(2, -10 * --progress) + 2 + origin;
		}

		/**
		 * <code>f(x)=2^(10(x-1))</code><br />
		 * Easing exponential function in.
		 *
		 * @param progress Current progress.
		 * @param origin   The original value.
		 * @param shift    The distance to shift the value.
		 * @param duration The duration time.
		 * @return The exponential in eased value.
		 */
		public static double easeIn(double progress, double origin, double shift, double duration) {
			return progress == 0 ? origin : shift * Math.pow(2, 10 * (progress / duration - 1)) + origin;
		}

		/**
		 * <code>f(x)=2^(10(x-1))</code><br />
		 * Easing exponential function out.
		 *
		 * @param progress Current progress.
		 * @param origin   The original value.
		 * @param shift    The distance to shift the value.
		 * @param duration The duration time.
		 * @return The exponential out eased value.
		 */
		public static double easeOut(double progress, double origin, double shift, double duration) {
			return progress == duration ? origin + shift : shift * (-Math.pow(2, -10 * progress / duration) + 1) + origin;
		}
	}

	/**
	 * Circular easing function.
	 * <code>f(x)=sqrt(1-x^2)</code>
	 */
	public static class Circular {
		/**
		 * <code>f(x)=sqrt(1-x^2)</code><br />
		 * Circular easing function taking zero as the origin.
		 *
		 * @param timer The dedicated {@link Timer}.
		 * @param shift The distance to shift the value.
		 * @return The eased circular value.
		 */
		public static double ease(@NotNull Timer timer, double shift) {
			return ease(timer.queue(), 0, shift, timer.getLasting());
		}

		/**
		 * <code>f(x)=sqrt(1-x^2)</code><br />
		 * Circular easing function in taking zero as the origin.
		 *
		 * @param timer The dedicated {@link Timer}.
		 * @param shift The distance to shift the value.
		 * @return The eased circular value.
		 */
		public static double easeIn(@NotNull Timer timer, double shift) {
			return easeIn(timer.queue(), 0, shift, timer.getLasting());
		}

		/**
		 * <code>f(x)=sqrt(1-x^2)</code><br />
		 * Circular easing function out taking zero as the origin.
		 *
		 * @param timer The dedicated {@link Timer}.
		 * @param shift The distance to shift the value.
		 * @return The eased circular value.
		 */
		public static double easeOut(@NotNull Timer timer, double shift) {
			return easeOut(timer.queue(), 0, shift, timer.getLasting());
		}

		/**
		 * <code>f(x)=sqrt(1-x^2)</code><br />
		 * Easing circular function.
		 *
		 * @param progress Current progress.
		 * @param origin   The original value.
		 * @param shift    The distance to shift the value.
		 * @param duration The duration time.
		 * @return The circular eased value.
		 */
		public static double ease(double progress, double origin, double shift, double duration) {
			return (progress /= (duration / 2)) < 1
						   ? -shift / 2 * (Math.sqrt(1 - pow(progress)) - 1) + origin
						   : shift / 2 * (Math.sqrt(1 - (progress -= 2) * progress) + 1) + origin;
		}

		/**
		 * <code>f(x)=sqrt(1-x^2)</code><br />
		 * Easing circular function in.
		 *
		 * @param progress Current progress.
		 * @param origin   The original value.
		 * @param shift    The distance to shift the value.
		 * @param duration The duration time.
		 * @return The circular in eased value.
		 */
		public static double easeIn(double progress, double origin, double shift, double duration) {
			return -shift * (Math.sqrt(1 - (progress /= duration) * progress) - 1) + origin;
		}

		/**
		 * <code>f(x)=sqrt(1-x^2)</code><br />
		 * Easing circular function out.
		 *
		 * @param progress Current progress.
		 * @param origin   The original value.
		 * @param shift    The distance to shift the value.
		 * @param duration The duration time.
		 * @return The circular out eased value.
		 */
		public static double easeOut(double progress, double origin, double shift, double duration) {
			return shift * Math.sqrt(1 - (progress = progress / duration - 1) * progress) + origin;
		}
	}

	/**
	 * Elastic easing function.
	 */
	public static class Elastic {
		/**
		 * Elastic easing function taking zero as the origin.
		 *
		 * @param timer The dedicated {@link Timer}.
		 * @param shift The distance to shift the value.
		 * @return The eased elastic value.
		 */
		public static double ease(@NotNull Timer timer, double shift) {
			return ease(timer.queue(), 0, shift, timer.getLasting());
		}

		/**
		 * Elastic easing function in taking zero as the origin.
		 *
		 * @param timer The dedicated {@link Timer}.
		 * @param shift The distance to shift the value.
		 * @return The eased elastic value.
		 */
		public static double easeIn(@NotNull Timer timer, double shift) {
			return easeIn(timer.queue(), 0, shift, timer.getLasting());
		}

		/**
		 * Elastic easing function out taking zero as the origin.
		 *
		 * @param timer The dedicated {@link Timer}.
		 * @param shift The distance to shift the value.
		 * @return The eased elastic value.
		 */
		public static double easeOut(@NotNull Timer timer, double shift) {
			return easeOut(timer.queue(), 0, shift, timer.getLasting());
		}

		/**
		 * Easing elastic function.
		 *
		 * @param progress Current progress.
		 * @param origin   The original value.
		 * @param shift    The distance to shift the value.
		 * @param duration The duration time.
		 * @return The elastic eased value.
		 */
		public static double ease(double progress, double origin, double shift, double duration) {
			double s, p = duration * 0.3 * 1.5, a = shift;
			if (progress == 0) return origin;
			if ((progress /= (duration / 2)) == 2) return origin + shift;
			if (a < Math.abs(shift)) {
				a = shift;
				s = p / 4;
			} else {
				s = p / (2 * Math.PI) * Math.asin(shift / a);
			}
			if (progress < 1)
				return -0.5 * (a * Math.pow(2, 10 * progress--) * Math.sin((progress * duration - s) * (2 * Math.PI) / p)) + origin;
			return 0.5 * (a * Math.pow(2, -10 * progress--) * Math.sin((progress * duration - s) * (2 * Math.PI) / p)) + origin + shift;
		}

		/**
		 * Easing elastic function in.
		 *
		 * @param progress Current progress.
		 * @param origin   The original value.
		 * @param shift    The distance to shift the value.
		 * @param duration The duration time.
		 * @return The elastic in eased value.
		 */
		public static double easeIn(double progress, double origin, double shift, double duration) {
			double s, p = duration * 0.3, a = shift;
			if (progress == 0) return origin;
			if ((progress /= duration) == 1) return origin + shift;
			if (a < Math.abs(shift)) {
				a = shift;
				s = p / 4;
			} else {
				s = p / (2 * Math.PI) * Math.asin(shift / a);
			}
			return -(a * Math.pow(2, 10 * progress--) * Math.sin((progress * duration - s) * (2 * Math.PI) / p)) + origin;
		}

		/**
		 * Easing elastic function out.
		 *
		 * @param progress Current progress.
		 * @param origin   The original value.
		 * @param shift    The distance to shift the value.
		 * @param duration The duration time.
		 * @return The elastic out eased value.
		 */
		public static double easeOut(double progress, double origin, double shift, double duration) {
			double s, p = duration * 0.3, a = shift;
			if (progress == 0) return origin;
			if ((progress /= duration) == 1) return origin + shift;
			if (a < Math.abs(shift)) {
				a = shift;
				s = p / 4;
			} else {
				s = p / (2 * Math.PI) * Math.asin(shift / a);
			}
			return a * Math.pow(2, 10 * progress) * Math.sin((progress * duration - s) * (2 * Math.PI) / p) + origin + shift;
		}
	}

	/**
	 * Back easing function.
	 */
	public static class Back {
		/**
		 * Back easing function taking zero as the origin.
		 *
		 * @param timer The dedicated {@link Timer}.
		 * @param shift The distance to shift the value.
		 * @return The eased back value.
		 */
		public static double ease(@NotNull Timer timer, double shift) {
			return ease(timer.queue(), 0, shift, timer.getLasting());
		}

		/**
		 * Back easing function in taking zero as the origin.
		 *
		 * @param timer The dedicated {@link Timer}.
		 * @param shift The distance to shift the value.
		 * @return The eased back value.
		 */
		public static double easeIn(@NotNull Timer timer, double shift) {
			return easeIn(timer.queue(), 0, shift, timer.getLasting());
		}

		/**
		 * Back easing function out taking zero as the origin.
		 *
		 * @param timer The dedicated {@link Timer}.
		 * @param shift The distance to shift the value.
		 * @return The eased back value.
		 */
		public static double easeOut(@NotNull Timer timer, double shift) {
			return easeOut(timer.queue(), 0, shift, timer.getLasting());
		}

		/**
		 * Easing back function.
		 *
		 * @param progress Current progress.
		 * @param origin   The original value.
		 * @param shift    The distance to shift the value.
		 * @param duration The duration time.
		 * @return The back eased value.
		 */
		public static double ease(double progress, double origin, double shift, double duration) {
			return (progress /= (duration / 2)) < 1
						   ? shift / 2 * (pow(progress) * ((1.70158 * 1.525 + 1) * progress - 1.70158 * 1.525)) + origin
						   : shift / 2 * ((progress -= 2) * progress * ((1.70158 * 1.525 + 1) * progress + 1.70158 * 1.525) + 2) + origin;
		}

		/**
		 * Easing back function in.
		 *
		 * @param progress Current progress.
		 * @param origin   The original value.
		 * @param shift    The distance to shift the value.
		 * @param duration The duration time.
		 * @return The back in eased value.
		 */
		public static double easeIn(double progress, double origin, double shift, double duration) {
			return shift * (progress /= duration) * progress * (2.70158 * progress - 1.70158) + origin;
		}

		/**
		 * Easing back function out.
		 *
		 * @param progress Current progress.
		 * @param origin   The original value.
		 * @param shift    The distance to shift the value.
		 * @param duration The duration time.
		 * @return The back out eased value.
		 */
		public static double easeOut(double progress, double origin, double shift, double duration) {
			return shift * (progress = progress / duration - 1) * progress * (2.70158 * progress + 1.70158) + 1 + origin;
		}
	}

	/**
	 * Bounce easing function.
	 */
	public static class Bounce {
		/**
		 * Bounce easing function taking zero as the origin.
		 *
		 * @param timer The dedicated {@link Timer}.
		 * @param shift The distance to shift the value.
		 * @return The eased bounce value.
		 */
		public static double ease(@NotNull Timer timer, double shift) {
			return ease(timer.queue(), 0, shift, timer.getLasting());
		}

		/**
		 * Bounce easing function in taking zero as the origin.
		 *
		 * @param timer The dedicated {@link Timer}.
		 * @param shift The distance to shift the value.
		 * @return The eased bounce value.
		 */
		public static double easeIn(@NotNull Timer timer, double shift) {
			return easeIn(timer.queue(), 0, shift, timer.getLasting());
		}

		/**
		 * Bounce easing function out taking zero as the origin.
		 *
		 * @param timer The dedicated {@link Timer}.
		 * @param shift The distance to shift the value.
		 * @return The eased bounce value.
		 */
		public static double easeOut(@NotNull Timer timer, double shift) {
			return easeOut(timer.queue(), 0, shift, timer.getLasting());
		}

		/**
		 * Easing bounce function.
		 *
		 * @param progress Current progress.
		 * @param origin   The original value.
		 * @param shift    The distance to shift the value.
		 * @param duration The duration time.
		 * @return The bounce eased value.
		 */
		public static double ease(double progress, double origin, double shift, double duration) {
			return (progress < duration / 2)
						   ? easeIn(progress * 2, 0, shift, duration) * 0.5 + origin
						   : easeOut(progress * 2 - duration, 0, shift, duration) * 0.5 + origin + shift * 0.5;
		}

		/**
		 * Easing bounce function in.
		 *
		 * @param progress Current progress.
		 * @param origin   The original value.
		 * @param shift    The distance to shift the value.
		 * @param duration The duration time.
		 * @return The bounce in eased value.
		 */
		public static double easeIn(double progress, double origin, double shift, double duration) {
			return shift - easeOut(duration - progress, 0, shift, duration) + origin;
		}

		/**
		 * Easing bounce function out.
		 *
		 * @param progress Current progress.
		 * @param origin   The original value.
		 * @param shift    The distance to shift the value.
		 * @param duration The duration time.
		 * @return The bounce out eased value.
		 */
		public static double easeOut(double progress, double origin, double shift, double duration) {
			if ((progress /= duration) <= (1 / 2.75)) {
				return shift * 7.5625 * pow(progress) + origin;
			} else if (progress < (2 / 2.75)) {
				return shift * (7.5625 * (progress -= (1.5 / 2.75)) * progress + 0.75) + origin;
			} else if (progress < (2.5 / 2.75)) {
				return shift * (7.5625 * (progress -= (2.25 / 2.75)) * progress + 0.9375) + origin;
			} else {
				return shift * (7.5625 * (progress -= (2.625 / 2.75)) * progress + 0.984375) + origin;
			}
		}
	}
}
