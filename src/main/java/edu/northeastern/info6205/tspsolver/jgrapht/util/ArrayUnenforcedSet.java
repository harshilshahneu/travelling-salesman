package edu.northeastern.info6205.tspsolver.jgrapht.util;

import java.util.*;

public class ArrayUnenforcedSet<E> extends ArrayList<E> implements Set<E> {
	private static final long serialVersionUID = 1L;

	public ArrayUnenforcedSet() {
		super();
	}

	public ArrayUnenforcedSet(Collection<? extends E> c) {
		super(c);
	}

	public ArrayUnenforcedSet(int n) {
		super(n);
	}

	@Override
	public boolean equals(Object o) {
		return new SetForEquality().equals(o);
	}

	@Override
	public int hashCode() {
		return new SetForEquality().hashCode();
	}

	private class SetForEquality extends AbstractSet<E> {
		@Override
		public Iterator<E> iterator() {
			return ArrayUnenforcedSet.this.iterator();
		}

		@Override
		public int size() {
			return ArrayUnenforcedSet.this.size();
		}
	}
}
