package net.krlite.equator.debug;

import net.krlite.equator.math.EasingFunctions;
import net.krlite.equator.util.Timer;

@SuppressWarnings("allJavadoc")
public class EquatorUtilsDebugger {
	public static void main(String[] args) {
		final Timer bounce = new Timer(2000);
		EasingFunctions.Combined bouncing = new EasingFunctions.Combined()
													.append(5, EasingFunctions.Linear::ease).append(15, EasingFunctions.Linear::ease);
		while (true) {
			bounce.run(bounce::reset);
			System.out.println(bouncing.apply(bounce));
		}
	}
}
