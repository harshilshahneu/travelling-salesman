package edu.northeastern.info6205.tspsolver.jgrapht.graph;

import edu.northeastern.info6205.tspsolver.jgrapht.graph.builder.GraphBuilder;
import edu.northeastern.info6205.tspsolver.jgrapht.util.SupplierUtil;

import java.util.function.Supplier;

public class SimpleGraph<V, E> extends AbstractBaseGraph<V, E> {
	private static final long serialVersionUID = 1L;

	public SimpleGraph(Class<? extends E> edgeClass) {
		this(null, SupplierUtil.createSupplier(edgeClass), false);
	}

	public SimpleGraph(Supplier<V> vertexSupplier, Supplier<E> edgeSupplier, boolean weighted) {
		super(vertexSupplier, edgeSupplier, new DefaultGraphType.Builder().undirected().allowMultipleEdges(false)
				.allowSelfLoops(false).weighted(weighted).build());
	}

	public static <V, E> GraphBuilder<V, E, ? extends SimpleGraph<V, E>> createBuilder(Class<? extends E> edgeClass) {
		return new GraphBuilder<>(new SimpleGraph<>(edgeClass));
	}

	public static <V, E> GraphBuilder<V, E, ? extends SimpleGraph<V, E>> createBuilder(Supplier<E> edgeSupplier) {
		return new GraphBuilder<>(new SimpleGraph<>(null, edgeSupplier, false));
	}

}
