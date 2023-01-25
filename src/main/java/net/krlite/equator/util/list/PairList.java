package net.krlite.equator.util.list;

import net.krlite.equator.util.pair.Pair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.stream.Collectors;

/**
 * A list of {@link Pair}s.
 *
 * @param <F>	The type of the first element of the pair.
 * @param <S>	The type of the second element of the pair.
 * @see ArrayList
 * @see Pair
 */
public class PairList<F, S> extends ArrayList<Pair<F, S>> {
	/**
	 * Adds two elements of a pair to the list.
	 *
	 * @param first		The first element.
	 * @param second	The second element.
	 * @return			Whether the pair was successfully added.
	 */
	public boolean add(F first, S second) {
		return add(new Pair<>(first, second));
	}

	/**
	 * Adds two elements of a pair to the list with a specified index.
	 *
	 * @param index		The index to add the pair at.
	 * @param first		The first element.
	 * @param second	The second element.
	 */
	public void add(int index, F first, S second) {
		add(index, new Pair<>(first, second));
	}

	/**
	 * Appends a {@link PairList}.
	 *
	 * @param list	The {@link PairList} to append.
	 */
	public void addAll(PairList<F, S> list) {
		for (Pair<F, S> pair : list) {
			add(pair);
		}
	}

	/**
	 * Gets a collection of the first elements of the pairs.
	 *
	 * @return	A collection of the first elements of the pairs.
	 */
	public Collection<F> getFirsts() {
		return this.stream().map(Pair::getFirst).collect(Collectors.toList());
	}

	/**
	 * Gets a collection of the second elements of the pairs.
	 *
	 * @return	A collection of the second elements of the pairs.
	 */
	public Collection<S> getSeconds() {
		return this.stream().map(Pair::getSecond).collect(Collectors.toList());
	}

	/**
	 * Swaps all the pairs in the list.
	 *
	 * @return	The list with all the pairs swapped.
	 * @see		Pair#swap()
	 */
	public PairList<S, F> swap() {
		return this.stream().map(Pair::swap).collect(Collectors.toCollection(PairList::new));
	}

	/**
	 * Gets the {@link ListIterator} of the first elements of the pairs.
	 *
	 * @return	The {@link ListIterator} of the first elements of the pairs.
	 */
	public ListIterator<F> firstListIterator() {
		return new ListIterator<>() {
			private final ListIterator<Pair<F, S>> iterator = PairList.this.listIterator();

			@Override
			public boolean hasNext() {
				return iterator.hasNext();
			}

			@Override
			public F next() {
				return iterator.next().getFirst();
			}

			@Override
			public boolean hasPrevious() {
				return iterator.hasPrevious();
			}

			@Override
			public F previous() {
				return iterator.previous().getFirst();
			}

			@Override
			public int nextIndex() {
				return iterator.nextIndex();
			}

			@Override
			public int previousIndex() {
				return iterator.previousIndex();
			}

			@Override
			public void remove() {
				iterator.remove();
			}

			@Override
			public void set(F e) {
				iterator.set(new Pair<>(e, iterator.next().getSecond()));
			}

			@Override
			public void add(F e) {
				iterator.add(new Pair<>(e, null));
			}
		};
	}

	/**
	 * Gets the {@link ListIterator} of the second elements of the pairs.
	 *
	 * @return	The {@link ListIterator} of the second elements of the pairs.
	 */
	public ListIterator<S> secondListIterator() {
		return new ListIterator<>() {
			private final ListIterator<Pair<F, S>> iterator = PairList.this.listIterator();

			@Override
			public boolean hasNext() {
				return iterator.hasNext();
			}

			@Override
			public S next() {
				return iterator.next().getSecond();
			}

			@Override
			public boolean hasPrevious() {
				return iterator.hasPrevious();
			}

			@Override
			public S previous() {
				return iterator.previous().getSecond();
			}

			@Override
			public int nextIndex() {
				return iterator.nextIndex();
			}

			@Override
			public int previousIndex() {
				return iterator.previousIndex();
			}

			@Override
			public void remove() {
				iterator.remove();
			}

			@Override
			public void set(S e) {
				iterator.set(new Pair<>(iterator.next().getFirst(), e));
			}

			@Override
			public void add(S e) {
				iterator.add(new Pair<>(null, e));
			}
		};
	}

