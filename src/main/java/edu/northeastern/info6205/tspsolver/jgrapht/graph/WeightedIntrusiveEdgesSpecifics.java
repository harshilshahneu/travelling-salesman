package edu.northeastern.info6205.tspsolver.jgrapht.graph;

import java.util.Map;

public class WeightedIntrusiveEdgesSpecifics<V, E> extends BaseIntrusiveEdgesSpecifics<V, E, IntrusiveWeightedEdge>
		implements IntrusiveEdgesSpecifics<V, E> {
	private static final long serialVersionUID = 1L;

	public WeightedIntrusiveEdgesSpecifics(Map<E, IntrusiveWeightedEdge> map) {
		super(map);
	}

	@Override
	public boolean add(E e, V sourceVertex, V targetVertex) {
		if (e instanceof IntrusiveWeightedEdge) {
			return addIntrusiveEdge(e, sourceVertex, targetVertex, (IntrusiveWeightedEdge) e);

		} else {
			int previousSize = edgeMap.size();
			IntrusiveWeightedEdge intrusiveEdge = edgeMap.computeIfAbsent(e, i -> new IntrusiveWeightedEdge());
			if (previousSize < edgeMap.size()) { // edge was added
				intrusiveEdge.source = sourceVertex;
				intrusiveEdge.target = targetVertex;
				return true;
			}
			return false;
		}
	}

	@Override
	public double getEdgeWeight(E e) {
		IntrusiveWeightedEdge ie = getIntrusiveEdge(e);
		if (ie == null) {
			throw new IllegalArgumentException("no such edge in graph: " + e.toString());
		}
		return ie.weight;
	}

	@Override
	public void setEdgeWeight(E e, double weight) {
		IntrusiveWeightedEdge ie = getIntrusiveEdge(e);
		if (ie == null) {
			throw new IllegalArgumentException("no such edge in graph: " + e.toString());
		}
		ie.weight = weight;
	}

	@Override
	protected IntrusiveWeightedEdge getIntrusiveEdge(E e) {
		if (e instanceof IntrusiveWeightedEdge) {
			return (IntrusiveWeightedEdge) e;
		}
		return edgeMap.get(e);
	}
}
