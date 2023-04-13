package edu.northeastern.info6205.tspsolver.jgrapht.alg.matching.blossom.v5;

import edu.northeastern.info6205.tspsolver.jheaps.AddressableHeap;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class BlossomVNode {
	
	AddressableHeap.Handle<Double, BlossomVNode> handle;
	
	boolean isTreeRoot;
	
	boolean isBlossom;
	
	boolean isOuter;
	
	boolean isProcessed;
	
	boolean isMarked;

	Label label;
	
	BlossomVEdge[] first;
	
	double dual;
	
	BlossomVEdge matched;
	
	BlossomVEdge bestEdge;
	
	BlossomVTree tree;
	
	BlossomVEdge parentEdge;
	
	BlossomVNode firstTreeChild;

	BlossomVNode treeSiblingNext;
	
	BlossomVNode treeSiblingPrev;

	BlossomVNode blossomParent;
	
	BlossomVNode blossomGrandparent;
	
	BlossomVEdge blossomSibling;
	
	int pos;

	public BlossomVNode(int pos) {
		this.first = new BlossomVEdge[2];
		this.label = Label.PLUS;
		this.pos = pos;
	}

	public void addEdge(BlossomVEdge edge, int dir) {
		if (first[dir] == null) {
			// the list in the direction dir is empty
			first[dir] = edge.next[dir] = edge.prev[dir] = edge;
		} else {
			// the list in the direction dir isn't empty
			// append this edge to the end of the linked list
			edge.prev[dir] = first[dir].prev[dir];
			edge.next[dir] = first[dir];
			first[dir].prev[dir].next[dir] = edge;
			first[dir].prev[dir] = edge;
		}
		
		edge.head[1 - dir] = this;
	}

	public void removeEdge(BlossomVEdge edge, int dir) {
		if (edge.prev[dir] == edge) {
			// it is the only edge of this node in the direction dir
			first[dir] = null;
		} else {
			// remove edge from the linked list
			edge.prev[dir].next[dir] = edge.next[dir];
			edge.next[dir].prev[dir] = edge.prev[dir];
			if (first[dir] == edge) {
				first[dir] = edge.next[dir];
			}
		}
	}

	public BlossomVNode getTreeGrandparent() {
		BlossomVNode t = parentEdge.getOpposite(this);
		return t.parentEdge.getOpposite(t);
	}

	public BlossomVNode getTreeParent() {
		return parentEdge == null ? null : parentEdge.getOpposite(this);
	}

	public void addChild(BlossomVNode child, BlossomVEdge parentEdge, boolean grow) {
		child.parentEdge = parentEdge;
		child.tree = tree;
		child.treeSiblingNext = firstTreeChild;
		if (grow) {
			// with this check we are able to avoid destroying the tree structure during the
			// augment
			// operation
			child.firstTreeChild = null;
		}
		if (firstTreeChild == null) {
			child.treeSiblingPrev = child;
		} else {
			child.treeSiblingPrev = firstTreeChild.treeSiblingPrev;
			firstTreeChild.treeSiblingPrev = child;
		}
		firstTreeChild = child;
	}

	public BlossomVNode getOppositeMatched() {
		return matched.getOpposite(this);
	}

	public void removeFromChildList() {
		if (isTreeRoot) {
			treeSiblingPrev.treeSiblingNext = treeSiblingNext;
			if (treeSiblingNext != null) {
				treeSiblingNext.treeSiblingPrev = treeSiblingPrev;
			}
		} else {
			if (treeSiblingPrev.treeSiblingNext == null) {
				// this vertex is the first child => we have to update parent.firstTreeChild
				parentEdge.getOpposite(this).firstTreeChild = treeSiblingNext;
			} else {
				// this vertex isn't the first child
				treeSiblingPrev.treeSiblingNext = treeSiblingNext;
			}
			if (treeSiblingNext == null) {
				// this vertex is the last child => we have to set treeSiblingPrev of the
				// firstChild
				if (parentEdge.getOpposite(this).firstTreeChild != null) {
					parentEdge.getOpposite(this).firstTreeChild.treeSiblingPrev = treeSiblingPrev;
				}
			} else {
				// this vertex isn't the last child
				treeSiblingNext.treeSiblingPrev = treeSiblingPrev;
			}
		}
	}

	public void moveChildrenTo(BlossomVNode blossom) {
		if (firstTreeChild != null) {
			if (blossom.firstTreeChild == null) {
				blossom.firstTreeChild = firstTreeChild;
			} else {
				BlossomVNode t = blossom.firstTreeChild.treeSiblingPrev;
				// concatenating child lists
				firstTreeChild.treeSiblingPrev.treeSiblingNext = blossom.firstTreeChild;
				blossom.firstTreeChild.treeSiblingPrev = firstTreeChild.treeSiblingPrev;
				// setting reference to the last child and updating firstTreeChild reference of
				// the
				// blossom
				firstTreeChild.treeSiblingPrev = t;
				blossom.firstTreeChild = firstTreeChild;
			}
			firstTreeChild = null; // now this node has no children
		}
	}

	public BlossomVNode getPenultimateBlossom() {
		BlossomVNode current = this;
		while (true) {
			if (!current.blossomGrandparent.isOuter) {
				current = current.blossomGrandparent;
			} else if (current.blossomGrandparent != current.blossomParent) {
				// this is the case when current.blossomGrandparent has been removed
				current.blossomGrandparent = current.blossomParent;
			} else {
				break;
			}
		}
		
		BlossomVNode prev = this;
		BlossomVNode next;
		while (prev != current) {
			next = prev.blossomGrandparent;
			prev.blossomGrandparent = current; // apply path compression
			prev = next;
		}

		return current;
	}

	public BlossomVNode getPenultimateBlossomAndFixBlossomGrandparent() {
		BlossomVNode current = this;
		BlossomVNode prev = null;
		while (true) {
			if (!current.blossomGrandparent.isOuter) {
				prev = current;
				current = current.blossomGrandparent;
			} else if (current.blossomGrandparent != current.blossomParent) {
				// this is the case when current.blossomGrandparent has been removed
				current.blossomGrandparent = current.blossomParent;
			} else {
				break;
			}
		}
		
		if (prev != null) {
			BlossomVNode prevNode = this;
			BlossomVNode nextNode;
			while (prevNode != prev) {
				nextNode = prevNode.blossomGrandparent;
				prevNode.blossomGrandparent = prev;
				prevNode = nextNode;
			}
		}
		return current;
	}

	public boolean isPlusNode() {
		return label == Label.PLUS;
	}

	public boolean isMinusNode() {
		return label == Label.MINUS;
	}

	public boolean isInfinityNode() {
		return label == Label.INFINITY;
	}

	public double getTrueDual() {
		if (isInfinityNode() || !isOuter) {
			return dual;
		}
		return isPlusNode() ? dual + tree.eps : dual - tree.eps;
	}

	public IncidentEdgeIterator incidentEdgesIterator() {
		return new IncidentEdgeIterator();
	}

	@Override
	public String toString() {
		return "BlossomVNode pos = " + pos + ", dual: " + dual + ", true dual: " + getTrueDual() + ", label: " + label
				+ (isMarked ? ", marked" : "") + (isProcessed ? ", processed" : "")
				+ (blossomParent == null || isOuter ? "" : ", blossomParent = " + blossomParent.pos)
				+ (matched == null ? "" : ", matched = " + matched);
	}

	public enum Label {
		PLUS,
		MINUS,
		INFINITY
	}

	public class IncidentEdgeIterator implements Iterator<BlossomVEdge> {

		private int currentDir;
		
		private int nextDir;
		
		private BlossomVEdge nextEdge;

		public IncidentEdgeIterator() {
			nextDir = first[0] == null ? 1 : 0;
			nextEdge = first[nextDir];
		}

		public int getDir() {
			return currentDir;
		}

		@Override
		public boolean hasNext() {
			return nextEdge != null;
		}

		@Override
		public BlossomVEdge next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			BlossomVEdge result = nextEdge;
			advance();
			return result;
		}

		private void advance() {
			currentDir = nextDir;
			nextEdge = nextEdge.next[nextDir];
			if (nextEdge == first[0]) {
				nextEdge = first[1];
				nextDir = 1;
			} else if (nextEdge == first[1]) {
				nextEdge = null;
			}
		}
	}
}
