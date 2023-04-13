package edu.northeastern.info6205.tspsolver.jgrapht.graph;

public class DefaultWeightedEdge extends IntrusiveWeightedEdge {
	private static final long serialVersionUID = 1L;

	protected Object getSource() {
		return source;
	}

	protected Object getTarget() {
		return target;
	}

	protected double getWeight() {
		return weight;
	}

	@Override
	public String toString() {
		return "(" + source + " : " + target + ")";
	}

}
