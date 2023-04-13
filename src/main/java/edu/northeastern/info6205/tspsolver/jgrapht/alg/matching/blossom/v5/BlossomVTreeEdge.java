package edu.northeastern.info6205.tspsolver.jgrapht.alg.matching.blossom.v5;

import edu.northeastern.info6205.tspsolver.jheaps.MergeableAddressableHeap;
import edu.northeastern.info6205.tspsolver.jheaps.tree.PairingHeap;

public class BlossomVTreeEdge {

	BlossomVTree[] head;

	BlossomVTreeEdge[] prev;

	BlossomVTreeEdge[] next;

	MergeableAddressableHeap<Double, BlossomVEdge> plusPlusEdges;

	MergeableAddressableHeap<Double, BlossomVEdge> plusMinusEdges0;

	MergeableAddressableHeap<Double, BlossomVEdge> plusMinusEdges1;

	public BlossomVTreeEdge() {
		this.head = new BlossomVTree[2];
		this.prev = new BlossomVTreeEdge[2];
		this.next = new BlossomVTreeEdge[2];
		this.plusPlusEdges = new PairingHeap<>();
		this.plusMinusEdges0 = new PairingHeap<>();
		this.plusMinusEdges1 = new PairingHeap<>();
	}

	public void removeFromTreeEdgeList() {
		for (int dir = 0; dir < 2; dir++) {
			if (prev[dir] != null) {
				prev[dir].next[dir] = next[dir];
			} else {
				// this is the first edge in this direction
				head[1 - dir].first[dir] = next[dir];
			}
			if (next[dir] != null) {
				next[dir].prev[dir] = prev[dir];
			}
		}
		head[0] = head[1] = null;
	}

	@Override
	public String toString() {
		return "BlossomVTreeEdge (" + head[0].id + ":" + head[1].id + ")";
	}

	public void addToCurrentMinusPlusHeap(BlossomVEdge edge, int direction) {
		edge.handle = getCurrentMinusPlusHeap(direction).insert(edge.slack, edge);
	}

	public void addToCurrentPlusMinusHeap(BlossomVEdge edge, int direction) {
		edge.handle = getCurrentPlusMinusHeap(direction).insert(edge.slack, edge);
	}

	public void addPlusPlusEdge(BlossomVEdge edge) {
		edge.handle = plusPlusEdges.insert(edge.slack, edge);
	}

	public void removeFromCurrentMinusPlusHeap(BlossomVEdge edge) {
		edge.handle.delete();
		edge.handle = null;
	}

	public void removeFromCurrentPlusMinusHeap(BlossomVEdge edge) {
		edge.handle.delete();
		edge.handle = null;
	}

	public void removeFromPlusPlusHeap(BlossomVEdge edge) {
		edge.handle.delete();
		edge.handle = null;
	}

	public MergeableAddressableHeap<Double, BlossomVEdge> getCurrentMinusPlusHeap(int currentDir) {
		return currentDir == 0 ? plusMinusEdges0 : plusMinusEdges1;
	}

	public MergeableAddressableHeap<Double, BlossomVEdge> getCurrentPlusMinusHeap(int currentDir) {
		return currentDir == 0 ? plusMinusEdges1 : plusMinusEdges0;
	}
}
