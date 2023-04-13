package edu.northeastern.info6205.tspsolver.jgrapht.graph;

import java.util.Map;

public class UniformIntrusiveEdgesSpecifics<V, E> extends BaseIntrusiveEdgesSpecifics<V, E, IntrusiveEdge>
		implements IntrusiveEdgesSpecifics<V, E> {
	private static final long serialVersionUID = 1L;

	public UniformIntrusiveEdgesSpecifics(Map<E, IntrusiveEdge> map) {
		super(map);
	}

	@Override
	public boolean add(E e, V sourceVertex, V targetVertex) {
		if (e instanceof IntrusiveEdge) {
			return addIntrusiveEdge(e, sourceVertex, targetVertex, (IntrusiveEdge) e);

		} else {
			int previousSize = edgeMap.size();
			IntrusiveEdge intrusiveEdge = edgeMap.computeIfAbsent(e, i -> new IntrusiveEdge());
			if (previousSize < edgeMap.size()) { // edge was added
				intrusiveEdge.source = sourceVertex;
				intrusiveEdge.target = targetVertex;
				return true;
			}
			return false;
		}
	}

	@Override
	protected IntrusiveEdge getIntrusiveEdge(E e) {
		if (e instanceof IntrusiveEdge) {
			return (IntrusiveEdge) e;
		}
		return edgeMap.get(e);
	}
}
