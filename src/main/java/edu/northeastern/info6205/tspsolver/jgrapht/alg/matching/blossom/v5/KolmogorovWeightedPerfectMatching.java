package edu.northeastern.info6205.tspsolver.jgrapht.alg.matching.blossom.v5;

import edu.northeastern.info6205.tspsolver.jgrapht.Graph;
import edu.northeastern.info6205.tspsolver.jgrapht.alg.interfaces.MatchingAlgorithm;
import edu.northeastern.info6205.tspsolver.jgrapht.alg.util.Pair;
import edu.northeastern.info6205.tspsolver.jgrapht.graph.AsWeightedGraph;

import java.util.*;

public class KolmogorovWeightedPerfectMatching<V, E> implements MatchingAlgorithm<V, E> {

	public static final double EPS = MatchingAlgorithm.DEFAULT_EPSILON;

	public static final double INFINITY = 1e100;

	public static final double NO_PERFECT_MATCHING_THRESHOLD = 1e10;

	public static final BlossomVOptions DEFAULT_OPTIONS = new BlossomVOptions();

	static final String NO_PERFECT_MATCHING = "There is no perfect matching in the specified graph";

	private final Graph<V, E> initialGraph;

	private final Graph<V, E> graph;

	BlossomVState<V, E> state;

	private BlossomVPrimalUpdater<V, E> primalUpdater;

	private BlossomVDualUpdater<V, E> dualUpdater;

	private Matching<V, E> matching;

	private DualSolution<V, E> dualSolution;

	private BlossomVOptions options;

	private ObjectiveSense objectiveSense;

	public KolmogorovWeightedPerfectMatching(Graph<V, E> graph) {
		this(graph, DEFAULT_OPTIONS, ObjectiveSense.MINIMIZE);
	}

	public KolmogorovWeightedPerfectMatching(Graph<V, E> graph, ObjectiveSense objectiveSense) {
		this(graph, DEFAULT_OPTIONS, objectiveSense);
	}

	public KolmogorovWeightedPerfectMatching(Graph<V, E> graph, BlossomVOptions options) {
		this(graph, options, ObjectiveSense.MINIMIZE);
	}

	public KolmogorovWeightedPerfectMatching(Graph<V, E> graph, BlossomVOptions options,
			ObjectiveSense objectiveSense) {
		Objects.requireNonNull(graph);
		this.objectiveSense = objectiveSense;
		if ((graph.vertexSet().size() & 1) == 1) {
			throw new IllegalArgumentException(NO_PERFECT_MATCHING);
		} else if (objectiveSense == ObjectiveSense.MAXIMIZE) {
			this.graph = new AsWeightedGraph<>(graph, e -> -graph.getEdgeWeight(e), true, false);
		} else {
			this.graph = graph;
		}
		this.initialGraph = graph;
		this.options = Objects.requireNonNull(options);
	}

	@Override
	public Matching<V, E> getMatching() {
		if (matching == null) {
			lazyComputeWeightedPerfectMatching();
		}
		return matching;
	}

	public DualSolution<V, E> getDualSolution() {
		dualSolution = lazyComputeDualSolution();
		return dualSolution;
	}

	public boolean testOptimality() {
		lazyComputeWeightedPerfectMatching();
		return getError() < EPS; // getError() won't return -1 since matching != null
	}

	public double getError() {
		lazyComputeWeightedPerfectMatching();
		double error = testNonNegativity();
		Set<E> matchedEdges = matching.getEdges();
		for (int i = 0; i < state.graphEdges.size(); i++) {
			E graphEdge = state.graphEdges.get(i);
			BlossomVEdge edge = state.edges[i];
			double slack = graph.getEdgeWeight(graphEdge);
			slack -= state.minEdgeWeight;
			BlossomVNode a = edge.headOriginal[0];
			BlossomVNode b = edge.headOriginal[1];

			Pair<BlossomVNode, BlossomVNode> lca = lca(a, b);
			slack -= totalDual(a, lca.getFirst());
			slack -= totalDual(b, lca.getSecond());

			if (lca.getFirst() == lca.getSecond()) {
				// if a and b have a common ancestor, its dual is subtracted from edge's slack
				slack += 2 * lca.getFirst().getTrueDual();
			}
			if (slack < 0 || matchedEdges.contains(graphEdge)) {
				error += Math.abs(slack);
			}
		}
		return error;
	}

