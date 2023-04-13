package edu.northeastern.info6205.tspsolver.jgrapht.graph;

import edu.northeastern.info6205.tspsolver.jgrapht.Graph;
import edu.northeastern.info6205.tspsolver.jgrapht.GraphTests;
import edu.northeastern.info6205.tspsolver.jgrapht.GraphType;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

public class AsWeightedGraph<V, E> extends GraphDelegator<V, E> implements Serializable, Graph<V, E> {
	private static final long serialVersionUID = 1L;

	private final Function<E, Double> weightFunction;
	private final Map<E, Double> weights;
	private final boolean writeWeightsThrough;
	private final boolean cacheWeights;

	public AsWeightedGraph(Graph<V, E> graph, Map<E, Double> weights) {
		this(graph, weights, graph.getType().isWeighted());
	}

	public AsWeightedGraph(Graph<V, E> graph, Map<E, Double> weights, boolean writeWeightsThrough) {
		super(graph);
		this.weights = Objects.requireNonNull(weights);
		this.weightFunction = null;
		this.cacheWeights = false;
		this.writeWeightsThrough = writeWeightsThrough;

		if (this.writeWeightsThrough)
			GraphTests.requireWeighted(graph);
	}

	public AsWeightedGraph(Graph<V, E> graph, Function<E, Double> weightFunction, boolean cacheWeights,
			boolean writeWeightsThrough) {
		super(graph);
		this.weightFunction = Objects.requireNonNull(weightFunction);
		this.cacheWeights = cacheWeights;
		this.writeWeightsThrough = writeWeightsThrough;
		this.weights = new HashMap<>();

		if (this.writeWeightsThrough)
			GraphTests.requireWeighted(graph);
	}

	@Override
	public double getEdgeWeight(E e) {
		Double weight;
		if (weightFunction != null) {
			if (cacheWeights) // If weights are cached, check map first before invoking the weight
								// function
				weight = weights.computeIfAbsent(e, weightFunction);
			else
				weight = weightFunction.apply(e);
		} else {
			weight = weights.get(e);
		}

		if (Objects.isNull(weight))
			weight = super.getEdgeWeight(e);

		return weight;
	}

	@Override
	public void setEdgeWeight(E e, double weight) {
		assert e != null;

		if (weightFunction != null && !cacheWeights) {
			throw new UnsupportedOperationException(
					"Cannot set an edge weight when a weight function is used and caching is disabled");
		}

		this.weights.put(e, weight);

		if (this.writeWeightsThrough)
			this.getDelegate().setEdgeWeight(e, weight);
	}

	@Override
	public GraphType getType() {
		return super.getType().asWeighted();
	}

}
