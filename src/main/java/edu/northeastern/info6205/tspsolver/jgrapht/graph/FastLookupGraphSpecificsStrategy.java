package edu.northeastern.info6205.tspsolver.jgrapht.graph;

import edu.northeastern.info6205.tspsolver.jgrapht.Graph;
import edu.northeastern.info6205.tspsolver.jgrapht.GraphType;
import edu.northeastern.info6205.tspsolver.jgrapht.graph.specifics.FastLookupDirectedSpecifics;
import edu.northeastern.info6205.tspsolver.jgrapht.graph.specifics.FastLookupUndirectedSpecifics;
import edu.northeastern.info6205.tspsolver.jgrapht.graph.specifics.Specifics;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.function.BiFunction;
import java.util.function.Function;

public class FastLookupGraphSpecificsStrategy<V, E> implements GraphSpecificsStrategy<V, E> {
	private static final long serialVersionUID = 1L;

	@SuppressWarnings({ "unchecked" })
	@Override
	public Function<GraphType, IntrusiveEdgesSpecifics<V, E>> getIntrusiveEdgesSpecificsFactory() {
		return (Function<GraphType, IntrusiveEdgesSpecifics<V, E>> & Serializable) (type) -> {
			if (type.isWeighted()) {
				return new WeightedIntrusiveEdgesSpecifics<V, E>(new LinkedHashMap<>());
			} else {
				return new UniformIntrusiveEdgesSpecifics<>(new LinkedHashMap<>());
			}
		};
	}

	@SuppressWarnings("unchecked")
	@Override
	public BiFunction<Graph<V, E>, GraphType, Specifics<V, E>> getSpecificsFactory() {
		return (BiFunction<Graph<V, E>, GraphType, Specifics<V, E>> & Serializable) (graph, type) -> {
			if (type.isDirected()) {
				return new FastLookupDirectedSpecifics<>(graph, new LinkedHashMap<>(), new HashMap<>(),
						getEdgeSetFactory());
			} else {
				return new FastLookupUndirectedSpecifics<>(graph, new LinkedHashMap<>(), new HashMap<>(),
						getEdgeSetFactory());
			}
		};
	}

}
