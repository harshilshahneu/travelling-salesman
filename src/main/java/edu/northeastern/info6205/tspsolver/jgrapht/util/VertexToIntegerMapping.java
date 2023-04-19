package edu.northeastern.info6205.tspsolver.jgrapht.util;


import java.util.*;

public class VertexToIntegerMapping<V> {
	private final Map<V, Integer> vertexMap;
	private final List<V> indexList;

	public VertexToIntegerMapping(List<V> vertices) {
		Objects.requireNonNull(vertices, "the input collection of vertices cannot be null");

		vertexMap = CollectionUtil.newHashMapWithExpectedSize(vertices.size());
		indexList = vertices;

		for (V v : vertices) {
			if (vertexMap.put(v, vertexMap.size()) != null) {
				throw new IllegalArgumentException("vertices are not distinct");
			}
		}
	}

	public VertexToIntegerMapping(Collection<V> vertices) {
		this(new ArrayList<>(Objects.requireNonNull(vertices, "the input collection of vertices cannot be null")));
	}

	public Map<V, Integer> getVertexMap() {
		return vertexMap;
	}

	public List<V> getIndexList() {
		return indexList;
	}
}
