package edu.northeastern.info6205.tspsolver.jgrapht.graph;

public class DefaultEdge extends IntrusiveEdge {
	private static final long serialVersionUID = 1L;

	protected Object getSource() {
		return source;
	}

	protected Object getTarget() {
		return target;
	}

	@Override
	public String toString() {
		return "(" + source + " : " + target + ")";
	}
}
