package edu.northeastern.info6205.tspsolver.jgrapht;

import java.util.*;
import java.util.stream.Collectors;

public abstract class GraphTests {
	private static final String GRAPH_CANNOT_BE_NULL = "Graph cannot be null";
	private static final String GRAPH_MUST_BE_DIRECTED_OR_UNDIRECTED = "Graph must be directed or undirected";
	private static final String GRAPH_MUST_BE_UNDIRECTED = "Graph must be undirected";
	private static final String GRAPH_MUST_BE_DIRECTED = "Graph must be directed";
	private static final String GRAPH_MUST_BE_WEIGHTED = "Graph must be weighted";

	public static <V, E> boolean isEmpty(Graph<V, E> graph) {
		Objects.requireNonNull(graph, GRAPH_CANNOT_BE_NULL);
		return graph.edgeSet().isEmpty();
	}

	public static <V, E> boolean isSimple(Graph<V, E> graph) {
		Objects.requireNonNull(graph, GRAPH_CANNOT_BE_NULL);

		GraphType type = graph.getType();
		if (type.isSimple()) {
			return true;
		}

		// no luck, we have to check
		for (V v : graph.vertexSet()) {
			Set<V> neighbors = new HashSet<>();
			for (E e : graph.outgoingEdgesOf(v)) {
				V u = Graphs.getOppositeVertex(graph, e, v);
				if (u.equals(v) || !neighbors.add(u)) {
					return false;
				}
			}
		}

		return true;
	}

	public static <V, E> boolean hasSelfLoops(Graph<V, E> graph) {
		Objects.requireNonNull(graph, GRAPH_CANNOT_BE_NULL);

		if (!graph.getType().isAllowingSelfLoops()) {
			return false;
		}

		// no luck, we have to check
		for (E e : graph.edgeSet()) {
			if (graph.getEdgeSource(e).equals(graph.getEdgeTarget(e))) {
				return true;
			}
		}
		return false;
	}

	public static <V, E> boolean hasMultipleEdges(Graph<V, E> graph) {
		Objects.requireNonNull(graph, GRAPH_CANNOT_BE_NULL);

		if (!graph.getType().isAllowingMultipleEdges()) {
			return false;
		}

		// no luck, we have to check
		for (V v : graph.vertexSet()) {
			Set<V> neighbors = new HashSet<>();
			for (E e : graph.outgoingEdgesOf(v)) {
				V u = Graphs.getOppositeVertex(graph, e, v);
				if (!neighbors.add(u)) {
					return true;
				}
			}
		}
		return false;
	}

	public static <V, E> boolean isComplete(Graph<V, E> graph) {
		Objects.requireNonNull(graph, GRAPH_CANNOT_BE_NULL);
		int n = graph.vertexSet().size();
		int allEdges;
		if (graph.getType().isDirected()) {
			allEdges = Math.multiplyExact(n, n - 1);
		} else if (graph.getType().isUndirected()) {
			if (n % 2 == 0) {
				allEdges = Math.multiplyExact(n / 2, n - 1);
			} else {
				allEdges = Math.multiplyExact(n, (n - 1) / 2);
			}
		} else {
			throw new IllegalArgumentException(GRAPH_MUST_BE_DIRECTED_OR_UNDIRECTED);
		}
		return graph.edgeSet().size() == allEdges && isSimple(graph);
	}

	public static <V, E> boolean isOverfull(Graph<V, E> graph) {
		int maxDegree = graph.vertexSet().stream().mapToInt(graph::degreeOf).max().getAsInt();
		return graph.edgeSet().size() > maxDegree * Math.floor(graph.vertexSet().size() / 2.0);
	}

	public static <V, E> boolean isSplit(Graph<V, E> graph) {
		requireUndirected(graph);
		if (!isSimple(graph) || graph.vertexSet().isEmpty())
			return false;

		List<Integer> degrees = new ArrayList<>(graph.vertexSet().size());
		degrees.addAll(graph.vertexSet().stream().map(graph::degreeOf).collect(Collectors.toList()));
		Collections.sort(degrees, Collections.reverseOrder()); // sort degrees descending order
		// Find m = \max_i \{d_i\geq i-1\}
		int m = 1;
		for (; m < degrees.size() && degrees.get(m) >= m; m++) {
		}
		m--;

		int left = 0;
		for (int i = 0; i <= m; i++)
			left += degrees.get(i);
		int right = m * (m + 1);
		for (int i = m + 1; i < degrees.size(); i++)
			right += degrees.get(i);
		return left == right;
	}

	public static <V, E> boolean isCubic(Graph<V, E> graph) {
		for (V v : graph.vertexSet())
			if (graph.degreeOf(v) != 3)
				return false;
		return true;
	}

