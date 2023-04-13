package edu.northeastern.info6205.tspsolver.jgrapht.graph;

import edu.northeastern.info6205.tspsolver.jgrapht.Graph;
import edu.northeastern.info6205.tspsolver.jgrapht.GraphType;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;

public class GraphDelegator<V, E> extends AbstractGraph<V, E> implements Graph<V, E>, Serializable {
	private static final long serialVersionUID = 1L;
	
	private final Graph<V, E> delegate;
	private final Supplier<V> vertexSupplier;
	private final Supplier<E> edgeSupplier;

	public GraphDelegator(Graph<V, E> graph) {
		this(graph, null, null);
	}

	public GraphDelegator(Graph<V, E> graph, Supplier<V> vertexSupplier, Supplier<E> edgeSupplier) {
		super();
		this.delegate = Objects.requireNonNull(graph, "graph must not be null");
		this.vertexSupplier = vertexSupplier;
		this.edgeSupplier = edgeSupplier;
	}

	@Override
	public Supplier<V> getVertexSupplier() {
		if (vertexSupplier != null) {
			return vertexSupplier;
		} else {
			return delegate.getVertexSupplier();
		}
	}

	@Override
	public Supplier<E> getEdgeSupplier() {
		if (edgeSupplier != null) {
			return edgeSupplier;
		} else {
			return delegate.getEdgeSupplier();
		}
	}

	@Override
	public Set<E> getAllEdges(V sourceVertex, V targetVertex) {
		return delegate.getAllEdges(sourceVertex, targetVertex);
	}

	@Override
	public E getEdge(V sourceVertex, V targetVertex) {
		return delegate.getEdge(sourceVertex, targetVertex);
	}

	@Override
	public E addEdge(V sourceVertex, V targetVertex) {

		if (edgeSupplier != null) {
			E e = edgeSupplier.get();
			return this.addEdge(sourceVertex, targetVertex, e) ? e : null;
		}
		return delegate.addEdge(sourceVertex, targetVertex);
	}

	@Override
	public boolean addEdge(V sourceVertex, V targetVertex, E e) {
		return delegate.addEdge(sourceVertex, targetVertex, e);
	}

	@Override
	public V addVertex() {

		if (vertexSupplier != null) {
			V v = vertexSupplier.get();
			return this.addVertex(v) ? v : null;
		}
		return delegate.addVertex();
	}

	@Override
	public boolean addVertex(V v) {
		return delegate.addVertex(v);
	}

	@Override
	public boolean containsEdge(E e) {
		return delegate.containsEdge(e);
	}

	@Override
	public boolean containsVertex(V v) {
		return delegate.containsVertex(v);
	}

	public int degreeOf(V vertex) {
		return delegate.degreeOf(vertex);
	}

	@Override
	public Set<E> edgeSet() {
		return delegate.edgeSet();
	}

	@Override
	public Set<E> edgesOf(V vertex) {
		return delegate.edgesOf(vertex);
	}

	@Override
	public int inDegreeOf(V vertex) {
		return delegate.inDegreeOf(vertex);
	}

	@Override
	public Set<E> incomingEdgesOf(V vertex) {
		return delegate.incomingEdgesOf(vertex);
	}

	@Override
	public int outDegreeOf(V vertex) {
		return delegate.outDegreeOf(vertex);
	}

	@Override
	public Set<E> outgoingEdgesOf(V vertex) {
		return delegate.outgoingEdgesOf(vertex);
	}

	@Override
	public boolean removeEdge(E e) {
		return delegate.removeEdge(e);
	}

	@Override
	public E removeEdge(V sourceVertex, V targetVertex) {
		return delegate.removeEdge(sourceVertex, targetVertex);
	}

	@Override
	public boolean removeVertex(V v) {
		return delegate.removeVertex(v);
	}

	@Override
	public String toString() {
		return delegate.toString();
	}

	@Override
	public Set<V> vertexSet() {
		return delegate.vertexSet();
	}

	@Override
	public V getEdgeSource(E e) {
		return delegate.getEdgeSource(e);
	}

	@Override
	public V getEdgeTarget(E e) {
		return delegate.getEdgeTarget(e);
	}

	@Override
	public double getEdgeWeight(E e) {
		return delegate.getEdgeWeight(e);
	}

	@Override
	public void setEdgeWeight(E e, double weight) {
		delegate.setEdgeWeight(e, weight);
	}

	@Override
	public GraphType getType() {
		return delegate.getType();
	}

	protected Graph<V, E> getDelegate() {
		return delegate;
	}

}
