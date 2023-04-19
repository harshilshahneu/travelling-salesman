package edu.northeastern.info6205.tspsolver.util;

import java.util.List;

import edu.northeastern.info6205.tspsolver.model.Point;

public class PointUtil {
//	private static final Logger LOGGER = LoggerFactory.getLogger(PointUtil.class);

	/**
	 * Will calculate the total distance travelled
	 * along the list of points provided
	 * */
	public static double getTotalCost(List<Point> points) {
//		LOGGER.trace("calculating total cost of points size: {}", points.size());
		
		double total = 0;
		for (int i = 0; i < points.size() - 1; i++) {
			Point source = points.get(i);
			Point destiation = points.get(i + 1);
			double distance = HaversineDistanceUtil.haversine(source, destiation);
			total += distance;
		}
		
//		LOGGER.trace("totalCost of travelling along the points: {}", total);
		return total;
	}
	
}
