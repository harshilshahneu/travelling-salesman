package edu.northeastern.info6205.tspsolver.util;

import java.util.List;

import edu.northeastern.info6205.tspsolver.harshil.Edge;

public class EdgeUtil {
//	private static final Logger LOGGER = LoggerFactory.getLogger(EdgeUtil.class);

	/**
	 * Will calculate the total distance travelled
	 * along the list of edges provided
	 * */
	public static double getTotalCost(List<Edge> edges) {
//		LOGGER.trace("calculating total cost of edges size: {}", edges.size());
		double totalCost = edges.stream()
                .mapToDouble(Edge::getDistance)
                .sum();
//		LOGGER.trace("totalCost of travelling along the edges: {}", totalCost);
		return totalCost;
	}
	
}