	private void lazyComputeWeightedPerfectMatching() {
		if (matching != null) {
			return;
		}
		BlossomVInitializer<V, E> initializer = new BlossomVInitializer<>(graph);
		this.state = initializer.initialize(options);
		this.primalUpdater = new BlossomVPrimalUpdater<>(state);
		this.dualUpdater = new BlossomVDualUpdater<>(state, primalUpdater);

		while (true) {
			int cycleTreeNum = state.treeNum;

			for (BlossomVNode currentRoot = state.nodes[state.nodeNum].treeSiblingNext; currentRoot != null;) {
				// initialize variables
				BlossomVNode nextRoot = currentRoot.treeSiblingNext;
				BlossomVNode nextNextRoot = null;
				if (nextRoot != null) {
					nextNextRoot = nextRoot.treeSiblingNext;
				}
				BlossomVTree tree = currentRoot.tree;
				int iterationTreeNum = state.treeNum;

				// first phase
				setCurrentEdgesAndTryToAugment(tree);

				if (iterationTreeNum == state.treeNum && options.updateDualsBefore) {
					dualUpdater.updateDualsSingle(tree);
				}

				// second phase
				// apply primal operations to the current tree while it is possible
				while (iterationTreeNum == state.treeNum) {
					if (!tree.plusInfinityEdges.isEmpty()) {
						// can grow tree
						BlossomVEdge edge = tree.plusInfinityEdges.findMin().getValue();
						if (edge.slack <= tree.eps) {
							primalUpdater.grow(edge, true, true);
							continue;
						}
					}
					if (!tree.plusPlusEdges.isEmpty()) {
						// can shrink blossom
						BlossomVEdge edge = tree.plusPlusEdges.findMin().getValue();
						if (edge.slack <= 2 * tree.eps) {
							primalUpdater.shrink(edge, true);
							continue;
						}
					}
					if (!tree.minusBlossoms.isEmpty()) {
						// can expand blossom
						BlossomVNode node = tree.minusBlossoms.findMin().getValue();
						if (node.dual <= tree.eps) {
							primalUpdater.expand(node, true);
							continue;
						}
					}
					// can't do anything
					break;
				}

				// third phase
				if (state.treeNum == iterationTreeNum) {
					tree.currentEdge = null;
					if (options.updateDualsAfter && dualUpdater.updateDualsSingle(tree)) {
						// since some progress has been made, continue with the same trees
						continue;
					}
					// clear current edge pointers
					tree.clearCurrentEdges();
				}
				currentRoot = nextRoot;
				if (nextRoot != null && nextRoot.isInfinityNode()) {
					currentRoot = nextNextRoot;
				}
			}

			if (state.treeNum == 0) {
				// we are done
				break;
			}
			if (cycleTreeNum == state.treeNum && dualUpdater.updateDuals(options.dualUpdateStrategy) <= 0) {
				dualUpdater.updateDuals(BlossomVOptions.DualUpdateStrategy.MULTIPLE_TREE_CONNECTED_COMPONENTS);
			}
		}
		finish();
	}

	private void setCurrentEdgesAndTryToAugment(BlossomVTree tree) {
		for (BlossomVTree.TreeEdgeIterator iterator = tree.treeEdgeIterator(); iterator.hasNext();) {
			BlossomVTreeEdge treeEdge = iterator.next();
			BlossomVTree opposite = treeEdge.head[iterator.getCurrentDirection()];

			if (!treeEdge.plusPlusEdges.isEmpty()) {
				BlossomVEdge edge = treeEdge.plusPlusEdges.findMin().getValue();
				if (edge.slack <= tree.eps + opposite.eps) {
					primalUpdater.augment(edge);
					break;
				}
			}

			opposite.currentEdge = treeEdge;
			opposite.currentDirection = iterator.getCurrentDirection();
		}
	}

	private double testNonNegativity() {
		BlossomVNode[] nodes = state.nodes;
		double error = 0;
		for (int i = 0; i < state.nodeNum; i++) {
			BlossomVNode node = nodes[i].blossomParent;
			while (node != null && !node.isMarked) {
				if (node.dual < 0) {
					error += Math.abs(node.dual);
					break;
				}
				node.isMarked = true;
				node = node.blossomParent;
			}
		}
		clearMarked();
		return error;
	}

	private double totalDual(BlossomVNode start, BlossomVNode end) {
		if (end == start) {
			return start.getTrueDual();
		} else {
			double result = 0;
			BlossomVNode current = start;
			do {
				result += current.getTrueDual();
				current = current.blossomParent;
			} while (current != null && current != end);
			result += end.getTrueDual();
			return result;
		}
	}

