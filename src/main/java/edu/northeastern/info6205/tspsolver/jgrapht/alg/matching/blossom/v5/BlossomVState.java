package edu.northeastern.info6205.tspsolver.jgrapht.alg.matching.blossom.v5;

import edu.northeastern.info6205.tspsolver.jgrapht.Graph;

import java.util.List;

public class BlossomVState<V, E> {

	final int nodeNum;

	final int edgeNum;

	Graph<V, E> graph;

	BlossomVNode[] nodes;

	BlossomVEdge[] edges;

	int treeNum;

	int removedNum;

	int blossomNum;

	KolmogorovWeightedPerfectMatching.Statistics statistics;

	BlossomVOptions options;

	List<V> graphVertices;

	List<E> graphEdges;

	double minEdgeWeight;

	public BlossomVState(Graph<V, E> graph, BlossomVNode[] nodes, BlossomVEdge[] edges, int nodeNum, int edgeNum,
						 int treeNum, List<V> graphVertices, List<E> graphEdges, BlossomVOptions options, double minEdgeWeight) {
		this.graph = graph;
		this.nodes = nodes;
		this.edges = edges;
		this.nodeNum = nodeNum;
		this.edgeNum = edgeNum;
		this.treeNum = treeNum;
		this.graphVertices = graphVertices;
		this.graphEdges = graphEdges;
		this.options = options;
		this.statistics = new KolmogorovWeightedPerfectMatching.Statistics();
		this.minEdgeWeight = minEdgeWeight;
	}

}
