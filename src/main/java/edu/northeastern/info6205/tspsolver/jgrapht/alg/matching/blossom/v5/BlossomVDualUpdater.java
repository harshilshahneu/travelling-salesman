package edu.northeastern.info6205.tspsolver.jgrapht.alg.matching.blossom.v5;

import edu.northeastern.info6205.tspsolver.jheaps.MergeableAddressableHeap;

public class BlossomVDualUpdater<V, E> {
	
	private BlossomVState<V, E> state;
	
	private BlossomVPrimalUpdater<V, E> primalUpdater;

	public BlossomVDualUpdater(BlossomVState<V, E> state, BlossomVPrimalUpdater<V, E> primalUpdater) {
		this.state = state;
		this.primalUpdater = primalUpdater;
	}

	public double updateDuals(BlossomVOptions.DualUpdateStrategy type) {
		long start = System.nanoTime();

		BlossomVEdge augmentEdge = null;
		// go through all tree roots and determine the initial tree dual change wrt.
		// in-tree
		// constraints
		// the cross-tree constraints are handles wrt. dual update strategy
		for (BlossomVNode root = state.nodes[state.nodeNum].treeSiblingNext; root != null; root = root.treeSiblingNext) {
			BlossomVTree tree = root.tree;
			double eps = getEps(tree);
			tree.accumulatedEps = eps - tree.eps;
		}
		if (type == BlossomVOptions.DualUpdateStrategy.MULTIPLE_TREE_FIXED_DELTA) {
			augmentEdge = multipleTreeFixedDelta();
		} else if (type == BlossomVOptions.DualUpdateStrategy.MULTIPLE_TREE_CONNECTED_COMPONENTS) {
			augmentEdge = updateDualsConnectedComponents();
		}

		double dualChange = 0;
		// add tree.accumulatedEps to the tree.eps
		for (BlossomVNode root = state.nodes[state.nodeNum].treeSiblingNext; root != null; root = root.treeSiblingNext) {
			if (root.tree.accumulatedEps > KolmogorovWeightedPerfectMatching.EPS) {
				dualChange += root.tree.accumulatedEps;
				root.tree.eps += root.tree.accumulatedEps;
			}
		}
		
		state.statistics.dualUpdatesTime += System.nanoTime() - start;
		if (augmentEdge != null) {
			primalUpdater.augment(augmentEdge);
		}
		return dualChange;
	}

	private double getEps(BlossomVTree tree) {
		double eps = KolmogorovWeightedPerfectMatching.INFINITY;
		// check minimum slack of the plus-infinity edges
		if (!tree.plusInfinityEdges.isEmpty()) {
			BlossomVEdge edge = tree.plusInfinityEdges.findMin().getValue();
			if (edge.slack < eps) {
				eps = edge.slack;
			}
		}
		// check minimum dual variable of the "-" blossoms
		if (!tree.minusBlossoms.isEmpty()) {
			BlossomVNode node = tree.minusBlossoms.findMin().getValue();
			if (node.dual < eps) {
				eps = node.dual;

			}
		}
		// check minimum slack of the (+, +) edges
		if (!tree.plusPlusEdges.isEmpty()) {
			BlossomVEdge edge = tree.plusPlusEdges.findMin().getValue();
			if (2 * eps > edge.slack) {
				eps = edge.slack / 2;
			}
		}
		return eps;
	}

