package edu.northeastern.info6205.tspsolver.jgrapht.graph;

import edu.northeastern.info6205.tspsolver.jgrapht.Graph;
import edu.northeastern.info6205.tspsolver.jgrapht.GraphType;
import edu.northeastern.info6205.tspsolver.jgrapht.graph.specifics.ArrayUnenforcedSetEdgeSetFactory;
import edu.northeastern.info6205.tspsolver.jgrapht.graph.specifics.Specifics;

import java.io.Serializable;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface GraphSpecificsStrategy<V, E> extends Serializable {

	Function<GraphType, IntrusiveEdgesSpecifics<V, E>> getIntrusiveEdgesSpecificsFactory();

	BiFunction<Graph<V, E>, GraphType, Specifics<V, E>> getSpecificsFactory();

	default EdgeSetFactory<V, E> getEdgeSetFactory() {
		return new ArrayUnenforcedSetEdgeSetFactory<V, E>();
	}

}
