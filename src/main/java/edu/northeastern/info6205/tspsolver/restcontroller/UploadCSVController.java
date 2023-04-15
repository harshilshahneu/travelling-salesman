package edu.northeastern.info6205.tspsolver.restcontroller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import edu.northeastern.info6205.tspsolver.model.Point;
import edu.northeastern.info6205.tspsolver.service.CSVParserService;
import edu.northeastern.info6205.tspsolver.service.MapService;
import edu.northeastern.info6205.tspsolver.service.TSPSolverService;
import edu.northeastern.info6205.tspsolver.service.impl.CSVParserServiceImpl;
import edu.northeastern.info6205.tspsolver.service.impl.MapServiceImpl;
import edu.northeastern.info6205.tspsolver.service.impl.TSPSolverServiceImpl;

@RestController
public class UploadCSVController {
	private static final Logger LOGGER = LoggerFactory.getLogger(UploadCSVController.class);

	@PostMapping("/api/csv")
	public String uploadCSV(@RequestParam MultipartFile multiPartFile) {
		LOGGER.debug("Starting to upload the CSV File");
		
		MapService mapService = MapServiceImpl.getInstance();
		mapService.publishClearMap();
		
		CSVParserService csvParserService = CSVParserServiceImpl.getInstance();
		List<Point> points = csvParserService.parsePoints(multiPartFile);
		mapService.publishAddPointsAndFitBound(points);
		
		// TODO for now using 0 as starting index, but should be part of API query param
		TSPSolverService tspSolverService = TSPSolverServiceImpl.getInstance();
		tspSolverService.solveAsync(points, 0);
		
		return "OK";
	}
	
}
