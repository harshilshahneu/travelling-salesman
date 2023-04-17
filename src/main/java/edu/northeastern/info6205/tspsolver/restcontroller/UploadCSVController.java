package edu.northeastern.info6205.tspsolver.restcontroller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import edu.northeastern.info6205.tspsolver.constant.Constant;
import edu.northeastern.info6205.tspsolver.model.Point;
import edu.northeastern.info6205.tspsolver.model.TSPPayload;
import edu.northeastern.info6205.tspsolver.service.CSVParserService;
import edu.northeastern.info6205.tspsolver.service.TSPService;
import edu.northeastern.info6205.tspsolver.service.impl.CSVParserServiceImpl;
import edu.northeastern.info6205.tspsolver.service.impl.TSPServiceImpl;

@RestController
public class UploadCSVController {
	private static final Logger LOGGER = LoggerFactory.getLogger(UploadCSVController.class);

	@PostMapping("/api/csv/{serviceType}")
	public String uploadCSV(
			@RequestPart MultipartFile multiPartFile, 
			@PathVariable String serviceType,
			@RequestPart TSPPayload tspPayload) {
		LOGGER.info(
				"Starting to upload the CSV File for serviceType: {}, tspPayload: {}", 
				serviceType,
				tspPayload);
		
		CSVParserService csvParserService = CSVParserServiceImpl.getInstance();
		List<Point> points = csvParserService.parsePoints(multiPartFile);
		
		TSPService asyncService = TSPServiceImpl.getInstance();
		asyncService.solve(serviceType, points, 0, tspPayload);
		
		return Constant.OK;
	}
	
}
