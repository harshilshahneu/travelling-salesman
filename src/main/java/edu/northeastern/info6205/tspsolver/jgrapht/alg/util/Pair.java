package edu.northeastern.info6205.tspsolver.jgrapht.alg.util;

import java.io.Serializable;
import java.util.Objects;

public class Pair<A, B> implements Serializable {
	private static final long serialVersionUID = 1L;

	protected A first;

	protected B second;

	public Pair(A a, B b) {
		this.first = a;
		this.second = b;
	}

	public A getFirst() {
		return first;
	}

	public B getSecond() {
		return second;
	}

	public void setFirst(A f) {
		first = f;
	}

	public void setSecond(B s) {
		second = s;
	}

	@SuppressWarnings("unlikely-arg-type")
	public <E> boolean hasElement(E e) {
		if (e == null) {
			return first == null || second == null;
		} else {
			return e.equals(first) || e.equals(second);
		}
	}

	@Override
	public String toString() {
		return "(" + first + "," + second + ")";
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		else if (!(o instanceof Pair))
			return false;

		@SuppressWarnings("unchecked")
		Pair<A, B> other = (Pair<A, B>) o;
		return Objects.equals(first, other.first) && Objects.equals(second, other.second);
	}

	@Override
	public int hashCode() {
		return Objects.hash(first, second);
	}

	public static <A, B> Pair<A, B> of(A a, B b) {
		return new Pair<>(a, b);
	}

}
