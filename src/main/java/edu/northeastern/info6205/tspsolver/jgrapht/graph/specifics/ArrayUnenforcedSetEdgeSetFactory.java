package edu.northeastern.info6205.tspsolver.jgrapht.graph.specifics;

import edu.northeastern.info6205.tspsolver.jgrapht.graph.EdgeSetFactory;
import edu.northeastern.info6205.tspsolver.jgrapht.util.ArrayUnenforcedSet;

import java.io.Serializable;
import java.util.Set;

public class ArrayUnenforcedSetEdgeSetFactory<V, E> implements EdgeSetFactory<V, E>, Serializable {
	private static final long serialVersionUID = 1L;

	@Override
	public Set<E> createEdgeSet(V vertex) {
		// NOTE: use size 1 to keep memory usage under control
		// for the common case of vertices with low degree
		return new ArrayUnenforcedSet<>(1);
	}

}
