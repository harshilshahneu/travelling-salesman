package edu.northeastern.info6205.tspsolver.jgrapht.graph;

import java.util.Set;

public interface EdgeSetFactory<V, E> {
	
	Set<E> createEdgeSet(V vertex);
	
}