	public static <V, E> boolean hasOreProperty(Graph<V, E> graph) {
		requireUndirected(graph);

		final int n = graph.vertexSet().size();

		if (!graph.getType().isSimple() || n < 3)
			return false;

		List<V> vertexList = new ArrayList<>(graph.vertexSet());

		for (int i = 0; i < vertexList.size(); i++) {
			for (int j = i + 1; j < vertexList.size(); j++) {
				V v = vertexList.get(i);
				V w = vertexList.get(j);

				if (!v.equals(w) && !graph.containsEdge(v, w) && graph.degreeOf(v) + graph.degreeOf(w) < n)
					return false;
			}
		}

		return true;
	}

	public static <V, E> boolean isTriangleFree(Graph<V, E> graph) {
		return GraphMetrics.getNumberOfTriangles(graph) == 0;
	}

	public static <V, E> boolean isKuratowskiSubdivision(Graph<V, E> graph) {
		return isK33Subdivision(graph) || isK5Subdivision(graph);
	}

	public static <V, E> boolean isK33Subdivision(Graph<V, E> graph) {
		List<V> degree3 = new ArrayList<>();
		// collect all vertices with degree 3
		for (V vertex : graph.vertexSet()) {
			int degree = graph.degreeOf(vertex);
			if (degree == 3) {
				degree3.add(vertex);
			} else if (degree != 2) {
				return false;
			}
		}
		if (degree3.size() != 6) {
			return false;
		}
		V vertex = degree3.remove(degree3.size() - 1);
		Set<V> reachable = reachableWithDegree(graph, vertex, 3);
		if (reachable.size() != 3) {
			return false;
		}
		degree3.removeAll(reachable);
		return reachable.equals(reachableWithDegree(graph, degree3.get(0), 3))
				&& reachable.equals(reachableWithDegree(graph, degree3.get(1), 3));
	}

	public static <V, E> boolean isK5Subdivision(Graph<V, E> graph) {
		Set<V> degree5 = new HashSet<>();
		for (V vertex : graph.vertexSet()) {
			int degree = graph.degreeOf(vertex);
			if (degree == 4) {
				degree5.add(vertex);
			} else if (degree != 2) {
				return false;
			}
		}
		if (degree5.size() != 5) {
			return false;
		}
		for (V vertex : degree5) {
			Set<V> reachable = reachableWithDegree(graph, vertex, 4);
			if (reachable.size() != 4 || !degree5.containsAll(reachable) || reachable.contains(vertex)) {
				return false;
			}
		}
		return true;
	}

	private static <V, E> Set<V> reachableWithDegree(Graph<V, E> graph, V startVertex, int degree) {
		Set<V> visited = new HashSet<>();
		Set<V> reachable = new HashSet<>();
		Queue<V> queue = new ArrayDeque<>();
		queue.add(startVertex);
		while (!queue.isEmpty()) {
			V current = queue.poll();
			visited.add(current);
			for (E e : graph.edgesOf(current)) {
				V opposite = Graphs.getOppositeVertex(graph, e, current);
				if (visited.contains(opposite)) {
					continue;
				}
				if (graph.degreeOf(opposite) == degree) {
					reachable.add(opposite);
				} else {
					queue.add(opposite);
				}
			}
		}
		return reachable;
	}

	public static <V, E> Graph<V, E> requireDirected(Graph<V, E> graph, String message) {
		if (graph == null)
			throw new NullPointerException(GRAPH_CANNOT_BE_NULL);
		if (!graph.getType().isDirected()) {
			throw new IllegalArgumentException(message);
		}
		return graph;
	}

	public static <V, E> Graph<V, E> requireDirected(Graph<V, E> graph) {
		return requireDirected(graph, GRAPH_MUST_BE_DIRECTED);
	}

	public static <V, E> Graph<V, E> requireUndirected(Graph<V, E> graph, String message) {
		if (graph == null)
			throw new NullPointerException(GRAPH_CANNOT_BE_NULL);
		if (!graph.getType().isUndirected()) {
			throw new IllegalArgumentException(message);
		}
		return graph;
	}

	public static <V, E> Graph<V, E> requireUndirected(Graph<V, E> graph) {
		return requireUndirected(graph, GRAPH_MUST_BE_UNDIRECTED);
	}

	public static <V, E> Graph<V, E> requireDirectedOrUndirected(Graph<V, E> graph, String message) {
		if (graph == null)
			throw new NullPointerException(GRAPH_CANNOT_BE_NULL);
		if (!graph.getType().isDirected() && !graph.getType().isUndirected()) {
			throw new IllegalArgumentException(message);
		}
		return graph;
	}

	public static <V, E> Graph<V, E> requireDirectedOrUndirected(Graph<V, E> graph) {
		return requireDirectedOrUndirected(graph, GRAPH_MUST_BE_DIRECTED_OR_UNDIRECTED);
	}

	public static <V, E> Graph<V, E> requireWeighted(Graph<V, E> graph) {
		if (graph == null)
			throw new NullPointerException(GRAPH_CANNOT_BE_NULL);
		if (!graph.getType().isWeighted()) {
			throw new IllegalArgumentException(GRAPH_MUST_BE_WEIGHTED);
		}
		return graph;
	}
}
