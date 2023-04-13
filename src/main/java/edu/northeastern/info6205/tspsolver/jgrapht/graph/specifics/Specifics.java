package edu.northeastern.info6205.tspsolver.jgrapht.graph.specifics;

import java.util.Set;
import java.util.function.Supplier;

public interface Specifics<V, E> {

	boolean addVertex(V vertex);

	Set<V> getVertexSet();

	Set<E> getAllEdges(V sourceVertex, V targetVertex);

	E getEdge(V sourceVertex, V targetVertex);

	boolean addEdgeToTouchingVertices(V sourceVertex, V targetVertex, E e);

	boolean addEdgeToTouchingVerticesIfAbsent(V sourceVertex, V targetVertex, E e);

	E createEdgeToTouchingVerticesIfAbsent(V sourceVertex, V targetVertex, Supplier<E> edgeSupplier);

	int degreeOf(V vertex);

	Set<E> edgesOf(V vertex);

	int inDegreeOf(V vertex);

	Set<E> incomingEdgesOf(V vertex);

	int outDegreeOf(V vertex);

	Set<E> outgoingEdgesOf(V vertex);

	void removeEdgeFromTouchingVertices(V sourceVertex, V targetVertex, E e);

}
