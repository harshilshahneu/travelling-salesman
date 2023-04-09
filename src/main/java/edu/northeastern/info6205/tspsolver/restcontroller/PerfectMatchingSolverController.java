package edu.northeastern.info6205.tspsolver.restcontroller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
	
}