	/**
	 * Gets the {@link Iterator} of the first elements of the pairs.
	 *
	 * @return	The {@link Iterator} of the first elements of the pairs.
	 */
	public Iterator<F> firstIterator() {
		return new Iterator<>() {
			private final Iterator<Pair<F, S>> iterator = PairList.this.iterator();

			@Override
			public boolean hasNext() {
				return iterator.hasNext();
			}

			@Override
			public F next() {
				return iterator.next().getFirst();
			}
		};
	}

	/**
	 * Gets the {@link Iterator} of the second elements of the pairs.
	 *
	 * @return	The {@link Iterator} of the second elements of the pairs.
	 */
	public Iterator<S> secondIterator() {
		return new Iterator<>() {
			private final Iterator<Pair<F, S>> iterator = PairList.this.iterator();

			@Override
			public boolean hasNext() {
				return iterator.hasNext();
			}

			@Override
			public S next() {
				return iterator.next().getSecond();
			}
		};
	}

	/**
	 * Checks if the list contains the element, which could be a {@link Pair} or a
	 * single element.
	 *
	 * @param o	The object to check.
	 * @return	<code>true</code> if the list contains the element.
	 * 			Otherwise <code>false</code>.
	 */
	@Override
	public boolean contains(Object o) {
		return super.contains(o) || getFirsts().contains(o) || getSeconds().contains(o);
	}

	/**
	 * Checks if the list contains the first element of the pair.
	 *
	 * @param first	The first element of the pair to check.
	 * @return		<code>true</code> if the list contains the first element.
	 * 				Otherwise <code>false</code>.
	 */
	public boolean containsFirst(F first) {
		return getFirsts().contains(first);
	}

	/**
	 * Checks if the list contains the second element of the pair.
	 *
	 * @param second	The second element of the pair to check.
	 * @return			<code>true</code> if the list contains the second element.
	 * 					Otherwise <code>false</code>.
	 */
	public boolean containsSecond(S second) {
		return getSeconds().contains(second);
	}

	/**
	 * Gets the index of a first element of the pair.
	 *
	 * @param first	The first element of the pair to get the index of.
	 * @return		The index of the first element.
	 */
	public int indexOfFirst(F first) {
		return getFirsts().stream().toList().indexOf(first);
	}

	/**
	 * Gets the index of a second element of the pair.
	 *
	 * @param second	The second element of the pair to get the index of.
	 * @return			The index of the second element.
	 */
	public int indexOfSecond(S second) {
		return getSeconds().stream().toList().indexOf(second);
	}

	/**
	 * Gets the last index of a first element of the pair.
	 *
	 * @param first	The first element of the pair to get the last index of.
	 * @return		The index of the pair.
	 */
	public int lastIndexOfFirst(F first) {
		return getFirsts().stream().toList().lastIndexOf(first);
	}

	/**
	 * Gets the last index of a second element of the pair.
	 *
	 * @param second	The second element of the pair to get the last index of.
	 * @return			The index of the pair.
	 */
	public int lastIndexOfSecond(S second) {
		return getSeconds().stream().toList().lastIndexOf(second);
	}

	/**
	 * Gets a list of the second elements inserted after the first element of
	 * each pair.
	 * @return	A list of the second elements inserted after the first element of
	 * 			each pair.
	 */
	public ArrayList<?> insert() {
		return this.stream().collect(ArrayList::new, (list, pair) -> {
			list.add(pair.getFirst());
			list.add(pair.getSecond());
		}, ArrayList::addAll);
	}
}
