package edu.northeastern.info6205.tspsolver.restcontroller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.northeastern.info6205.tspsolver.harshil.Edge;
import edu.northeastern.info6205.tspsolver.model.Point;
import edu.northeastern.info6205.tspsolver.service.PerfectMatchingSolverService;

@RestController
public class PerfectMatchingSolverController {
	private static final Logger LOGGER = LoggerFactory.getLogger(PerfectMatchingSolverController.class);
	
	@Autowired
	private PerfectMatchingSolverService perfectMatchingSolverService;
	
	@PostMapping("/api/perfect-matchin/edmond")
	public List<Edge> edmondAlgorithm(@RequestBody List<Point> points) {
		LOGGER.trace("solving edmond algorithms for points size: {}", points.size());
		List<Edge> result = perfectMatchingSolverService.edmondAlgorithm(points);
		return result;
	}
	
	@PostMapping("/api/perfect-matchin/kolmogorv")
	public List<Edge> kolmogorovAlgorithm(@RequestBody List<Point> points) {
		LOGGER.trace("solving kolmogorov algorithms for points size: {}", points.size());
		List<Edge> result = perfectMatchingSolverService.kolmogorovMatching(points);
		return result;
	}
	
	@PostMapping("/api/perfect-matchin/kolmogorv/{multiplier}")
	public List<Edge> kolmogorovAlgorithmMultiplies(@RequestBody List<Point> points, @PathVariable Integer multiplier) {
		LOGGER.trace(
				"solving kolmogorov algorithms for points size: {}, multiplier: {}", 
				points.size(),
				multiplier);
		
		int counter = 0;
		List<Point> newList = new ArrayList<>(points.size() * multiplier);
		for (int i = 0; i < multiplier; i++) {
			List<Point> modifiedPoints = new ArrayList<>();
			
			for (Point point : points) {
				Point newPoint = new Point(
						point.getId() + " - " + counter, 
						point.getLatitude(), 
						point.getLongitude());
				
				counter++;
				
				modifiedPoints.add(newPoint);
			}
			
			newList.addAll(modifiedPoints);
		}
		
		List<Edge> result = perfectMatchingSolverService.kolmogorovMatching(newList);
		return result;
	}
	
}
