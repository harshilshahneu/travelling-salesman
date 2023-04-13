package edu.northeastern.info6205.tspsolver.jgrapht.graph.specifics;

import edu.northeastern.info6205.tspsolver.jgrapht.Graph;
import edu.northeastern.info6205.tspsolver.jgrapht.alg.util.Pair;
import edu.northeastern.info6205.tspsolver.jgrapht.graph.EdgeSetFactory;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;

public class FastLookupDirectedSpecifics<V, E> extends DirectedSpecifics<V, E> {
	private static final long serialVersionUID = 1L;
	
	protected Map<Pair<V, V>, Set<E>> touchingVerticesToEdgeMap;

	public FastLookupDirectedSpecifics(Graph<V, E> graph, Map<V, DirectedEdgeContainer<V, E>> vertexMap,
									   Map<Pair<V, V>, Set<E>> touchingVerticesToEdgeMap, EdgeSetFactory<V, E> edgeSetFactory) {
		super(graph, vertexMap, edgeSetFactory);
		this.touchingVerticesToEdgeMap = Objects.requireNonNull(touchingVerticesToEdgeMap);
	}

	@Override
	public Set<E> getAllEdges(V sourceVertex, V targetVertex) {
		if (graph.containsVertex(sourceVertex) && graph.containsVertex(targetVertex)) {
			Set<E> edges = touchingVerticesToEdgeMap.get(new Pair<>(sourceVertex, targetVertex));
			if (edges == null) {
				return Collections.emptySet();
			} else {
				Set<E> edgeSet = edgeSetFactory.createEdgeSet(sourceVertex);
				edgeSet.addAll(edges);
				return edgeSet;
			}
		} else {
			return null;
		}
	}

	@Override
	public E getEdge(V sourceVertex, V targetVertex) {
		Set<E> edges = touchingVerticesToEdgeMap.get(new Pair<>(sourceVertex, targetVertex));
		if (edges == null || edges.isEmpty())
			return null;
		else {
			return edges.iterator().next();
		}
	}

	@Override
	public boolean addEdgeToTouchingVertices(V sourceVertex, V targetVertex, E e) {
		if (!super.addEdgeToTouchingVertices(sourceVertex, targetVertex, e)) {
			return false;
		}
		addToIndex(sourceVertex, targetVertex, e);
		return true;
	}

	@Override
	public boolean addEdgeToTouchingVerticesIfAbsent(V sourceVertex, V targetVertex, E e) {
		// first lookup using our own index
		E edge = getEdge(sourceVertex, targetVertex);
		if (edge != null) {
			return false;
		}

		return addEdgeToTouchingVertices(sourceVertex, targetVertex, e);
	}

	@Override
	public E createEdgeToTouchingVerticesIfAbsent(V sourceVertex, V targetVertex, Supplier<E> edgeSupplier) {
		// first lookup using our own index
		E edge = getEdge(sourceVertex, targetVertex);
		if (edge != null) {
			return null;
		}

		E e = edgeSupplier.get();
		addEdgeToTouchingVertices(sourceVertex, targetVertex, e);
		return e;
	}

	@Override
	public void removeEdgeFromTouchingVertices(V sourceVertex, V targetVertex, E e) {
		super.removeEdgeFromTouchingVertices(sourceVertex, targetVertex, e);

		removeFromIndex(sourceVertex, targetVertex, e);
	}

	protected void addToIndex(V sourceVertex, V targetVertex, E e) {
		Pair<V, V> vertexPair = new Pair<>(sourceVertex, targetVertex);
		Set<E> edgeSet = touchingVerticesToEdgeMap.get(vertexPair);
		if (edgeSet != null)
			edgeSet.add(e);
		else {
			edgeSet = edgeSetFactory.createEdgeSet(sourceVertex);
			edgeSet.add(e);
			touchingVerticesToEdgeMap.put(vertexPair, edgeSet);
		}
	}

	protected void removeFromIndex(V sourceVertex, V targetVertex, E e) {
		Pair<V, V> vertexPair = new Pair<>(sourceVertex, targetVertex);
		Set<E> edgeSet = touchingVerticesToEdgeMap.get(vertexPair);
		if (edgeSet != null) {
			edgeSet.remove(e);
			if (edgeSet.isEmpty()) {
				touchingVerticesToEdgeMap.remove(vertexPair);
			}
		}
	}

}