	public boolean updateDualsSingle(BlossomVTree tree) {
		long start = System.nanoTime();

		double eps = getEps(tree); // include only constraints on (+,+) in-tree edges, (+, inf)
									// edges and "-' blossoms
		double epsAugment = KolmogorovWeightedPerfectMatching.INFINITY; // takes into account
																		// constraints of the
																		// cross-tree edges
		BlossomVEdge augmentEdge = null; // the (+, +) cross-tree edge of minimum slack
		double delta = 0;
		for (BlossomVTree.TreeEdgeIterator iterator = tree.treeEdgeIterator(); iterator.hasNext();) {
			BlossomVTreeEdge treeEdge = iterator.next();
			BlossomVTree opposite = treeEdge.head[iterator.getCurrentDirection()];
			if (!treeEdge.plusPlusEdges.isEmpty()) {
				BlossomVEdge plusPlusEdge = treeEdge.plusPlusEdges.findMin().getValue();
				if (plusPlusEdge.slack - opposite.eps < epsAugment) {
					epsAugment = plusPlusEdge.slack - opposite.eps;
					augmentEdge = plusPlusEdge;
				}
			}
			MergeableAddressableHeap<Double, BlossomVEdge> currentPlusMinusHeap = treeEdge
					.getCurrentPlusMinusHeap(opposite.currentDirection);
			if (!currentPlusMinusHeap.isEmpty()) {
				BlossomVEdge edge = currentPlusMinusHeap.findMin().getValue();
				if (edge.slack + opposite.eps < eps) {
					eps = edge.slack + opposite.eps;

				}
			}
		}
		if (eps > epsAugment) {
			eps = epsAugment;
		}
		// now eps takes into account all the constraints
		if (eps > KolmogorovWeightedPerfectMatching.NO_PERFECT_MATCHING_THRESHOLD) {
			throw new IllegalArgumentException(KolmogorovWeightedPerfectMatching.NO_PERFECT_MATCHING);
		}
		if (eps > tree.eps) {
			delta = eps - tree.eps;
			tree.eps = eps;
		}

		state.statistics.dualUpdatesTime += System.nanoTime() - start;

		if (augmentEdge != null && epsAugment <= tree.eps) {
			primalUpdater.augment(augmentEdge);
			return false; // can't proceed with the same tree
		} else {
			return delta > KolmogorovWeightedPerfectMatching.EPS;
		}
	}

	private BlossomVEdge updateDualsConnectedComponents() {
		BlossomVTree dummyTree = new BlossomVTree();
		BlossomVEdge augmentEdge = null;
		double augmentEps = KolmogorovWeightedPerfectMatching.INFINITY;

		double oppositeEps;
		for (BlossomVNode root = state.nodes[state.nodeNum].treeSiblingNext; root != null; root = root.treeSiblingNext) {
			root.tree.nextTree = null;
		}
		for (BlossomVNode root = state.nodes[state.nodeNum].treeSiblingNext; root != null; root = root.treeSiblingNext) {
			BlossomVTree startTree = root.tree;
			if (startTree.nextTree != null) {
				// this tree is present in some connected component and has been processed
				// already
				continue;
			}
			double eps = startTree.accumulatedEps;

			startTree.nextTree = startTree;
			BlossomVTree connectedComponentLast = startTree;

			BlossomVTree currentTree = startTree;
			while (true) {
				for (BlossomVTree.TreeEdgeIterator iterator = currentTree.treeEdgeIterator(); iterator.hasNext();) {
					BlossomVTreeEdge currentEdge = iterator.next();
					int dir = iterator.getCurrentDirection();
					BlossomVTree opposite = currentEdge.head[dir];
					double plusPlusEps = KolmogorovWeightedPerfectMatching.INFINITY;
					int dirRev = 1 - dir;

					if (!currentEdge.plusPlusEdges.isEmpty()) {
						plusPlusEps = currentEdge.plusPlusEdges.findMin().getKey() - currentTree.eps - opposite.eps;
						if (augmentEps > plusPlusEps) {
							augmentEps = plusPlusEps;
							augmentEdge = currentEdge.plusPlusEdges.findMin().getValue();
						}
					}
					if (opposite.nextTree != null && opposite.nextTree != dummyTree) {
						// opposite tree is in the same connected component
						// since the trees in the same connected component have the same dual change
						// we don't have to check (-, +) edges in this tree edge
						if (2 * eps > plusPlusEps) {
							eps = plusPlusEps / 2;
						}
						continue;
					}

					double[] plusMinusEps = new double[2];
					plusMinusEps[dir] = KolmogorovWeightedPerfectMatching.INFINITY;
					if (!currentEdge.getCurrentPlusMinusHeap(dir).isEmpty()) {
						plusMinusEps[dir] = currentEdge.getCurrentPlusMinusHeap(dir).findMin().getKey()
								- currentTree.eps + opposite.eps;
					}
					plusMinusEps[dirRev] = KolmogorovWeightedPerfectMatching.INFINITY;
					if (!currentEdge.getCurrentPlusMinusHeap(dirRev).isEmpty()) {
						plusMinusEps[dirRev] = currentEdge.getCurrentPlusMinusHeap(dirRev).findMin().getKey()
								- opposite.eps + currentTree.eps;
					}
					if (opposite.nextTree == dummyTree) {
						// opposite tree is in another connected component and has valid accumulated
						// eps
						oppositeEps = opposite.accumulatedEps;
					} else if (plusMinusEps[0] > 0 && plusMinusEps[1] > 0) {
						// this tree edge doesn't contain any tight (-, +) cross-tree edge and
						// opposite tree
						// hasn't been processed yet.
						oppositeEps = 0;
					} else {
						// opposite hasn't been processed and there is a tight (-, +) cross-tree
						// edge between
						// current tree and opposite tree => we add opposite to the current
						// connected component
						connectedComponentLast.nextTree = opposite;
						connectedComponentLast = opposite.nextTree = opposite;
						if (eps > opposite.accumulatedEps) {
							// eps of the connected component can't be greater than the minimum
							// accumulated eps among trees in the connected component
							eps = opposite.accumulatedEps;
						}
						continue;
					}
					if (eps > plusPlusEps - oppositeEps) {
						// eps is bounded by the resulting slack of a (+, +) cross-tree edge
						eps = plusPlusEps - oppositeEps;
					}
					if (eps > plusMinusEps[dir] + oppositeEps) {
						// eps is bounded by the resulting slack of a (+, -) cross-tree edge in the
						// current direction
						eps = plusMinusEps[dir] + oppositeEps;
					}
				}
				if (currentTree.nextTree == currentTree) {
					// the end of the connected component
					break;
				}
				currentTree = currentTree.nextTree;
			}

			if (eps > KolmogorovWeightedPerfectMatching.NO_PERFECT_MATCHING_THRESHOLD) {
				throw new IllegalArgumentException(KolmogorovWeightedPerfectMatching.NO_PERFECT_MATCHING);
			}

			// apply dual change to all trees in the connected component
			BlossomVTree nextTree = startTree;
			do {
				currentTree = nextTree;
				nextTree = nextTree.nextTree;
				currentTree.nextTree = dummyTree;
				currentTree.accumulatedEps = eps;
			} while (currentTree != nextTree);
		}
		if (augmentEdge != null && augmentEps - augmentEdge.head[0].tree.accumulatedEps
				- augmentEdge.head[1].tree.accumulatedEps <= 0) {
			return augmentEdge;
		}
		return null;
	}

