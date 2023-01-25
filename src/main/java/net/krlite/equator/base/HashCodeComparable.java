package net.krlite.equator.base;

import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * A class to compare objects by their field hash code.
 */
public abstract class HashCodeComparable {
	/**
	 * Compares this object to the specified object.
	 *
	 * @param o	the nullable object to compare this object
	 *          against with.
	 * @return	{@code true} if the specified object has
	 * 			the same field hash code as this object.
	 * 			Otherwise {@code false}.
	 */
	@Override
	public boolean equals(@Nullable Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		HashCodeComparable that = (HashCodeComparable) o;
		return hashCode() == that.hashCode();
	}

	/**
	 * Returns the hash code of this object, based on the fields.
	 *
	 * @return	the hash code of this object.
	 */
	@Override
	public int hashCode() {
		return Objects.hash((Object) getClass().getDeclaredFields());
	}
}
