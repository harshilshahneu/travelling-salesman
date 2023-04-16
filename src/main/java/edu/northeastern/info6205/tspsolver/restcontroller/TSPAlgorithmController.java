package edu.northeastern.info6205.tspsolver.restcontroller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.northeastern.info6205.tspsolver.service.TSPSolverService;
import edu.northeastern.info6205.tspsolver.service.impl.TSPAntColonyOptimzationSolverServiceImpl;
import edu.northeastern.info6205.tspsolver.service.impl.TSPChristofidesSolverServiceImpl;
import edu.northeastern.info6205.tspsolver.service.impl.TSPRandomThreeOptSolverServiceImpl;
import edu.northeastern.info6205.tspsolver.service.impl.TSPRandomTwoOptSolverServiceImpl;
import edu.northeastern.info6205.tspsolver.service.impl.TSPSimulatedAnnealingSolverServiceImpl;

@RestController
public class TSPAlgorithmController {
	private static final Logger LOGGER = LoggerFactory.getLogger(TSPAlgorithmController.class);

	private static final String NAME = "name";
	private static final String IDENTIFIER = "identifier";
	
	@GetMapping("/api/tsp/services")
	public List<Map<String, String>> getServices() {
		LOGGER.debug("Will get all the services available");
		
		List<Map<String, String>> result = new ArrayList<>();
		
		// Jsprit is not working and also takes a lot of time, so won't provide that option in the UI
//		result.add(getServiceDetails(TSPJspritSolverServiceImpl.getInstance()));
		result.add(getServiceDetails(TSPChristofidesSolverServiceImpl.getInstance()));
		result.add(getServiceDetails(TSPRandomTwoOptSolverServiceImpl.getInstance()));
		result.add(getServiceDetails(TSPRandomThreeOptSolverServiceImpl.getInstance()));
		result.add(getServiceDetails(TSPSimulatedAnnealingSolverServiceImpl.getInstance()));
		result.add(getServiceDetails(TSPAntColonyOptimzationSolverServiceImpl.getInstance()));

		return result;
	}
	
	private Map<String, String> getServiceDetails(TSPSolverService service) {
		Map<String, String> result = new HashMap<>();
		result.put(NAME, service.getName());
		result.put(IDENTIFIER, service.getKeyIdentifier());
		return result;
	}
	
}
