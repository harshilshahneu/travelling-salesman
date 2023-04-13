package edu.northeastern.info6205.tspsolver.jgrapht.util;

import java.util.*;

public class CollectionUtil {
	private CollectionUtil() {
	}

	public static <K, V> HashMap<K, V> newHashMapWithExpectedSize(int expectedSize) {
		return new HashMap<>(capacityForSize(expectedSize));
	}

	public static <K, V> LinkedHashMap<K, V> newLinkedHashMapWithExpectedSize(int expectedSize) {
		return new LinkedHashMap<>(capacityForSize(expectedSize));
	}

	public static <E> HashSet<E> newHashSetWithExpectedSize(int expectedSize) {
		return new HashSet<>(capacityForSize(expectedSize));
	}

	public static <E> LinkedHashSet<E> newLinkedHashSetWithExpectedSize(int expectedSize) {
		return new LinkedHashSet<>(capacityForSize(expectedSize));
	}

	private static int capacityForSize(int size) { // consider default load factor 0.75f of (Linked)HashMap
		return (int) (size / 0.75f + 1.0f); // let (Linked)HashMap limit it if it's too large
	}

	public static <E> E getElement(Iterable<E> iterable, int index) {
		if (iterable instanceof List) {
			return ((List<E>) iterable).get(index);
		}
		Iterator<E> it = iterable.iterator();
		for (int i = 0; i < index && it.hasNext(); i++) {
			it.next();
		}
		if (it.hasNext()) {
			return it.next();
		} else {
			throw new IndexOutOfBoundsException(String.valueOf(index));
		}
	}
}
