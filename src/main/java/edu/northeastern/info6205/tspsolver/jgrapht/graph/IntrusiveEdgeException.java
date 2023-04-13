package edu.northeastern.info6205.tspsolver.jgrapht.graph;

public class IntrusiveEdgeException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public <V> IntrusiveEdgeException(V source, V target) {
		super("Edge already associated with source <" + source + "> and target <" + target + ">");
	}
}
