package edu.northeastern.info6205.tspsolver.jgrapht;

import edu.northeastern.info6205.tspsolver.jgrapht.util.LiveIterableWrapper;

public interface GraphIterables<V, E> {
	
	Graph<V, E> getGraph();

	default Iterable<E> edges() {
		return new LiveIterableWrapper<>(() -> getGraph().edgeSet());
	}

	default long edgeCount() {
		return getGraph().edgeSet().size();
	}

	default Iterable<V> vertices() {
		return new LiveIterableWrapper<>(() -> getGraph().vertexSet());
	}

	default long vertexCount() {
		return getGraph().vertexSet().size();
	}

	default Iterable<E> edgesOf(V vertex) {
		return new LiveIterableWrapper<>(() -> getGraph().edgesOf(vertex));
	}

	default long degreeOf(V vertex) {
		return getGraph().degreeOf(vertex);
	}

	default Iterable<E> incomingEdgesOf(V vertex) {
		return new LiveIterableWrapper<>(() -> getGraph().incomingEdgesOf(vertex));
	}

	default long inDegreeOf(V vertex) {
		return getGraph().inDegreeOf(vertex);
	}

	default Iterable<E> outgoingEdgesOf(V vertex) {
		return new LiveIterableWrapper<>(() -> getGraph().outgoingEdgesOf(vertex));
	}

	default Iterable<E> allEdges(V sourceVertex, V targetVertex) {
		return new LiveIterableWrapper<>(() -> getGraph().getAllEdges(sourceVertex, targetVertex));
	}

}
