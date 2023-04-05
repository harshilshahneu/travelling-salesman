package edu.northeastern.info6205.tspsolver.restcontroller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import edu.northeastern.info6205.tspsolver.service.MapService;

@RestController
public class TestController {
	private static final Logger LOGGER = LoggerFactory.getLogger(TestController.class);

	@Autowired
	private MapService mapService;
	
	@GetMapping("/api/test/publish-point-visit/{pointId}")
	public String publishNodeVisit(@PathVariable String pointId) {
		LOGGER.trace("publishNodeVisit() called with pointId: {}", pointId);
		mapService.publishPointRelaxed(pointId);
		return "OK";
	}
	
	@GetMapping("/api/test/ping")
	public String testPing() {
		LOGGER.trace("testPing() called");
		return "Test-PING-PONG";
	}
	
}
