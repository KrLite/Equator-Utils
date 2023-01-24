package net.krlite.equator.debug;

import net.krlite.equator.math.EasingFunctions;
import net.krlite.equator.util.Timer;

import java.text.DecimalFormat;

@SuppressWarnings("allJavadoc")
public class EquatorUtilsDebugger {
	public static void main(String[] args) {
		Timer t = new Timer(100000);
		EasingFunctions.Combined c = new EasingFunctions.Combined().append(EasingFunctions.Quadratic::easeOut, 5).appendNegate(EasingFunctions.Bounce::easeOut, 15);
		while (true) {
			t.run(t::reset);
			System.out.println(new DecimalFormat("#.###").format(c.apply(t)));
		}
	}
}