	private Pair<BlossomVNode, BlossomVNode> lca(BlossomVNode a, BlossomVNode b) {
		BlossomVNode[] branches = new BlossomVNode[] { a, b };
		int dir = 0;
		Pair<BlossomVNode, BlossomVNode> result;
		while (true) {
			if (branches[dir].isMarked) {
				result = new Pair<>(branches[dir], branches[dir]);
				break;
			}
			branches[dir].isMarked = true;
			if (branches[dir].isOuter) {
				BlossomVNode jumpNode = branches[1 - dir];
				while (!jumpNode.isOuter && !jumpNode.isMarked) {
					jumpNode = jumpNode.blossomParent;
				}
				if (jumpNode.isMarked) {
					result = new Pair<>(jumpNode, jumpNode);
				} else {
					result = dir == 0 ? new Pair<>(branches[dir], jumpNode) : new Pair<>(jumpNode, branches[dir]);
				}
				break;
			}
			branches[dir] = branches[dir].blossomParent;
			dir = 1 - dir;
		}
		clearMarked(a);
		clearMarked(b);
		return result;
	}

	private void clearMarked(BlossomVNode node) {
		do {
			node.isMarked = false;
			node = node.blossomParent;
		} while (node != null && node.isMarked);
	}

	private void clearMarked() {
		BlossomVNode[] nodes = state.nodes;
		for (int i = 0; i < state.nodeNum; i++) {
			BlossomVNode current = nodes[i];
			do {
				current.isMarked = false;
				current = current.blossomParent;
			} while (current != null && current.isMarked);
		}
	}

	private void finish() {
		Set<E> edges = new HashSet<>();
		BlossomVNode[] nodes = state.nodes;
		List<BlossomVNode> processed = new LinkedList<>();

		for (int i = 0; i < state.nodeNum; i++) {
			if (nodes[i].matched == null) {
				BlossomVNode blossomPrev = null;
				BlossomVNode blossom = nodes[i];
				// traverse the path from unmatched node to the first unprocessed pseudonode
				do {
					blossom.blossomGrandparent = blossomPrev;
					blossomPrev = blossom;
					blossom = blossomPrev.blossomParent;
				} while (!blossom.isOuter);
				// now node.blossomGrandparent points to the previous blossom in the hierarchy
				// (not
				// counting the blossom node)
				while (true) {
					// find the root of the blossom. This can be a pseudonode
					BlossomVNode blossomRoot = blossom.matched.getCurrentOriginal(blossom);
					if (blossomRoot == null) {
						blossomRoot = blossom.matched.head[0].isProcessed ? blossom.matched.headOriginal[1]
								: blossom.matched.headOriginal[0];
					}
					while (blossomRoot.blossomParent != blossom) {
						blossomRoot = blossomRoot.blossomParent;
					}
					blossomRoot.matched = blossom.matched;
					BlossomVNode node = blossom.getOppositeMatched();
					if (node != null) {
						node.isProcessed = true;
						processed.add(node);
					}
					node = blossomRoot.blossomSibling.getOpposite(blossomRoot);
					// chang the matching in the blossom
					while (node != blossomRoot) {
						node.matched = node.blossomSibling;
						BlossomVNode nextNode = node.blossomSibling.getOpposite(node);
						nextNode.matched = node.matched;
						node = nextNode.blossomSibling.getOpposite(nextNode);
					}
					if (!blossomPrev.isBlossom) {
						break;
					}
					blossom = blossomPrev;
					blossomPrev = blossom.blossomGrandparent;
				}
				for (BlossomVNode processedNode : processed) {
					processedNode.isProcessed = false;
				}
				processed.clear();
			}
		}
		// compute the final matching
		double weight = 0;
		for (int i = 0; i < state.nodeNum; i++) {
			E graphEdge = state.graphEdges.get(nodes[i].matched.pos);
			if (!edges.contains(graphEdge)) {
				edges.add(graphEdge);
				weight += state.graph.getEdgeWeight(graphEdge);
			}
		}
		if (objectiveSense == ObjectiveSense.MAXIMIZE) {
			weight = -weight;
		}
		matching = new MatchingImpl<>(state.graph, edges, weight);
	}

