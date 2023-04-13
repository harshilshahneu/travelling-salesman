package edu.northeastern.info6205.tspsolver.jgrapht;

import edu.northeastern.info6205.tspsolver.jgrapht.util.CollectionUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class GraphMetrics {

	static <V, E> long naiveCountTriangles(Graph<V, E> graph, List<V> vertexSubset) {
		long total = 0;

		if (graph.getType().isAllowingMultipleEdges()) {
			for (int i = 0; i < vertexSubset.size(); i++) {
				for (int j = i + 1; j < vertexSubset.size(); j++) {
					for (int k = j + 1; k < vertexSubset.size(); k++) {
						V u = vertexSubset.get(i);
						V v = vertexSubset.get(j);
						V w = vertexSubset.get(k);

						int uvEdgeCount = graph.getAllEdges(u, v).size();
						if (uvEdgeCount == 0) {
							continue;
						}
						int vwEdgeCount = graph.getAllEdges(v, w).size();
						if (vwEdgeCount == 0) {
							continue;
						}
						int wuEdgeCount = graph.getAllEdges(w, u).size();
						if (wuEdgeCount == 0) {
							continue;
						}
						total += uvEdgeCount * vwEdgeCount * wuEdgeCount;
					}
				}
			}
		} else {
			for (int i = 0; i < vertexSubset.size(); i++) {
				for (int j = i + 1; j < vertexSubset.size(); j++) {
					for (int k = j + 1; k < vertexSubset.size(); k++) {
						V u = vertexSubset.get(i);
						V v = vertexSubset.get(j);
						V w = vertexSubset.get(k);

						if (graph.containsEdge(u, v) && graph.containsEdge(v, w) && graph.containsEdge(w, u)) {
							total++;
						}
					}
				}
			}
		}

		return total;
	}

	public static <V, E> long getNumberOfTriangles(Graph<V, E> graph) {
		GraphTests.requireUndirected(graph);

		final int sqrtV = (int) Math.sqrt(graph.vertexSet().size());

		List<V> vertexList = new ArrayList<>(graph.vertexSet());

		

		// Fix vertex order for unique comparison of vertices
		Map<V, Integer> vertexOrder = CollectionUtil.newHashMapWithExpectedSize(graph.vertexSet().size());
		int k = 0;
		for (V v : graph.vertexSet()) {
			vertexOrder.put(v, k++);
		}

		Comparator<V> comparator = Comparator.comparingInt(graph::degreeOf).thenComparingInt(System::identityHashCode)
				.thenComparingInt(vertexOrder::get);

		vertexList.sort(comparator);

		// vertex v is a heavy-hitter iff degree(v) >= sqrtV
		List<V> heavyHitterVertices = vertexList.stream().filter(x -> graph.degreeOf(x) >= sqrtV)
				.collect(Collectors.toCollection(ArrayList::new));

		// count the number of triangles formed from only heavy-hitter vertices
		long numberTriangles = naiveCountTriangles(graph, heavyHitterVertices);

		for (E edge : graph.edgeSet()) {
			V v1 = graph.getEdgeSource(edge);
			V v2 = graph.getEdgeTarget(edge);

			if (v1 == v2) {
				continue;
			}

			if (graph.degreeOf(v1) < sqrtV || graph.degreeOf(v2) < sqrtV) {
				// ensure that v1 <= v2 (swap them otherwise)
				if (comparator.compare(v1, v2) > 0) {
					V tmp = v1;
					v1 = v2;
					v2 = tmp;
				}

				for (E e : graph.edgesOf(v1)) {
					V u = Graphs.getOppositeVertex(graph, e, v1);

					// check if the triangle is non-trivial: u, v1, v2 are distinct vertices
					if (u == v1 || u == v2) {
						continue;
					}

					
					if (comparator.compare(v2, u) <= 0 && graph.containsEdge(u, v2)) {
						numberTriangles++;
					}
				}
			}
		}

		return numberTriangles;
	}
}
