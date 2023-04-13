package edu.northeastern.info6205.tspsolver.jgrapht.graph;


import edu.northeastern.info6205.tspsolver.jgrapht.*;

import java.util.Objects;

public class DefaultGraphIterables<V, E> implements GraphIterables<V, E> {
	protected Graph<V, E> graph;

	public DefaultGraphIterables() {
		this(null);
	}

	public DefaultGraphIterables(Graph<V, E> graph) {
		this.graph = Objects.requireNonNull(graph);
	}

	@Override
	public Graph<V, E> getGraph() {
		return graph;
	}

}
