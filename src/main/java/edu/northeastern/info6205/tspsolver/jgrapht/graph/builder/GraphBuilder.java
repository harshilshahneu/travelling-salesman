package edu.northeastern.info6205.tspsolver.jgrapht.graph.builder;

import edu.northeastern.info6205.tspsolver.jgrapht.Graph;

public class GraphBuilder<V, E, G extends Graph<V, E>> extends AbstractGraphBuilder<V, E, G, GraphBuilder<V, E, G>> {

	public GraphBuilder(G baseGraph) {
		super(baseGraph);
	}

	@Override
	protected GraphBuilder<V, E, G> self() {
		return this;
	}

}