	private void prepareForDualSolution() {
		BlossomVNode[] nodes = state.nodes;
		for (int i = 0; i < state.nodeNum; i++) {
			BlossomVNode current = nodes[i];
			BlossomVNode prev = null;
			do {
				current.blossomGrandparent = prev;
				current.isMarked = true;
				prev = current;
				current = current.blossomParent;
			} while (current != null && !current.isMarked);
		}
		clearMarked();
	}

	private Set<V> getBlossomNodes(BlossomVNode pseudonode, Map<BlossomVNode, Set<V>> blossomNodes) {
		if (blossomNodes.containsKey(pseudonode)) {
			return blossomNodes.get(pseudonode);
		}
		Set<V> result = new HashSet<>();
		BlossomVNode endNode = pseudonode.blossomGrandparent;
		BlossomVNode current = endNode;
		do {
			if (current.isBlossom) {
				if (!blossomNodes.containsKey(current)) {
					result.addAll(getBlossomNodes(current, blossomNodes));
				} else {
					result.addAll(blossomNodes.get(current));
				}
			} else {
				result.add(state.graphVertices.get(current.pos));
			}
			current = current.blossomSibling.getOpposite(current);
		} while (current != endNode);
		blossomNodes.put(pseudonode, result);
		return result;
	}

	private DualSolution<V, E> lazyComputeDualSolution() {
		lazyComputeWeightedPerfectMatching();
		if (dualSolution != null) {
			return dualSolution;
		}
		Map<Set<V>, Double> dualMap = new HashMap<>();
		Map<BlossomVNode, Set<V>> nodesInBlossoms = new HashMap<>();
		BlossomVNode[] nodes = state.nodes;
		prepareForDualSolution();
		double dualShift = state.minEdgeWeight / 2;
		for (int i = 0; i < state.nodeNum; i++) {
			BlossomVNode current = nodes[i];
			// jump up while the first already processed node is encountered
			do {
				double dual = current.getTrueDual();
				if (!current.isBlossom) {
					dual += dualShift;
				}
				if (objectiveSense == ObjectiveSense.MAXIMIZE) {
					dual = -dual;
				}
				if (Math.abs(dual) > EPS) {
					if (current.isBlossom) {
						dualMap.put(getBlossomNodes(current, nodesInBlossoms), dual);
					} else {
						dualMap.put(Collections.singleton(state.graphVertices.get(current.pos)), dual);
					}
				}
				current.isMarked = true;
				if (current.isOuter) {
					break;
				}
				current = current.blossomParent;
			} while (current != null && !current.isMarked);
		}
		clearMarked();
		return new DualSolution<>(initialGraph, dualMap);
	}

	public Statistics getStatistics() {
		return state.statistics;
	}

	public static class Statistics {

		int shrinkNum = 0;

		int expandNum = 0;

		int growNum = 0;

		long augmentTime = 0;

		long expandTime = 0;

		long shrinkTime = 0;

		long growTime = 0;

		long dualUpdatesTime = 0;

		public int getShrinkNum() {
			return shrinkNum;
		}

		public int getExpandNum() {
			return expandNum;
		}

		public int getGrowNum() {
			return growNum;
		}

		public long getAugmentTime() {
			return augmentTime;
		}

		public long getExpandTime() {
			return expandTime;
		}

		public long getShrinkTime() {
			return shrinkTime;
		}

		public long getGrowTime() {
			return growTime;
		}

		public long getDualUpdatesTime() {
			return dualUpdatesTime;
		}

		@Override
		public String toString() {
			return "Statistics{shrinkNum=" + shrinkNum + ", expandNum=" + expandNum + ", growNum=" + growNum
					+ ", augmentTime=" + augmentTime + ", expandTime=" + expandTime + ", shrinkTime=" + shrinkTime
					+ ", growTime=" + growTime + '}';
		}
	}

	public static class DualSolution<V, E> {

		Graph<V, E> graph;

		Map<Set<V>, Double> dualVariables;

		public DualSolution(Graph<V, E> graph, Map<Set<V>, Double> dualVariables) {
			this.graph = graph;
			this.dualVariables = dualVariables;
		}

		public Graph<V, E> getGraph() {
			return graph;
		}

		public Map<Set<V>, Double> getDualVariables() {
			return dualVariables;
		}

		@Override
		public String toString() {
			final StringBuilder sb = new StringBuilder("DualSolution{");
			sb.append("graph=").append(graph);
			sb.append(", dualVariables=").append(dualVariables);
			sb.append('}');
			return sb.toString();
		}
	}
}
