package edu.northeastern.info6205.tspsolver.jgrapht.alg.matching.blossom.v5;

import edu.northeastern.info6205.tspsolver.jheaps.AddressableHeap;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class BlossomVEdge {
	
	final int pos;
	
	AddressableHeap.Handle<Double, BlossomVEdge> handle;
	
	double slack;
	
	BlossomVNode[] headOriginal;
	
	BlossomVNode[] head;
	
	BlossomVEdge[] prev;
	
	BlossomVEdge[] next;

	public BlossomVEdge(int pos) {
		headOriginal = new BlossomVNode[2];
		head = new BlossomVNode[2];
		next = new BlossomVEdge[2];
		prev = new BlossomVEdge[2];
		this.pos = pos;
	}

	public BlossomVNode getOpposite(BlossomVNode endpoint) {
		if (endpoint != head[0] && endpoint != head[1]) { // we need this check during finishing
															// phase
			return null;
		}
		return head[0] == endpoint ? head[1] : head[0];
	}

	public BlossomVNode getCurrentOriginal(BlossomVNode endpoint) {
		if (endpoint != head[0] && endpoint != head[1]) { // we need this check during finishing
															// phase
			return null;
		}
		return head[0] == endpoint ? headOriginal[0] : headOriginal[1];
	}

	public int getDirFrom(BlossomVNode current) {
		return head[0] == current ? 1 : 0;
	}

	@Override
	public String toString() {
		return "BlossomVEdge (" + head[0].pos + "," + head[1].pos + "), original: [" + headOriginal[0].pos + ","
				+ headOriginal[1].pos + "], slack: " + slack + ", true slack: " + getTrueSlack()
				+ (getTrueSlack() == 0 ? ", tight" : "");
	}

	public double getTrueSlack() {
		double result = slack;

		if (head[0].tree != null) {
			if (head[0].isPlusNode()) {
				result -= head[0].tree.eps;
			} else {
				result += head[0].tree.eps;
			}
		}
		if (head[1].tree != null) {
			if (head[1].isPlusNode()) {
				result -= head[1].tree.eps;
			} else {
				result += head[1].tree.eps;
			}
		}
		return result;

	}

	public void moveEdgeTail(BlossomVNode from, BlossomVNode to) {
		int dir = getDirFrom(from);
		from.removeEdge(this, dir);
		to.addEdge(this, dir);
	}

	public BlossomNodesIterator blossomNodesIterator(BlossomVNode root) {
		return new BlossomNodesIterator(root, this);
	}

	public static class BlossomNodesIterator implements Iterator<BlossomVNode> {
		
		private BlossomVNode root;
		
		private BlossomVNode currentNode;
		
		private BlossomVNode current;
		
		private int currentDirection;
		
		private BlossomVEdge blossomFormingEdge;

		public BlossomNodesIterator(BlossomVNode root, BlossomVEdge blossomFormingEdge) {
			this.root = root;
			this.blossomFormingEdge = blossomFormingEdge;
			currentNode = current = blossomFormingEdge.head[0];
			currentDirection = 0;
		}

		@Override
		public boolean hasNext() {
			if (current != null) {
				return true;
			}
			current = advance();
			return current != null;
		}

		public int getCurrentDirection() {
			return currentDirection;
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
			}
			if (currentNode == root && currentDirection == 0) {
				// we have just traversed blossom's root and now start to traverse the second
				// branch
				currentDirection = 1;
				currentNode = blossomFormingEdge.head[1];
				if (currentNode == root) {
					currentNode = null;
				}
			} else if (currentNode.getTreeParent() == root && currentDirection == 1) {
				// we have just finished traversing the blossom's nodes
				currentNode = null;
			} else {
				currentNode = currentNode.getTreeParent();
			}
			return currentNode;
		}
	}
}
