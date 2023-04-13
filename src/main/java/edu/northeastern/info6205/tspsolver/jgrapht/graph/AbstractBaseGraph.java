package edu.northeastern.info6205.tspsolver.jgrapht.graph;

import edu.northeastern.info6205.tspsolver.jgrapht.Graph;
import edu.northeastern.info6205.tspsolver.jgrapht.GraphIterables;
import edu.northeastern.info6205.tspsolver.jgrapht.GraphType;
import edu.northeastern.info6205.tspsolver.jgrapht.Graphs;
import edu.northeastern.info6205.tspsolver.jgrapht.graph.specifics.Specifics;
import edu.northeastern.info6205.tspsolver.jgrapht.util.TypeUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;

public abstract class AbstractBaseGraph<V, E> extends AbstractGraph<V, E>
		implements Graph<V, E>, Cloneable, Serializable {
	private static final long serialVersionUID = 1L;

	private static final String LOOPS_NOT_ALLOWED = "loops not allowed";
	private static final String GRAPH_SPECIFICS_MUST_NOT_BE_NULL = "Graph specifics must not be null";
	private static final String INVALID_VERTEX_SUPPLIER_DOES_NOT_RETURN_UNIQUE_VERTICES_ON_EACH_CALL = "Invalid vertex supplier (does not return unique vertices on each call).";
	private static final String MIXED_GRAPH_NOT_SUPPORTED = "Mixed graph not supported";
	private static final String GRAPH_SPECIFICS_STRATEGY_REQUIRED = "Graph specifics strategy required";
	private static final String THE_GRAPH_CONTAINS_NO_VERTEX_SUPPLIER = "The graph contains no vertex supplier";
	private static final String THE_GRAPH_CONTAINS_NO_EDGE_SUPPLIER = "The graph contains no edge supplier";

	private transient Set<V> unmodifiableVertexSet = null;

	private Supplier<V> vertexSupplier;
	private Supplier<E> edgeSupplier;
	private GraphType type;

	private Specifics<V, E> specifics;
	private IntrusiveEdgesSpecifics<V, E> intrusiveEdgesSpecifics;
	private org.jgrapht.graph.GraphSpecificsStrategy<V, E> graphSpecificsStrategy;

	private transient GraphIterables<V, E> graphIterables = null;

	protected AbstractBaseGraph(Supplier<V> vertexSupplier, Supplier<E> edgeSupplier, GraphType type) {
		this(vertexSupplier, edgeSupplier, type, new FastLookupGraphSpecificsStrategy<>());
	}

	protected AbstractBaseGraph(Supplier<V> vertexSupplier, Supplier<E> edgeSupplier, GraphType type,
			GraphSpecificsStrategy<V, E> graphSpecificsStrategy) {
		this.vertexSupplier = vertexSupplier;
		this.edgeSupplier = edgeSupplier;
		this.type = Objects.requireNonNull(type);
		if (type.isMixed()) {
			throw new IllegalArgumentException(MIXED_GRAPH_NOT_SUPPORTED);
		}

		this.graphSpecificsStrategy = Objects.requireNonNull(graphSpecificsStrategy, GRAPH_SPECIFICS_STRATEGY_REQUIRED);
		this.specifics = Objects.requireNonNull(graphSpecificsStrategy.getSpecificsFactory().apply(this, type),
				GRAPH_SPECIFICS_MUST_NOT_BE_NULL);
		this.intrusiveEdgesSpecifics = Objects.requireNonNull(
				graphSpecificsStrategy.getIntrusiveEdgesSpecificsFactory().apply(type),
				GRAPH_SPECIFICS_MUST_NOT_BE_NULL);

	}

	@Override
	public Set<E> getAllEdges(V sourceVertex, V targetVertex) {
		return specifics.getAllEdges(sourceVertex, targetVertex);
	}

	@Override
	public Supplier<E> getEdgeSupplier() {
		return edgeSupplier;
	}

	public void setEdgeSupplier(Supplier<E> edgeSupplier) {
		this.edgeSupplier = edgeSupplier;
	}

	@Override
	public Supplier<V> getVertexSupplier() {
		return vertexSupplier;
	}

	public void setVertexSupplier(Supplier<V> vertexSupplier) {
		this.vertexSupplier = vertexSupplier;
	}

	@Override
	public E getEdge(V sourceVertex, V targetVertex) {
		return specifics.getEdge(sourceVertex, targetVertex);
	}

	@Override
	public E addEdge(V sourceVertex, V targetVertex) {
		assertVertexExist(sourceVertex);
		assertVertexExist(targetVertex);

		if (!type.isAllowingSelfLoops() && sourceVertex.equals(targetVertex)) {
			throw new IllegalArgumentException(LOOPS_NOT_ALLOWED);
		}

		if (edgeSupplier == null) {
			throw new UnsupportedOperationException(THE_GRAPH_CONTAINS_NO_EDGE_SUPPLIER);
		}

		if (!type.isAllowingMultipleEdges()) {
			E e = specifics.createEdgeToTouchingVerticesIfAbsent(sourceVertex, targetVertex, edgeSupplier);
			if (e != null) {
				boolean edgeAdded = false;
				try {
					edgeAdded = intrusiveEdgesSpecifics.add(e, sourceVertex, targetVertex);
				} finally {
					if (!edgeAdded) {
						// edge was already present or adding threw an exception -> revert add
						specifics.removeEdgeFromTouchingVertices(sourceVertex, targetVertex, e);
					}
				}
				if (edgeAdded) {
					return e;
				}
			}
		} else {
			E e = edgeSupplier.get();
			if (intrusiveEdgesSpecifics.add(e, sourceVertex, targetVertex)) {
				specifics.addEdgeToTouchingVertices(sourceVertex, targetVertex, e);
				return e;
			}
		}
		return null;
	}

	@Override
	public boolean addEdge(V sourceVertex, V targetVertex, E e) {
		if (e == null) {
			throw new NullPointerException();
		}

		assertVertexExist(sourceVertex);
		assertVertexExist(targetVertex);

		if (!type.isAllowingSelfLoops() && sourceVertex.equals(targetVertex)) {
			throw new IllegalArgumentException(LOOPS_NOT_ALLOWED);
		}

		if (!type.isAllowingMultipleEdges()) {

			if (!specifics.addEdgeToTouchingVerticesIfAbsent(sourceVertex, targetVertex, e)) {
				return false;
			}
			boolean edgeAdded = false;
			try {
				edgeAdded = intrusiveEdgesSpecifics.add(e, sourceVertex, targetVertex);
			} finally {
				if (!edgeAdded) {
					// edge was already present or adding threw an exception -> revert add
					specifics.removeEdgeFromTouchingVertices(sourceVertex, targetVertex, e);
				}
			}
			return edgeAdded;
		} else {
			if (intrusiveEdgesSpecifics.add(e, sourceVertex, targetVertex)) {
				specifics.addEdgeToTouchingVertices(sourceVertex, targetVertex, e);
				return true;
			}
			return false;
		}
	}

	@Override
	public V addVertex() {
		if (vertexSupplier == null) {
			throw new UnsupportedOperationException(THE_GRAPH_CONTAINS_NO_VERTEX_SUPPLIER);
		}

		V v = vertexSupplier.get();

		if (!specifics.addVertex(v)) {
			throw new IllegalArgumentException(INVALID_VERTEX_SUPPLIER_DOES_NOT_RETURN_UNIQUE_VERTICES_ON_EACH_CALL);
		}
		return v;
	}

	@Override
	public boolean addVertex(V v) {
		if (v == null) {
			throw new NullPointerException();
		} else if (containsVertex(v)) {
			return false;
		} else {
			specifics.addVertex(v);
			return true;
		}
	}

	@Override
	public V getEdgeSource(E e) {
		return intrusiveEdgesSpecifics.getEdgeSource(e);
	}

	@Override
	public V getEdgeTarget(E e) {
		return intrusiveEdgesSpecifics.getEdgeTarget(e);
	}

	@Override
	public Object clone() {
		try {
			AbstractBaseGraph<V, E> newGraph = TypeUtil.uncheckedCast(super.clone());

			newGraph.vertexSupplier = this.vertexSupplier;
			newGraph.edgeSupplier = this.edgeSupplier;
			newGraph.type = type;
			newGraph.unmodifiableVertexSet = null;

			newGraph.graphSpecificsStrategy = this.graphSpecificsStrategy;

			// NOTE: it's important for this to happen in an object
			// method so that the new inner class instance gets associated with
			// the right outer class instance
			newGraph.specifics = newGraph.graphSpecificsStrategy.getSpecificsFactory().apply(newGraph, newGraph.type);
			newGraph.intrusiveEdgesSpecifics = newGraph.graphSpecificsStrategy.getIntrusiveEdgesSpecificsFactory()
					.apply(newGraph.type);

			newGraph.graphIterables = null;

			Graphs.addGraph(newGraph, this);

			return newGraph;
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	public boolean containsEdge(E e) {
		return intrusiveEdgesSpecifics.containsEdge(e);
	}

	@Override
	public boolean containsVertex(V v) {
		return specifics.getVertexSet().contains(v);
	}

	@Override
	public int degreeOf(V vertex) {
		assertVertexExist(vertex);
		return specifics.degreeOf(vertex);
	}

	@Override
	public Set<E> edgeSet() {
		return intrusiveEdgesSpecifics.getEdgeSet();
	}

	@Override
	public Set<E> edgesOf(V vertex) {
		assertVertexExist(vertex);
		return specifics.edgesOf(vertex);
	}

	@Override
	public int inDegreeOf(V vertex) {
		assertVertexExist(vertex);
		return specifics.inDegreeOf(vertex);
	}

	@Override
	public Set<E> incomingEdgesOf(V vertex) {
		assertVertexExist(vertex);
		return specifics.incomingEdgesOf(vertex);
	}

	@Override
	public int outDegreeOf(V vertex) {
		assertVertexExist(vertex);
		return specifics.outDegreeOf(vertex);
	}

	@Override
	public Set<E> outgoingEdgesOf(V vertex) {
		assertVertexExist(vertex);
		return specifics.outgoingEdgesOf(vertex);
	}

	@Override
	public E removeEdge(V sourceVertex, V targetVertex) {
		E e = getEdge(sourceVertex, targetVertex);

		if (e != null) {
			specifics.removeEdgeFromTouchingVertices(sourceVertex, targetVertex, e);
			intrusiveEdgesSpecifics.remove(e);
		}

		return e;
	}

	@Override
	public boolean removeEdge(E e) {
		if (containsEdge(e)) {
			V sourceVertex = getEdgeSource(e);
			V targetVertex = getEdgeTarget(e);
			specifics.removeEdgeFromTouchingVertices(sourceVertex, targetVertex, e);
			intrusiveEdgesSpecifics.remove(e);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean removeVertex(V v) {
		if (containsVertex(v)) {
			Set<E> touchingEdgesList = edgesOf(v);

			// cannot iterate over list - will cause
			// ConcurrentModificationException
			removeAllEdges(new ArrayList<>(touchingEdgesList));

			specifics.getVertexSet().remove(v); // remove the vertex itself

			return true;
		} else {
			return false;
		}
	}

	@Override
	public Set<V> vertexSet() {
		if (unmodifiableVertexSet == null) {
			unmodifiableVertexSet = Collections.unmodifiableSet(specifics.getVertexSet());
		}

		return unmodifiableVertexSet;
	}

	@Override
	public double getEdgeWeight(E e) {
		if (e == null) {
			throw new NullPointerException();
		}
		return intrusiveEdgesSpecifics.getEdgeWeight(e);
	}

	@Override
	public void setEdgeWeight(E e, double weight) {
		if (e == null) {
			throw new NullPointerException();
		}
		intrusiveEdgesSpecifics.setEdgeWeight(e, weight);
	}

	@Override
	public GraphType getType() {
		return type;
	}

	@Override
	public GraphIterables<V, E> iterables() {
		// override interface to avoid instantiating frequently
		if (graphIterables == null) {
			graphIterables = new DefaultGraphIterables<>(this);
		}
		return graphIterables;
	}
}
