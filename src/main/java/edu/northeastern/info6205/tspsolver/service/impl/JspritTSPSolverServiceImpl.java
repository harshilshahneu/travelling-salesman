package edu.northeastern.info6205.tspsolver.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.graphhopper.jsprit.core.algorithm.VehicleRoutingAlgorithm;
import com.graphhopper.jsprit.core.algorithm.box.Jsprit;
import com.graphhopper.jsprit.core.problem.Location;
import com.graphhopper.jsprit.core.problem.VehicleRoutingProblem;
import com.graphhopper.jsprit.core.problem.cost.TransportCost;
import com.graphhopper.jsprit.core.problem.cost.VehicleRoutingTransportCosts;
import com.graphhopper.jsprit.core.problem.driver.Driver;
import com.graphhopper.jsprit.core.problem.job.Job;
import com.graphhopper.jsprit.core.problem.solution.VehicleRoutingProblemSolution;
import com.graphhopper.jsprit.core.problem.solution.route.VehicleRoute;
import com.graphhopper.jsprit.core.problem.vehicle.Vehicle;
import com.graphhopper.jsprit.core.problem.vehicle.VehicleImpl;
import com.graphhopper.jsprit.core.problem.vehicle.VehicleType;
import com.graphhopper.jsprit.core.problem.vehicle.VehicleTypeImpl;
import com.graphhopper.jsprit.core.util.Solutions;

import edu.northeastern.info6205.tspsolver.constant.Constant;
import edu.northeastern.info6205.tspsolver.harshil.Edge;
import edu.northeastern.info6205.tspsolver.harshil.HaversineDistance;
import edu.northeastern.info6205.tspsolver.model.Point;
import edu.northeastern.info6205.tspsolver.service.JspritTSPSolverService;
import edu.northeastern.info6205.tspsolver.service.MapService;

@Service
public class JspritTSPSolverServiceImpl implements JspritTSPSolverService {
	private static final Logger LOGGER = LoggerFactory.getLogger(JspritTSPSolverServiceImpl.class);
	
	@Autowired
	private MapService mapService;
	
	@Override
	public List<Point> getTSPTour(List<Point> points, int startingPointIndex) {
		LOGGER.info(
				"JSP TSP will solve for points size: {}, startingPointIndex: {}", 
				points.size(),
				startingPointIndex);
		
		List<com.graphhopper.jsprit.core.problem.job.Service> jobs = new ArrayList<>();
		
		for (int i = 0; i < points.size(); ++i) {
			if (i == startingPointIndex) {
				continue;
			}
			
			Point point = points.get(i);
			
			Location location = Location.newInstance(
					point.getLongitude(), 
					point.getLatitude());
			
			com.graphhopper.jsprit.core.problem.job.Service service = com.graphhopper.jsprit.core.problem.job.Service
					.Builder
					.newInstance(point.getId())
					.setLocation(location)
					.build();
			
			jobs.add(service);
		}
		
		Point startingPoint = points.get(startingPointIndex);
		
		Location startingLocation = Location.newInstance(
				startingPoint.getLongitude(), 
				startingPoint.getLatitude());
		
		com.graphhopper.jsprit.core.problem.job.Service home = com.graphhopper.jsprit.core.problem.job.Service
				.Builder
				.newInstance(String.valueOf(startingPointIndex))
                .setLocation(startingLocation)
                .build();

		jobs.add(home);
		
		VehicleType vehicleType = VehicleTypeImpl
				.Builder
				.newInstance("vehicleType")
				.addCapacityDimension(0, 1)
				.build();
		
		VehicleImpl vehicleImpl = VehicleImpl
				.Builder
				.newInstance("vehicle")
				.setType(vehicleType)
				.setStartLocation(startingLocation)
				.setEndLocation(startingLocation)
				.build();
		
		VehicleRoutingTransportCosts cost = new HaversineCost();
		
		VehicleRoutingProblem problem = VehicleRoutingProblem
				.Builder
				.newInstance()
				.addVehicle(vehicleImpl)
				.addAllJobs(jobs)
				.setRoutingCost(cost)
				.build();
		
		VehicleRoutingAlgorithm algorithm = Jsprit.createAlgorithm(problem);
		
		VehicleRoutingProblemSolution solution = Solutions.bestOf(algorithm.searchSolutions());
		
		List<Point> tour = new ArrayList<>();
		Collection<VehicleRoute> routes = solution.getRoutes();
		Point prevPoint = null;

		for (VehicleRoute route : routes) {
			// Verbose log to analyze the loop iterations
			LOGGER.trace("Start a route");
			
			Collection<Job> shipments = route.getTourActivities().getJobs();
			
			for (Job shipment : shipments) {
				String id = shipment.getId();
				LOGGER.trace("visit shipment ID: {}", id);
				int index = Integer.parseInt(id);
				Point point = points.get(index);
				tour.add(point);
				
				if (prevPoint != null) {
					Edge edge = new Edge(prevPoint, point);
					mapService.publishDrawEdge(edge);
				}
				
				prevPoint = point;
			}
			
		}
		
		return tour;
	}
	
	private static class HaversineCost implements TransportCost, VehicleRoutingTransportCosts {

		@Override
		public double getTransportCost(
				Location from, 
				Location to, 
				double departureTime, 
				Driver driver,
				Vehicle vehicle) {
			return getDistance(
					from, 
					to, 
					departureTime, 
					vehicle);
		}

		@Override
		public double getBackwardTransportCost(
				Location from, 
				Location to, 
				double arrivalTime, 
				Driver driver,
				Vehicle vehicle) {
			return getDistance(
					from, 
					to, 
					arrivalTime, 
					vehicle);
		}

		@Override
		public double getTransportTime(Location from, Location to, double departureTime, Driver driver,
				Vehicle vehicle) {
			return getDistance(
					from, 
					to, 
					departureTime, 
					vehicle);
		}

		@Override
		public double getBackwardTransportTime(
				Location from, 
				Location to, 
				double arrivalTime, 
				Driver driver,
				Vehicle vehicle) {
			return getTransportTime(
					from, 
					to, 
					arrivalTime, 
					driver, 
					vehicle);
		}

		@Override
		public double getDistance(Location from, Location to, double departureTime, Vehicle vehicle) {
			// Doesn't matter what string we choose for ID since we are only concerned with coordinates to calculate distance
			Point n1 = new Point(Constant.BLANK_STRING, from.getCoordinate().getY(), from.getCoordinate().getX());
			Point n2 = new Point(Constant.BLANK_STRING, to.getCoordinate().getY(), to.getCoordinate().getX());

			return HaversineDistance.haversine(n1, n2);
		}
	}

}
