package edu.northeastern.info6205.tspsolver.jgrapht;

public interface GraphMapping<V, E> {
	
	V getVertexCorrespondence(V vertex, boolean forward);

	E getEdgeCorrespondence(E edge, boolean forward);
	
}