	private BlossomVEdge multipleTreeFixedDelta() {
		BlossomVEdge augmentEdge = null;
		double eps = KolmogorovWeightedPerfectMatching.INFINITY;
		double augmentEps = KolmogorovWeightedPerfectMatching.INFINITY;
		for (BlossomVNode root = state.nodes[state.nodeNum].treeSiblingNext; root != null; root = root.treeSiblingNext) {
			BlossomVTree tree = root.tree;
			double treeEps = tree.eps;
			eps = Math.min(eps, tree.accumulatedEps);
			// iterate only through outgoing tree edges so that every edge is considered
			// only once
			for (BlossomVTreeEdge outgoingTreeEdge = tree.first[0]; outgoingTreeEdge != null; outgoingTreeEdge = outgoingTreeEdge.next[0]) {
				// since all epsilons are equal we don't have to check (+, -) cross tree edges
				if (!outgoingTreeEdge.plusPlusEdges.isEmpty()) {
					BlossomVEdge varEdge = outgoingTreeEdge.plusPlusEdges.findMin().getValue();
					double slack = varEdge.slack - treeEps - outgoingTreeEdge.head[0].eps;
					eps = Math.min(eps, slack / 2);
					if (augmentEps > slack) {
						augmentEps = slack;
						augmentEdge = varEdge;
					}
				}
			}
		}
		if (eps > KolmogorovWeightedPerfectMatching.NO_PERFECT_MATCHING_THRESHOLD) {
			throw new IllegalArgumentException(KolmogorovWeightedPerfectMatching.NO_PERFECT_MATCHING);
		}
		for (BlossomVNode root = state.nodes[state.nodeNum].treeSiblingNext; root != null; root = root.treeSiblingNext) {
			root.tree.accumulatedEps = eps;
		}
		if (augmentEps <= 2 * eps) {
			return augmentEdge;
		}
		return null;
	}

}
