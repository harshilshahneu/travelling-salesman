package edu.northeastern.info6205.tspsolver.jgrapht.alg.matching.blossom.v5;

import edu.northeastern.info6205.tspsolver.jgrapht.Graph;
import edu.northeastern.info6205.tspsolver.jgrapht.util.CollectionUtil;
import edu.northeastern.info6205.tspsolver.jheaps.AddressableHeap;
import edu.northeastern.info6205.tspsolver.jheaps.tree.PairingHeap;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class BlossomVInitializer<V, E> {
	
	private final Graph<V, E> graph;
	
	private int nodeNum;
	
	private int edgeNum = 0;
	
	private BlossomVNode[] nodes;
	
	private BlossomVEdge[] edges;
	
	private List<V> graphVertices;
	
	private List<E> graphEdges;

	public BlossomVInitializer(Graph<V, E> graph) {
		this.graph = graph;
		nodeNum = graph.vertexSet().size();
	}

	public BlossomVState<V, E> initialize(BlossomVOptions options) {
		switch (options.initializationType) {
		case NONE:
			return simpleInitialization(options);
		case GREEDY:
			return greedyInitialization(options);
		case FRACTIONAL:
			return fractionalMatchingInitialization(options);
		default:
			return null;
		}
	}

	private BlossomVState<V, E> simpleInitialization(BlossomVOptions options) {
		double minEdgeWeight = initGraph();
		for (BlossomVNode node : nodes) {
			node.isOuter = true;
		}
		allocateTrees();
		initAuxiliaryGraph();
		return new BlossomVState<>(graph, nodes, edges, nodeNum, edgeNum, nodeNum, graphVertices, graphEdges, options,
				minEdgeWeight);
	}

	private BlossomVState<V, E> greedyInitialization(BlossomVOptions options) {
		double minEdgeWeight = initGraph();
		int treeNum = initGreedy();
		allocateTrees();
		initAuxiliaryGraph();
		return new BlossomVState<>(graph, nodes, edges, nodeNum, edgeNum, treeNum, graphVertices, graphEdges, options,
				minEdgeWeight);
	}

	private BlossomVState<V, E> fractionalMatchingInitialization(BlossomVOptions options) {
		double minEdgeWeight = initGraph();
		initGreedy();
		allocateTrees();
		int treeNum = initFractional();
		initAuxiliaryGraph();
		return new BlossomVState<>(graph, nodes, edges, nodeNum, edgeNum, treeNum, graphVertices, graphEdges, options,
				minEdgeWeight);
	}

	private double initGraph() {
		int expectedEdgeNum = graph.edgeSet().size();
		nodes = new BlossomVNode[nodeNum + 1];
		edges = new BlossomVEdge[expectedEdgeNum];
		graphVertices = new ArrayList<>(nodeNum);
		graphEdges = new ArrayList<>(expectedEdgeNum);
		HashMap<V, BlossomVNode> vertexMap = CollectionUtil.newHashMapWithExpectedSize(nodeNum);
		int i = 0;
		// maps nodes
		for (V vertex : graph.vertexSet()) {
			nodes[i] = new BlossomVNode(i);
			graphVertices.add(vertex);
			vertexMap.put(vertex, nodes[i]);
			i++;
		}
		nodes[nodeNum] = new BlossomVNode(nodeNum); // auxiliary node to keep track of the first
													// item in the linked list of tree roots
		i = 0;
		double minEdgeWeight = graph.edgeSet().stream().map(graph::getEdgeWeight).min(Comparator.naturalOrder())
				.orElse(0d);
		// maps edges
		for (E e : graph.edgeSet()) {
			BlossomVNode source = vertexMap.get(graph.getEdgeSource(e));
			BlossomVNode target = vertexMap.get(graph.getEdgeTarget(e));
			if (source != target) { // we avoid self-loops in order to support pseudographs
				edgeNum++;
				BlossomVEdge edge = addEdge(source, target, graph.getEdgeWeight(e) - minEdgeWeight, i);
				edges[i] = edge;
				graphEdges.add(e);
				i++;
			}
		}
		return minEdgeWeight;
	}

	public BlossomVEdge addEdge(BlossomVNode from, BlossomVNode to, double slack, int pos) {
		BlossomVEdge edge = new BlossomVEdge(pos);
		edge.slack = slack;
		edge.headOriginal[0] = to;
		edge.headOriginal[1] = from;
		// the call to the BlossomVNode#addEdge implies setting head[dir] reference
		from.addEdge(edge, 0);
		to.addEdge(edge, 1);
		return edge;
	}

	private int initGreedy() {
		// set all dual variables to infinity
		for (int i = 0; i < nodeNum; i++) {
			nodes[i].dual = KolmogorovWeightedPerfectMatching.INFINITY;
		}
		// set dual variables to half of the minimum weight of the incident edges
		for (int i = 0; i < edgeNum; i++) {
			BlossomVEdge edge = edges[i];
			if (edge.head[0].dual > edge.slack) {
				edge.head[0].dual = edge.slack;
			}
			if (edge.head[1].dual > edge.slack) {
				edge.head[1].dual = edge.slack;
			}
		}
		// divide dual variables by two; this ensures nonnegativity of all slacks;
		// decrease edge slacks accordingly
		for (int i = 0; i < edgeNum; i++) {
			BlossomVEdge edge = edges[i];
			BlossomVNode source = edge.head[0];
			BlossomVNode target = edge.head[1];
			if (!source.isOuter) {
				source.isOuter = true;
				source.dual /= 2;
			}
			edge.slack -= source.dual;
			if (!target.isOuter) {
				target.isOuter = true;
				target.dual /= 2;
			}
			edge.slack -= target.dual;
		}
		// go through all vertices, greedily increase their dual variables to the
		// minimum slack of
		// incident edges;
		// if there exists a tight unmatched edge in the neighborhood, match it
		int treeNum = nodeNum;
		for (int i = 0; i < nodeNum; i++) {
			BlossomVNode node = nodes[i];
			if (!node.isInfinityNode()) {
				double minSlack = KolmogorovWeightedPerfectMatching.INFINITY;
				// find the minimum slack of incident edges
				for (BlossomVNode.IncidentEdgeIterator incidentEdgeIterator = node
						.incidentEdgesIterator(); incidentEdgeIterator.hasNext();) {
					BlossomVEdge edge = incidentEdgeIterator.next();
					if (edge.slack < minSlack) {
						minSlack = edge.slack;
					}
				}
				node.dual += minSlack;
				double resultMinSlack = minSlack;
				// subtract minimum slack from the slacks of all incident edges
				for (BlossomVNode.IncidentEdgeIterator incidentEdgeIterator = node
						.incidentEdgesIterator(); incidentEdgeIterator.hasNext();) {
					BlossomVEdge edge = incidentEdgeIterator.next();
					int dir = incidentEdgeIterator.getDir();
					if (edge.slack <= resultMinSlack && node.isPlusNode() && edge.head[dir].isPlusNode()) {
						node.label = BlossomVNode.Label.INFINITY;
						edge.head[dir].label = BlossomVNode.Label.INFINITY;
						node.matched = edge;
						edge.head[dir].matched = edge;
						treeNum -= 2;
					}
					edge.slack -= resultMinSlack;
				}
			}
		}

		return treeNum;
	}

	private void initAuxiliaryGraph() {
		// go through all tree roots and visit all incident edges of those roots.
		// if a (+, inf) edge is encountered => add it to the infinity heap
		// if a (+, +) edge is encountered and the opposite node hasn't been processed
		// yet =>
		// add this edge to the heap of (+, +) cross-tree edges
		for (BlossomVNode root = nodes[nodeNum].treeSiblingNext; root != null; root = root.treeSiblingNext) {
			BlossomVTree tree = root.tree;
			for (BlossomVNode.IncidentEdgeIterator edgeIterator = root.incidentEdgesIterator(); edgeIterator
					.hasNext();) {
				BlossomVEdge edge = edgeIterator.next();
				BlossomVNode opposite = edge.head[edgeIterator.getDir()];
				if (opposite.isInfinityNode()) {
					tree.addPlusInfinityEdge(edge);
				} else if (!opposite.isProcessed) {
					if (opposite.tree.currentEdge == null) {
						BlossomVTree.addTreeEdge(tree, opposite.tree);
					}
					opposite.tree.currentEdge.addPlusPlusEdge(edge);
				}
			}
			root.isProcessed = true;
			for (BlossomVTree.TreeEdgeIterator treeEdgeIterator = tree.treeEdgeIterator(); treeEdgeIterator
					.hasNext();) {
				BlossomVTreeEdge treeEdge = treeEdgeIterator.next();
				treeEdge.head[treeEdgeIterator.getCurrentDirection()].currentEdge = null;
			}
		}
		// clear isProcessed flags
		for (BlossomVNode root = nodes[nodeNum].treeSiblingNext; root != null; root = root.treeSiblingNext) {
			root.isProcessed = false;
		}
	}

	private void allocateTrees() {
		BlossomVNode lastRoot = nodes[nodeNum];
		for (int i = 0; i < nodeNum; i++) {
			BlossomVNode node = nodes[i];
			if (node.isPlusNode()) {
				node.treeSiblingPrev = lastRoot;
				lastRoot.treeSiblingNext = node;
				lastRoot = node;
				new BlossomVTree(node);
			}
		}
		lastRoot.treeSiblingNext = null;
	}

	private int finish() {
		BlossomVNode prevRoot = nodes[nodeNum];
		int treeNum = 0;
		for (int i = 0; i < nodeNum; i++) {
			BlossomVNode node = nodes[i];
			node.firstTreeChild = node.treeSiblingNext = node.treeSiblingPrev = null;
			if (!node.isOuter) {
				expandInit(node, null); // this node becomes unmatched
				node.parentEdge = null;
				node.label = BlossomVNode.Label.PLUS;
				new BlossomVTree(node);

				prevRoot.treeSiblingNext = node;
				node.treeSiblingPrev = prevRoot;
				prevRoot = node;
				treeNum++;
			}
		}
		return treeNum;
	}

	private void updateDuals(AddressableHeap<Double, BlossomVEdge> heap, BlossomVNode root, double eps) {
		for (BlossomVTree.TreeNodeIterator treeNodeIterator = new BlossomVTree.TreeNodeIterator(root); treeNodeIterator
				.hasNext();) {
			BlossomVNode treeNode = treeNodeIterator.next();
			if (treeNode.isProcessed) {
				treeNode.dual += eps;
				if (!treeNode.isTreeRoot) {
					BlossomVNode minusNode = treeNode.getOppositeMatched();
					minusNode.dual -= eps;
					double delta = eps - treeNode.matched.slack;
					for (BlossomVNode.IncidentEdgeIterator iterator = minusNode.incidentEdgesIterator(); iterator
							.hasNext();) {
						iterator.next().slack += delta;
					}
				}
				for (BlossomVNode.IncidentEdgeIterator iterator = treeNode.incidentEdgesIterator(); iterator
						.hasNext();) {
					iterator.next().slack -= eps;
				}
				treeNode.isProcessed = false;
			}
		}
		// clear bestEdge after dual update
		while (!heap.isEmpty()) {
			BlossomVEdge edge = heap.findMin().getValue();
			BlossomVNode node = edge.head[0].isInfinityNode() ? edge.head[0] : edge.head[1];
			removeFromHeap(node);
		}
	}

	private void addToHead(AddressableHeap<Double, BlossomVEdge> heap, BlossomVNode node, BlossomVEdge bestEdge) {
		bestEdge.handle = heap.insert(bestEdge.slack, bestEdge);
		node.bestEdge = bestEdge;
	}

	private void removeFromHeap(BlossomVNode node) {
		node.bestEdge.handle.delete();
		node.bestEdge.handle = null;
		node.bestEdge = null;
	}

	private BlossomVNode findBlossomRootInit(BlossomVEdge blossomFormingEdge) {
		BlossomVNode[] branches = new BlossomVNode[] { blossomFormingEdge.head[0], blossomFormingEdge.head[1] };
		BlossomVNode root, upperBound; // need to be scoped outside of the loop
		int dir = 0;
		while (true) {
			if (!branches[dir].isOuter) {
				root = branches[dir];
				upperBound = branches[1 - dir];
				break;
			}
			branches[dir].isOuter = false;
			if (branches[dir].isTreeRoot) {
				upperBound = branches[dir];
				BlossomVNode jumpNode = branches[1 - dir];
				while (jumpNode.isOuter) {
					jumpNode.isOuter = false;
					jumpNode = jumpNode.getTreeParent();
					jumpNode.isOuter = false;
					jumpNode = jumpNode.getTreeParent();
				}
				root = jumpNode;
				break;
			}
			BlossomVNode node = branches[dir].getTreeParent();
			node.isOuter = false;
			branches[dir] = node.getTreeParent();
			dir = 1 - dir;
		}
		BlossomVNode jumpNode = root;
		while (jumpNode != upperBound) {
			jumpNode = jumpNode.getTreeParent();
			jumpNode.isOuter = true;
			jumpNode = jumpNode.getTreeParent();
			jumpNode.isOuter = true;
		}
		return root;
	}

	private void handleInfinityEdgeInit(AddressableHeap<Double, BlossomVEdge> heap, BlossomVEdge infinityEdge, int dir,
                                        double eps, double criticalEps) {
		BlossomVNode inTreeNode = infinityEdge.head[1 - dir];
		BlossomVNode oppositeNode = infinityEdge.head[dir];
		if (infinityEdge.slack > eps) { // this edge isn't tight, but this edge can become a best
										// edge
			if (infinityEdge.slack < criticalEps) { // this edge can become a best edge
				if (oppositeNode.bestEdge == null) { // inTreeNode hadn't had any best edge before
					addToHead(heap, oppositeNode, infinityEdge);
				} else {
					if (infinityEdge.slack < oppositeNode.bestEdge.slack) {
						removeFromHeap(oppositeNode);
						addToHead(heap, oppositeNode, infinityEdge);
					}
				}
			}
		} else {
			// this is a tight edge, can grow it
			if (oppositeNode.bestEdge != null) {
				removeFromHeap(oppositeNode);
			}
			oppositeNode.label = BlossomVNode.Label.MINUS;
			inTreeNode.addChild(oppositeNode, infinityEdge, true);

			BlossomVNode plusNode = oppositeNode.matched.getOpposite(oppositeNode);
			if (plusNode.bestEdge != null) {
				removeFromHeap(plusNode);
			}
			plusNode.label = BlossomVNode.Label.PLUS;
			oppositeNode.addChild(plusNode, plusNode.matched, true);
		}
	}

	private void augmentBranchInit(BlossomVNode treeRoot, BlossomVNode branchStart, BlossomVEdge augmentEdge) {
		for (BlossomVTree.TreeNodeIterator iterator = new BlossomVTree.TreeNodeIterator(treeRoot); iterator
				.hasNext();) {
			iterator.next().label = BlossomVNode.Label.INFINITY;
		}

		BlossomVNode plusNode = branchStart;
		BlossomVNode minusNode = branchStart.getTreeParent();
		BlossomVEdge matchedEdge = augmentEdge;
		// alternate the matching from branch start up to the tree root
		while (minusNode != null) {
			plusNode.matched = matchedEdge;
			minusNode.matched = matchedEdge = minusNode.parentEdge;
			plusNode = minusNode.getTreeParent();
			minusNode = plusNode.getTreeParent();
		}
		treeRoot.matched = matchedEdge;

		treeRoot.removeFromChildList();
		treeRoot.isTreeRoot = false;
	}

	private void shrinkInit(BlossomVEdge blossomFormingEdge, BlossomVNode treeRoot) {
		for (BlossomVTree.TreeNodeIterator iterator = new BlossomVTree.TreeNodeIterator(treeRoot); iterator
				.hasNext();) {
			iterator.next().label = BlossomVNode.Label.INFINITY;
		}
		BlossomVNode blossomRoot = findBlossomRootInit(blossomFormingEdge);

		// alternate the matching from blossom root up to the tree root
		if (!blossomRoot.isTreeRoot) {
			BlossomVNode minusNode = blossomRoot.getTreeParent();
			BlossomVEdge prevEdge = minusNode.parentEdge;
			minusNode.matched = minusNode.parentEdge;
			BlossomVNode plusNode = minusNode.getTreeParent();
			while (plusNode != treeRoot) {
				minusNode = plusNode.getTreeParent();
				plusNode.matched = prevEdge;
				minusNode.matched = prevEdge = minusNode.parentEdge;
				plusNode = minusNode.getTreeParent();
			}
			plusNode.matched = prevEdge;
		}

		// set the circular blossomSibling references
		BlossomVEdge prevEdge = blossomFormingEdge;
		for (BlossomVEdge.BlossomNodesIterator iterator = blossomFormingEdge.blossomNodesIterator(blossomRoot); iterator
				.hasNext();) {
			BlossomVNode current = iterator.next();
			current.label = BlossomVNode.Label.PLUS;
			if (iterator.getCurrentDirection() == 0) {
				current.blossomSibling = prevEdge;
				prevEdge = current.parentEdge;
			} else {
				current.blossomSibling = current.parentEdge;
			}
		}
		treeRoot.removeFromChildList();
		treeRoot.isTreeRoot = false;

	}

	private void expandInit(BlossomVNode blossomNode, BlossomVEdge blossomNodeMatched) {
		BlossomVNode currentNode = blossomNode.blossomSibling.getOpposite(blossomNode);

		blossomNode.isOuter = true;
		blossomNode.label = BlossomVNode.Label.INFINITY;
		blossomNode.matched = blossomNodeMatched;
		// change the matching in the blossom
		do {
			currentNode.matched = currentNode.blossomSibling;
			BlossomVEdge prevEdge = currentNode.blossomSibling;
			currentNode.isOuter = true;
			currentNode.label = BlossomVNode.Label.INFINITY;
			currentNode = currentNode.blossomSibling.getOpposite(currentNode);

			currentNode.matched = prevEdge;
			currentNode.isOuter = true;
			currentNode.label = BlossomVNode.Label.INFINITY;
			currentNode = currentNode.blossomSibling.getOpposite(currentNode);
		} while (currentNode != blossomNode);
	}

	private int initFractional() {
		
		AddressableHeap<Double, BlossomVEdge> heap = new PairingHeap<>();

		for (BlossomVNode root = nodes[nodeNum].treeSiblingNext; root != null;) {
			BlossomVNode root2 = root.treeSiblingNext;
			BlossomVNode root3 = null;
			if (root2 != null) {
				root3 = root2.treeSiblingNext;
			}
			BlossomVNode currentNode = root;

			heap.clear();

			double branchEps = 0;
			Action flag = Action.NONE;
			BlossomVNode branchRoot = currentNode;
			BlossomVEdge criticalEdge = null;
			
			double criticalEps = KolmogorovWeightedPerfectMatching.INFINITY;
			int criticalDir = -1;
			boolean primalOperation = false;

			
			while (true) {
				currentNode.isProcessed = true;
				currentNode.dual -= branchEps; // apply lazy delta spreading

				if (!currentNode.isTreeRoot) {
					// apply lazy delta spreading to the matched "-" node
					currentNode.getOppositeMatched().dual += branchEps;
				}

				// Process edges incident to the current node
				BlossomVNode.IncidentEdgeIterator iterator;
				for (iterator = currentNode.incidentEdgesIterator(); iterator.hasNext();) {
					BlossomVEdge currentEdge = iterator.next();
					int dir = iterator.getDir();

					currentEdge.slack += branchEps; // apply lazy delta spreading
					BlossomVNode oppositeNode = currentEdge.head[dir];

					if (oppositeNode.tree == root.tree) {
						// opposite node is in the same tree
						if (oppositeNode.isPlusNode()) {
							double slack = currentEdge.slack;
							if (!oppositeNode.isProcessed) {
								slack += branchEps;
							}
							if (2 * criticalEps > slack || criticalEdge == null) {
								flag = Action.SHRINK;
								criticalEps = slack / 2;
								criticalEdge = currentEdge;
								criticalDir = dir;
								if (criticalEps <= branchEps) {
									// found a tight (+, +) in-tree edge to shrink => go out of the
									// loop
									primalOperation = true;
									break;
								}
							}
						}

					} else if (oppositeNode.isPlusNode()) {
						// current edge is a (+, +) cross-tree edge
						if (criticalEps >= currentEdge.slack || criticalEdge == null) {
							//
							flag = Action.AUGMENT;
							criticalEps = currentEdge.slack;
							criticalEdge = currentEdge;
							criticalDir = dir;
							if (criticalEps <= branchEps) {
								// found a tight (+, +) cross-tree edge to augment
								primalOperation = true;
								break;
							}
						}

					} else {
						// opposite node is an infinity node since all other trees contain only one
						// "+" node
						handleInfinityEdgeInit(heap, currentEdge, dir, branchEps, criticalEps);
					}
				}
				if (primalOperation) {
					// finish processing incident edges
					while (iterator.hasNext()) {
						iterator.next().slack += branchEps;
					}
					// exit the loop since we can perform shrink or augment operation
					break;
				} else {
					
					if (currentNode.firstTreeChild != null) {
						// move to the next grandchild
						currentNode = currentNode.firstTreeChild.getOppositeMatched();
					} else {
						// try to find another unprocessed node
						while (currentNode != branchRoot && currentNode.treeSiblingNext == null) {
							currentNode = currentNode.getTreeParent();
						}
						if (currentNode.isMinusNode()) {
							// found an unprocessed node
							currentNode = currentNode.treeSiblingNext.getOppositeMatched();
						} else if (currentNode == branchRoot) {
							// we've processed all nodes in the current branch
							BlossomVEdge minSlackEdge = heap.isEmpty() ? null : heap.findMin().getValue();
							if (minSlackEdge == null || minSlackEdge.slack >= criticalEps) {
								// can perform primal operation after updating duals
								if (criticalEps > KolmogorovWeightedPerfectMatching.NO_PERFECT_MATCHING_THRESHOLD) {
									throw new IllegalArgumentException(
											KolmogorovWeightedPerfectMatching.NO_PERFECT_MATCHING);
								}
								branchEps = criticalEps;
								break;
							} else {
								// grow minimum slack edge
								int dirToFreeNode = minSlackEdge.head[0].isInfinityNode() ? 0 : 1;
								currentNode = minSlackEdge.head[1 - dirToFreeNode];

								BlossomVNode minusNode = minSlackEdge.head[dirToFreeNode];
								removeFromHeap(minusNode);
								minusNode.label = BlossomVNode.Label.MINUS;
								currentNode.addChild(minusNode, minSlackEdge, true);
								branchEps = minSlackEdge.slack; // set new eps of the tree

								BlossomVNode plusNode = minusNode.getOppositeMatched();
								if (plusNode.bestEdge != null) {
									removeFromHeap(plusNode);
								}
								plusNode.label = BlossomVNode.Label.PLUS;
								minusNode.addChild(plusNode, minusNode.matched, true);

								// Start a new branch
								currentNode = branchRoot = plusNode;
							}
						}
					}
				}
			}

			// update duals
			updateDuals(heap, root, branchEps);

			// apply primal operation
			BlossomVNode from = criticalEdge.head[1 - criticalDir];
			BlossomVNode to = criticalEdge.head[criticalDir];
			if (flag == Action.SHRINK) {
				shrinkInit(criticalEdge, root);
			} else {
				augmentBranchInit(root, from, criticalEdge);
				if (to.isOuter) {
					// this node doesn't belong to a 1/2-values odd circuit
					augmentBranchInit(to, to, criticalEdge); // to is the root of the opposite tree
				} else {
					// this node belongs to a 1/2-values odd circuit
					expandInit(to, criticalEdge);
				}
			}

			root = root2;
			if (root != null && !root.isTreeRoot) {
				root = root3;
			}
		}

		return finish();
	}

	enum Action {
		NONE, SHRINK, AUGMENT,
	}
}
