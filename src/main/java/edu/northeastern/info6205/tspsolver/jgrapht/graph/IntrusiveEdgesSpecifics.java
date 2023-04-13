package edu.northeastern.info6205.tspsolver.jgrapht.graph;

import java.io.Serializable;
import java.util.Set;

public interface IntrusiveEdgesSpecifics<V, E> extends Serializable {

	V getEdgeSource(E e);

	V getEdgeTarget(E e);

	boolean add(E e, V sourceVertex, V targetVertex);

	boolean containsEdge(E e);

	Set<E> getEdgeSet();

	void remove(E e);

	double getEdgeWeight(E e);

	void setEdgeWeight(E e, double weight);
}
