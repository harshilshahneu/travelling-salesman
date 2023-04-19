package edu.northeastern.info6205.tspsolver.jgrapht.graph;

import edu.northeastern.info6205.tspsolver.jgrapht.Graph;
import edu.northeastern.info6205.tspsolver.jgrapht.GraphType;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

public class AsUnmodifiableGraph<V, E> extends GraphDelegator<V, E> implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final String UNMODIFIABLE = "this graph is unmodifiable";

	public AsUnmodifiableGraph(Graph<V, E> g) {
		super(g);
	}

	@Override
	public E addEdge(V sourceVertex, V targetVertex) {
		throw new UnsupportedOperationException(UNMODIFIABLE);
	}

	@Override
	public boolean addEdge(V sourceVertex, V targetVertex, E e) {
		throw new UnsupportedOperationException(UNMODIFIABLE);
	}

	@Override
	public V addVertex() {
		throw new UnsupportedOperationException(UNMODIFIABLE);
	}

	@Override
	public boolean addVertex(V v) {
		throw new UnsupportedOperationException(UNMODIFIABLE);
	}

	@Override
	public boolean removeAllEdges(Collection<? extends E> edges) {
		throw new UnsupportedOperationException(UNMODIFIABLE);
	}

	@Override
	public Set<E> removeAllEdges(V sourceVertex, V targetVertex) {
		throw new UnsupportedOperationException(UNMODIFIABLE);
	}

	@Override
	public boolean removeAllVertices(Collection<? extends V> vertices) {
		throw new UnsupportedOperationException(UNMODIFIABLE);
	}

	@Override
	public boolean removeEdge(E e) {
		throw new UnsupportedOperationException(UNMODIFIABLE);
	}

	@Override
	public E removeEdge(V sourceVertex, V targetVertex) {
		throw new UnsupportedOperationException(UNMODIFIABLE);
	}

	@Override
	public boolean removeVertex(V v) {
		throw new UnsupportedOperationException(UNMODIFIABLE);
	}

	@Override
	public GraphType getType() {
		return super.getType().asUnmodifiable();
	}
}
