package edu.northeastern.info6205.tspsolver.jgrapht.graph.specifics;

import edu.northeastern.info6205.tspsolver.jgrapht.Graph;
import edu.northeastern.info6205.tspsolver.jgrapht.graph.EdgeSetFactory;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;

public class UndirectedSpecifics<V, E> implements Specifics<V, E>, Serializable {
	private static final long serialVersionUID = 1L;
	
	protected Graph<V, E> graph;
	protected Map<V, org.jgrapht.graph.specifics.UndirectedEdgeContainer<V, E>> vertexMap;
	protected EdgeSetFactory<V, E> edgeSetFactory;

	public UndirectedSpecifics(Graph<V, E> graph, Map<V, org.jgrapht.graph.specifics.UndirectedEdgeContainer<V, E>> vertexMap,
			EdgeSetFactory<V, E> edgeSetFactory) {
		this.graph = Objects.requireNonNull(graph);
		this.vertexMap = Objects.requireNonNull(vertexMap);
		this.edgeSetFactory = Objects.requireNonNull(edgeSetFactory);
	}

	@Override
	public boolean addVertex(V v) {
		org.jgrapht.graph.specifics.UndirectedEdgeContainer<V, E> ec = vertexMap.get(v);
		if (ec == null) {
			vertexMap.put(v, new UndirectedEdgeContainer<>(edgeSetFactory, v));
			return true;
		}
		return false;
	}

	@Override
	public Set<V> getVertexSet() {
		return vertexMap.keySet();
	}

	@Override
	public Set<E> getAllEdges(V sourceVertex, V targetVertex) {
		Set<E> edges = null;

		if (graph.containsVertex(sourceVertex) && graph.containsVertex(targetVertex)) {
			edges = new ArrayUnenforcedSet<>();

			for (E e : getEdgeContainer(sourceVertex).vertexEdges) {
				boolean equal = isEqualsStraightOrInverted(sourceVertex, targetVertex, e);

				if (equal) {
					edges.add(e);
				}
			}
		}

		return edges;
	}

	@Override
	public E getEdge(V sourceVertex, V targetVertex) {
		if (graph.containsVertex(sourceVertex) && graph.containsVertex(targetVertex)) {

			for (E e : getEdgeContainer(sourceVertex).vertexEdges) {
				boolean equal = isEqualsStraightOrInverted(sourceVertex, targetVertex, e);

				if (equal) {
					return e;
				}
			}
		}

		return null;
	}

	private boolean isEqualsStraightOrInverted(Object sourceVertex, Object targetVertex, E e) {
		boolean equalStraight = sourceVertex.equals(graph.getEdgeSource(e))
				&& targetVertex.equals(graph.getEdgeTarget(e));

		boolean equalInverted = sourceVertex.equals(graph.getEdgeTarget(e))
				&& targetVertex.equals(graph.getEdgeSource(e));
		return equalStraight || equalInverted;
	}

	@Override
	public boolean addEdgeToTouchingVertices(V sourceVertex, V targetVertex, E e) {
		getEdgeContainer(sourceVertex).addEdge(e);

		if (!sourceVertex.equals(targetVertex)) {
			getEdgeContainer(targetVertex).addEdge(e);
		}
		return true;
	}

	@Override
	public boolean addEdgeToTouchingVerticesIfAbsent(V sourceVertex, V targetVertex, E e) {
		// lookup for edge with same source and target
		org.jgrapht.graph.specifics.UndirectedEdgeContainer<V, E> ec = getEdgeContainer(sourceVertex);
		for (E edge : ec.vertexEdges) {
			if (isEqualsStraightOrInverted(sourceVertex, targetVertex, edge)) {
				return false;
			}
		}

		// add
		ec.addEdge(e);
		getEdgeContainer(targetVertex).addEdge(e);
		return true;
	}

	@Override
	public E createEdgeToTouchingVerticesIfAbsent(V sourceVertex, V targetVertex, Supplier<E> edgeSupplier) {
		// lookup for edge with same source and target
		org.jgrapht.graph.specifics.UndirectedEdgeContainer<V, E> ec = getEdgeContainer(sourceVertex);
		for (E edge : ec.vertexEdges) {
			if (isEqualsStraightOrInverted(sourceVertex, targetVertex, edge)) {
				return null;
			}
		}

		// create and add
		E e = edgeSupplier.get();
		ec.addEdge(e);
		getEdgeContainer(targetVertex).addEdge(e);

		return e;
	}

	@Override
	public int degreeOf(V vertex) {
		if (graph.getType().isAllowingSelfLoops()) {

			int degree = 0;
			Set<E> edges = getEdgeContainer(vertex).vertexEdges;

			for (E e : edges) {
				if (graph.getEdgeSource(e).equals(graph.getEdgeTarget(e))) {
					degree += 2;
				} else {
					degree += 1;
				}
			}

			return degree;
		} else {
			return getEdgeContainer(vertex).edgeCount();
		}
	}

	@Override
	public Set<E> edgesOf(V vertex) {
		return getEdgeContainer(vertex).getUnmodifiableVertexEdges();
	}

	@Override
	public int inDegreeOf(V vertex) {
		return degreeOf(vertex);
	}

	@Override
	public Set<E> incomingEdgesOf(V vertex) {
		return getEdgeContainer(vertex).getUnmodifiableVertexEdges();
	}

	@Override
	public int outDegreeOf(V vertex) {
		return degreeOf(vertex);
	}

	@Override
	public Set<E> outgoingEdgesOf(V vertex) {
		return getEdgeContainer(vertex).getUnmodifiableVertexEdges();
	}

	@Override
	public void removeEdgeFromTouchingVertices(V sourceVertex, V targetVertex, E e) {
		getEdgeContainer(sourceVertex).removeEdge(e);

		if (!sourceVertex.equals(targetVertex)) {
			getEdgeContainer(targetVertex).removeEdge(e);
		}
	}

	protected org.jgrapht.graph.specifics.UndirectedEdgeContainer<V, E> getEdgeContainer(V vertex) {
		org.jgrapht.graph.specifics.UndirectedEdgeContainer<V, E> ec = vertexMap.get(vertex);

		if (ec == null) {
			ec = new UndirectedEdgeContainer<>(edgeSetFactory, vertex);
			vertexMap.put(vertex, ec);
		}

		return ec;
	}

}
