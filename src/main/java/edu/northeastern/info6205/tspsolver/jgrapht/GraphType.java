package edu.northeastern.info6205.tspsolver.jgrapht;

public interface GraphType {
	
	boolean isDirected();

	boolean isUndirected();

	boolean isMixed();

	boolean isAllowingMultipleEdges();

	boolean isAllowingSelfLoops();

	boolean isAllowingCycles();

	boolean isWeighted();

	boolean isSimple();

	boolean isPseudograph();

	boolean isMultigraph();

	boolean isModifiable();

	GraphType asDirected();

	GraphType asUndirected();

	GraphType asMixed();

	GraphType asUnweighted();

	GraphType asWeighted();

	GraphType asModifiable();

	GraphType asUnmodifiable();
}
