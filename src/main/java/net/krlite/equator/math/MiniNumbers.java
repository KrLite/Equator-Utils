package net.krlite.equator.math;

public class MiniNumbers {
	private static <T extends Enum<T> & MiniNumber> String toMiniNumber(String doubleNumber, boolean alwaysWithSign, T[] numbers, T plus, T minus, T dot) {
		String[] parts = doubleNumber.split("\\.");
		String integerPart = parts[0];
		String decimalPart = parts.length > 1 ? parts[1] : null;
		StringBuilder result = new StringBuilder();
		if (integerPart.startsWith("-")) {
			result.append(minus.getValue());
			integerPart = integerPart.substring(1);
		} else if (alwaysWithSign) result.append(plus.getValue());
		for (int i = 0; i < integerPart.length(); i++)
			result.append(numbers[Integer.parseInt(integerPart.substring(i, i + 1))].getValue());
		if (decimalPart != null) {
			result.append(dot.getValue());
			for (int i = 0; i < decimalPart.length(); i++)
				result.append(numbers[Integer.parseInt(decimalPart.substring(i, i + 1))].getValue());
		}
		return result.toString();
	}

	protected interface MiniNumber {
		String getValue();

		String toMiniNumber(double number, boolean alwaysWithSign);

		default String toMiniNumber(double number) {
			return toMiniNumber(number, false);
		}
	}

	public enum Sup implements MiniNumber {
		ZERO("⁰"),
		ONE("¹"),
		TWO("²"),
		THREE("³"),
		FOUR("⁴"),
		FIVE("⁵"),
		SIX("⁶"),
		SEVEN("⁷"),
		EIGHT("⁸"),
		NINE("⁹"),
		PLUS("⁺"),
		MINUS("⁻"),
		EQUALS("⁼"),
		PAREN_OPEN("⁽"),
		PAREN_CLOSE("⁾"),
		N("ⁿ"),
		I("ⁱ"),
		DOT("·");

		private final String value;

		Sup(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}

		public static final Sup[] NUMBERS = {
			ZERO, ONE, TWO, THREE, FOUR,
			FIVE, SIX, SEVEN, EIGHT, NINE
		};

		@Override
		public String toMiniNumber(double number, boolean alwaysWithSign) {
			return MiniNumbers.toMiniNumber(Double.toString(number), alwaysWithSign, NUMBERS, PLUS, MINUS, DOT);
		}
	}

	public enum Sub implements MiniNumber {
		ZERO("₀"),
		ONE("₁"),
		TWO("₂"),
		THREE("₃"),
		FOUR("₄"),
		FIVE("₅"),
		SIX("₆"),
		SEVEN("₇"),
		EIGHT("₈"),
		NINE("₉"),
		PLUS("₊"),
		MINUS("₋"),
		EQUALS("₌"),
		PAREN_OPEN("₍"),
		PAREN_CLOSE("₎"),
		N("ₙ"),
		I("ᵢ"),
		DOT(".");

		private final String value;

		Sub(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}

		public static final Sub[] NUMBERS = {
			ZERO, ONE, TWO, THREE, FOUR,
			FIVE, SIX, SEVEN, EIGHT, NINE
		};

		@Override
		public String toMiniNumber(double number, boolean alwaysWithSign) {
			return MiniNumbers.toMiniNumber(Double.toString(number), alwaysWithSign, NUMBERS, PLUS, MINUS, DOT);
		}
	}
}
