package edu.northeastern.info6205.tspsolver.restcontroller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.northeastern.info6205.tspsolver.model.Point;
import edu.northeastern.info6205.tspsolver.service.MapService;
import edu.northeastern.info6205.tspsolver.service.TestService;
import edu.northeastern.info6205.tspsolver.service.impl.MapServiceImpl;
import edu.northeastern.info6205.tspsolver.service.impl.TestServiceImpl;

@RestController
public class TestController {
	private static final Logger LOGGER = LoggerFactory.getLogger(TestController.class);

	@GetMapping("/api/test/publish-point-visit/{pointId}")
	public String publishNodeVisit(@PathVariable String pointId) {
		LOGGER.trace("publishNodeVisit() called with pointId: {}", pointId);
		MapService mapService = MapServiceImpl.getInstance();
		mapService.publishPointRelaxed(pointId);
		return "OK";
	}
	
	@PostMapping("/api/test/publish-points")
	public List<Point> publishPoints(@RequestBody List<Point> points) {
		LOGGER.trace("publishPoints() called with points size: {}", points.size());
		TestService testService = TestServiceImpl.getInstance();
		testService.testAsync(points);
		return points;
	}
	
	@GetMapping("/api/test/ping")
	public String testPing() {
		LOGGER.trace("testPing() called");
		return "Test-PING-PONG";
	}
	
}
