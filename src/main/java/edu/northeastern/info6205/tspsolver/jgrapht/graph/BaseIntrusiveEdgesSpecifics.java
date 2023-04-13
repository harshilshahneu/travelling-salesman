package edu.northeastern.info6205.tspsolver.jgrapht.graph;

import edu.northeastern.info6205.tspsolver.jgrapht.Graph;
import edu.northeastern.info6205.tspsolver.jgrapht.util.TypeUtil;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public abstract class BaseIntrusiveEdgesSpecifics<V, E, IE extends IntrusiveEdge> implements Serializable {
	private static final long serialVersionUID = 1L;
	
	protected Map<E, IE> edgeMap;
	protected transient Set<E> unmodifiableEdgeSet = null;

	public BaseIntrusiveEdgesSpecifics(Map<E, IE> edgeMap) {
		this.edgeMap = Objects.requireNonNull(edgeMap);
	}

	public boolean containsEdge(E e) {
		return edgeMap.containsKey(e);
	}

	public Set<E> getEdgeSet() {
		if (unmodifiableEdgeSet == null) {
			unmodifiableEdgeSet = Collections.unmodifiableSet(edgeMap.keySet());
		}
		return unmodifiableEdgeSet;
	}

	public void remove(E e) {
		edgeMap.remove(e);
	}

	public V getEdgeSource(E e) {
		IntrusiveEdge ie = getIntrusiveEdge(e);
		if (ie == null) {
			throw new IllegalArgumentException("no such edge in graph: " + e.toString());
		}
		return TypeUtil.uncheckedCast(ie.source);
	}

	public V getEdgeTarget(E e) {
		IntrusiveEdge ie = getIntrusiveEdge(e);
		if (ie == null) {
			throw new IllegalArgumentException("no such edge in graph: " + e.toString());
		}
		return TypeUtil.uncheckedCast(ie.target);
	}

	public double getEdgeWeight(E e) {
		return Graph.DEFAULT_EDGE_WEIGHT;
	}

	public void setEdgeWeight(E e, double weight) {
		throw new UnsupportedOperationException();
	}

	public abstract boolean add(E e, V sourceVertex, V targetVertex);

	protected boolean addIntrusiveEdge(E edge, V sourceVertex, V targetVertex, IE e) {
		if (e.source == null && e.target == null) { // edge not yet in any graph
			e.source = sourceVertex;
			e.target = targetVertex;

		} else if (e.source != sourceVertex || e.target != targetVertex) {
			// Edge already contained in this or another graph but with different touching
			// edges. Reject the edge to not reset the touching vertices of the edge.
			// Changing the touching vertices causes major inconsistent behavior.
			throw new IntrusiveEdgeException(e.source, e.target);
		}
		return edgeMap.putIfAbsent(edge, e) == null;
	}

	protected abstract IE getIntrusiveEdge(E e);
}
