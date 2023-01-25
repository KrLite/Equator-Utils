package net.krlite.equator.core;

/**
 * An interface to define a short string representation of an object.
 */
public interface ShortStringable extends FieldFormattable {
	/**
	 * Returns a short string representation of the object,
	 * including the field values and excluding the class name.
	 *
	 * @return	The short string representation of the object.
	 */
	default String toShortString() {
		return "{" + formatFields(false) + "}";
	}
}
