package edu.northeastern.info6205.tspsolver.jgrapht.graph;

import edu.northeastern.info6205.tspsolver.jgrapht.graph.builder.GraphBuilder;
import edu.northeastern.info6205.tspsolver.jgrapht.util.SupplierUtil;

import java.util.function.Supplier;

public class SimpleWeightedGraph<V, E> extends SimpleGraph<V, E> {
	private static final long serialVersionUID = 1L;

	public SimpleWeightedGraph(Class<? extends E> edgeClass) {
		this(null, SupplierUtil.createSupplier(edgeClass));
	}

	public SimpleWeightedGraph(Supplier<V> vertexSupplier, Supplier<E> edgeSupplier) {
		super(vertexSupplier, edgeSupplier, true);
	}

	public static <V, E> GraphBuilder<V, E, ? extends SimpleWeightedGraph<V, E>> createBuilder(
			Class<? extends E> edgeClass) {
		return new GraphBuilder<>(new SimpleWeightedGraph<>(edgeClass));
	}

	public static <V, E> GraphBuilder<V, E, ? extends SimpleWeightedGraph<V, E>> createBuilder(
			Supplier<E> edgeSupplier) {
		return new GraphBuilder<>(new SimpleWeightedGraph<>(null, edgeSupplier));
	}

}
