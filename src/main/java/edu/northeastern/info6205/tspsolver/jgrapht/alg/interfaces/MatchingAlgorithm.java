package edu.northeastern.info6205.tspsolver.jgrapht.alg.interfaces;

//import org.jgrapht.Graph;


import edu.northeastern.info6205.tspsolver.jgrapht.Graph;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public interface MatchingAlgorithm<V, E> {
	
	double DEFAULT_EPSILON = 1e-9;

	Matching<V, E> getMatching();

	interface Matching<V, E> extends Iterable<E> {
	
		Graph<V, E> getGraph();

		double getWeight();

		Set<E> getEdges();

		default boolean isMatched(V v) {
			Set<E> edges = getEdges();
			return getGraph().edgesOf(v).stream().anyMatch(edges::contains);
		}

		default boolean isPerfect() {
			return getEdges().size() == getGraph().vertexSet().size() / 2.0;
		}

		@Override
		default Iterator<E> iterator() {
			return getEdges().iterator();
		}
	}

	class MatchingImpl<V, E> implements Matching<V, E>, Serializable {
		private static final long serialVersionUID = 1L;
		
		private Graph<V, E> graph;
		private Set<E> edges;
		private double weight;
		private Set<V> matchedVertices = null;

		public MatchingImpl(Graph<V, E> graph, Set<E> edges, double weight) {
			this.graph = graph;
			this.edges = edges;
			this.weight = weight;
		}

		@Override
		public Graph<V, E> getGraph() {
			return graph;
		}

		@Override
		public double getWeight() {
			return weight;
		}

		@Override
		public Set<E> getEdges() {
			return edges;
		}

		@Override
		public boolean isMatched(V v) {
			if (matchedVertices == null) { // lazily index the vertices that have been matched
				matchedVertices = new HashSet<>();
				for (E e : edges) {
					matchedVertices.add(graph.getEdgeSource(e));
					matchedVertices.add(graph.getEdgeTarget(e));
				}
			}
			return matchedVertices.contains(v);
		}

		@Override
		public String toString() {
			return "Matching [edges=" + edges + ", weight=" + weight + "]";
		}

	}

}
