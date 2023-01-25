package net.krlite.equator.core;

import java.util.Arrays;
import java.util.Objects;

/**
 * A class that formats fields in a way which is easy to read.
 */
public interface FieldFormattable {
	/**
	 * Formats the fields of this object in a way which is easy to read.
	 *
	 * @param fieldNames	Whether to include the field names.
	 * @param excluded		The names of the fields to exclude.
	 * @return				The formatted fields as string.
	 */
	default String formatFields(boolean fieldNames, String... excluded) {
		return Arrays.stream(getClass().getDeclaredFields())
					   .peek(field -> field.setAccessible(true))
					   .filter(field -> Arrays.stream(excluded).noneMatch(e -> e.equals(field.getName())))
					   .map(field -> {
						   try {
							   return (fieldNames ? field.getName() + "=" : "") + field.get(this);
						   } catch (IllegalAccessException illegalAccessException) {
							   illegalAccessException.printStackTrace();
						   }
						   return null;
					   }).filter(Objects::nonNull)
					   .reduce((f1, f2) -> f1 + ", " + f2).orElse("");
	}

	/**
	 * Formats the fields of this object in a way which is easy to read.
	 *
	 * @param excluded	The names of the fields to exclude.
	 * @return			The formatted fields as string.
	 */
	default String formatFields(String... excluded) {
		return formatFields(true, excluded);
	}

/**
	 * Formats the fields of this object in a way which is easy to read.
 *
	 * @param fieldNames	Whether to include the field names.
	 * @return				The formatted fields as string.
	 */
	default String formatFields(boolean fieldNames) {
		return formatFields(fieldNames, "");
	}

	/**
	 * Formats all the fields of this object in a way which is easy to read.
	 *
	 * @return	The formatted fields as string.
	 */
	default String formatFields() {
		return formatFields(true);
	}
}
