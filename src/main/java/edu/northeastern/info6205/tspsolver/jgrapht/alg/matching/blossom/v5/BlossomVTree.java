package edu.northeastern.info6205.tspsolver.jgrapht.alg.matching.blossom.v5;

import edu.northeastern.info6205.tspsolver.jheaps.MergeableAddressableHeap;
import edu.northeastern.info6205.tspsolver.jheaps.tree.PairingHeap;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class BlossomVTree {

	private static int currentId = 1;

	BlossomVTreeEdge[] first;

	BlossomVTreeEdge currentEdge;

	int currentDirection;

	double eps;

	double accumulatedEps;

	BlossomVNode root;

	BlossomVTree nextTree;

	MergeableAddressableHeap<Double, BlossomVEdge> plusPlusEdges;

	MergeableAddressableHeap<Double, BlossomVEdge> plusInfinityEdges;

	MergeableAddressableHeap<Double, BlossomVNode> minusBlossoms;

	int id;

	public BlossomVTree() {
	}

	public BlossomVTree(BlossomVNode root) {
		this.root = root;
		root.tree = this;
		root.isTreeRoot = true;
		first = new BlossomVTreeEdge[2];
		plusPlusEdges = new PairingHeap<>();
		plusInfinityEdges = new PairingHeap<>();
		minusBlossoms = new PairingHeap<>();
		this.id = currentId++;
	}

	public static BlossomVTreeEdge addTreeEdge(BlossomVTree from, BlossomVTree to) {
		BlossomVTreeEdge treeEdge = new BlossomVTreeEdge();

		treeEdge.head[0] = to;
		treeEdge.head[1] = from;

		if (from.first[0] != null) {
			from.first[0].prev[0] = treeEdge;
		}
		if (to.first[1] != null) {
			to.first[1].prev[1] = treeEdge;
		}

		treeEdge.next[0] = from.first[0];
		treeEdge.next[1] = to.first[1];

		from.first[0] = treeEdge;
		to.first[1] = treeEdge;

		to.currentEdge = treeEdge;
		to.currentDirection = 0;
		return treeEdge;
	}

	public void setCurrentEdges() {
		BlossomVTreeEdge treeEdge;
		for (TreeEdgeIterator iterator = treeEdgeIterator(); iterator.hasNext();) {
			treeEdge = iterator.next();
			BlossomVTree opposite = treeEdge.head[iterator.getCurrentDirection()];
			opposite.currentEdge = treeEdge;
			opposite.currentDirection = iterator.getCurrentDirection();
		}
	}

	public void clearCurrentEdges() {
		currentEdge = null;
		for (TreeEdgeIterator iterator = treeEdgeIterator(); iterator.hasNext();) {
			iterator.next().head[iterator.getCurrentDirection()].currentEdge = null;
		}
	}

	public void printTreeNodes() {
		System.out.println("Printing tree nodes");
		for (TreeNodeIterator iterator = treeNodeIterator(); iterator.hasNext();) {
			System.out.println(iterator.next());
		}
	}

	@Override
	public String toString() {
		return "BlossomVTree pos=" + id + ", eps = " + eps + ", root = " + root;
	}

	public void addPlusPlusEdge(BlossomVEdge edge) {
		edge.handle = plusPlusEdges.insert(edge.slack, edge);
	}

	public void addPlusInfinityEdge(BlossomVEdge edge) {
		edge.handle = plusInfinityEdges.insert(edge.slack, edge);
	}

	public void addMinusBlossom(BlossomVNode blossom) {
		blossom.handle = minusBlossoms.insert(blossom.dual, blossom);
	}

	public void removePlusPlusEdge(BlossomVEdge edge) {
		edge.handle.delete();
	}

	public void removePlusInfinityEdge(BlossomVEdge edge) {
		edge.handle.delete();
	}

	public void removeMinusBlossom(BlossomVNode blossom) {
		blossom.handle.delete();
	}

	public TreeNodeIterator treeNodeIterator() {
		return new TreeNodeIterator(root);
	}

	public TreeEdgeIterator treeEdgeIterator() {
		return new TreeEdgeIterator();
	}

	public static class TreeNodeIterator implements Iterator<BlossomVNode> {

		private BlossomVNode currentNode;

		private BlossomVNode current;

		private BlossomVNode treeRoot;

		public TreeNodeIterator(BlossomVNode root) {
			this.currentNode = this.current = root;
			this.treeRoot = root;
		}

		@Override
		public boolean hasNext() {
			if (current != null) {
				return true;
			}
			current = advance();
			return current != null;
		}

		@Override
		public BlossomVNode next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			BlossomVNode result = current;
			current = null;
			return result;
		}

		private BlossomVNode advance() {
			if (currentNode == null) {
				return null;
			} else if (currentNode.firstTreeChild != null) {
				// advance deeper
				currentNode = currentNode.firstTreeChild;
				return currentNode;
			} else {
				// advance to the next unvisited sibling of the current node or
				// of some of its ancestors
				while (currentNode != treeRoot && currentNode.treeSiblingNext == null) {
					currentNode = currentNode.parentEdge.getOpposite(currentNode);
				}
				currentNode = currentNode.treeSiblingNext;
				if (currentNode == treeRoot.treeSiblingNext) {
					currentNode = null;
				}
				return currentNode;
			}
		}
	}

	public class TreeEdgeIterator implements Iterator<BlossomVTreeEdge> {

		private int currentDirection;

		private BlossomVTreeEdge currentEdge;

		private BlossomVTreeEdge result;

		public TreeEdgeIterator() {
			currentEdge = first[0];
			currentDirection = 0;
			if (currentEdge == null) {
				currentEdge = first[1];
				currentDirection = 1;
			}
			result = currentEdge;
		}

		@Override
		public boolean hasNext() {
			if (result != null) {
				return true;
			}
			result = advance();
			return result != null;
		}

		@Override
		public BlossomVTreeEdge next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			BlossomVTreeEdge res = result;
			result = null;
			return res;
		}

		public int getCurrentDirection() {
			return currentDirection;
		}

		private BlossomVTreeEdge advance() {
			if (currentEdge == null) {
				return null;
			}
			currentEdge = currentEdge.next[currentDirection];
			if (currentEdge == null && currentDirection == 0) {
				currentDirection = 1;
				currentEdge = first[1];
			}
			return currentEdge;
		}
	}
}
