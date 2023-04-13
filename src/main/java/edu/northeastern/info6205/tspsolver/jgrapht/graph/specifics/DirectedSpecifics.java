package edu.northeastern.info6205.tspsolver.jgrapht.graph.specifics;

import edu.northeastern.info6205.tspsolver.jgrapht.Graph;
import edu.northeastern.info6205.tspsolver.jgrapht.graph.EdgeSetFactory;
import edu.northeastern.info6205.tspsolver.jgrapht.util.ArrayUnenforcedSet;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;

public class DirectedSpecifics<V, E> implements Specifics<V, E>, Serializable {
	private static final long serialVersionUID = 1L;
	
	protected Graph<V, E> graph;
	protected Map<V, DirectedEdgeContainer<V, E>> vertexMap;
	protected EdgeSetFactory<V, E> edgeSetFactory;

	public DirectedSpecifics(Graph<V, E> graph, Map<V, DirectedEdgeContainer<V, E>> vertexMap,
			EdgeSetFactory<V, E> edgeSetFactory) {
		this.graph = Objects.requireNonNull(graph);
		this.vertexMap = Objects.requireNonNull(vertexMap);
		this.edgeSetFactory = Objects.requireNonNull(edgeSetFactory);
	}

	@Override
	public boolean addVertex(V v) {
		DirectedEdgeContainer<V, E> ec = vertexMap.get(v);
		if (ec == null) {
			vertexMap.put(v, new DirectedEdgeContainer<>(edgeSetFactory, v));
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

			DirectedEdgeContainer<V, E> ec = getEdgeContainer(sourceVertex);

			for (E e : ec.outgoing) {
				if (graph.getEdgeTarget(e).equals(targetVertex)) {
					edges.add(e);
				}
			}
		}

		return edges;
	}

	@Override
	public E getEdge(V sourceVertex, V targetVertex) {
		if (graph.containsVertex(sourceVertex) && graph.containsVertex(targetVertex)) {
			DirectedEdgeContainer<V, E> ec = getEdgeContainer(sourceVertex);

			for (E e : ec.outgoing) {
				if (graph.getEdgeTarget(e).equals(targetVertex)) {
					return e;
				}
			}
		}

		return null;
	}

	@Override
	public boolean addEdgeToTouchingVertices(V sourceVertex, V targetVertex, E e) {
		getEdgeContainer(sourceVertex).addOutgoingEdge(e);
		getEdgeContainer(targetVertex).addIncomingEdge(e);
		return true;
	}

	@Override
	public boolean addEdgeToTouchingVerticesIfAbsent(V sourceVertex, V targetVertex, E e) {
		// lookup for edge with same source and target
		DirectedEdgeContainer<V, E> ec = getEdgeContainer(sourceVertex);
		for (E outEdge : ec.outgoing) {
			if (graph.getEdgeTarget(outEdge).equals(targetVertex)) {
				return false;
			}
		}

		// add
		ec.addOutgoingEdge(e);
		getEdgeContainer(targetVertex).addIncomingEdge(e);

		return true;
	}

	@Override
	public E createEdgeToTouchingVerticesIfAbsent(V sourceVertex, V targetVertex, Supplier<E> edgeSupplier) {
		// lookup for edge with same source and target
		DirectedEdgeContainer<V, E> ec = getEdgeContainer(sourceVertex);
		for (E e : ec.outgoing) {
			if (graph.getEdgeTarget(e).equals(targetVertex)) {
				return null;
			}
		}

		// create and add
		E e = edgeSupplier.get();
		ec.addOutgoingEdge(e);
		getEdgeContainer(targetVertex).addIncomingEdge(e);

		return e;
	}

	@Override
	public int degreeOf(V vertex) {
		return inDegreeOf(vertex) + outDegreeOf(vertex);
	}

	@Override
	public Set<E> edgesOf(V vertex) {
		ArrayUnenforcedSet<E> inAndOut = new ArrayUnenforcedSet<>(getEdgeContainer(vertex).incoming);

		if (graph.getType().isAllowingSelfLoops()) {
			for (E e : getEdgeContainer(vertex).outgoing) {
				V target = graph.getEdgeTarget(e);
				if (!vertex.equals(target)) {
					inAndOut.add(e);
				}
			}
		} else {
			inAndOut.addAll(getEdgeContainer(vertex).outgoing);
		}

		return Collections.unmodifiableSet(inAndOut);
	}

	@Override
	public int inDegreeOf(V vertex) {
		return getEdgeContainer(vertex).incoming.size();
	}

	@Override
	public Set<E> incomingEdgesOf(V vertex) {
		return getEdgeContainer(vertex).getUnmodifiableIncomingEdges();
	}

	@Override
	public int outDegreeOf(V vertex) {
		return getEdgeContainer(vertex).outgoing.size();
	}

	@Override
	public Set<E> outgoingEdgesOf(V vertex) {
		return getEdgeContainer(vertex).getUnmodifiableOutgoingEdges();
	}

	@Override
	public void removeEdgeFromTouchingVertices(V sourceVertex, V targetVertex, E e) {
		getEdgeContainer(sourceVertex).removeOutgoingEdge(e);
		getEdgeContainer(targetVertex).removeIncomingEdge(e);
	}

	protected DirectedEdgeContainer<V, E> getEdgeContainer(V vertex) {
		DirectedEdgeContainer<V, E> ec = vertexMap.get(vertex);

		if (ec == null) {
			ec = new DirectedEdgeContainer<>(edgeSetFactory, vertex);
			vertexMap.put(vertex, ec);
		}

		return ec;
	}

}
