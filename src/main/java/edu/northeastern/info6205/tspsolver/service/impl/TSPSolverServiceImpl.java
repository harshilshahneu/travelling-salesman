package edu.northeastern.info6205.tspsolver.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import edu.northeastern.info6205.tspsolver.harshil.Edge;
import edu.northeastern.info6205.tspsolver.harshil.PrimsMST;
import edu.northeastern.info6205.tspsolver.model.Point;
import edu.northeastern.info6205.tspsolver.service.TSPSolverService;

@Service
public class TSPSolverServiceImpl implements TSPSolverService {
	private static final Logger LOGGER = LoggerFactory.getLogger(TSPSolverServiceImpl.class);

	@Override
	public void solveAsync(List<Point> points) {
		LOGGER.info("TSPSolverService will solve asynchronosly for points size: {}", points.size());
		
		Runnable runnable = () -> {
			PrimsMST primsMST = new PrimsMST(points);
			primsMST.solve();
			Edge[] edges = primsMST.getMst();
			for(Edge edge : edges) {
				if(edge == null) {
					// TODO THis should not even print!!
					// Keeping it here for now, so that can debug later
					LOGGER.trace("NULL EDGE");
				} else {
					LOGGER.trace(
							"{}, to {} Distance: {}",
							edge.from != null ? edge.from.getId() : "NULL",
							edge.to != null ? edge.to.getId() : "NULL",
							edge.distance);	
				}
			}
//			primsMST.printMST(edges);
			
			LOGGER.info("Cost: {}", primsMST.getMstCost());
		};
		
		

		new Thread(runnable).start();
	}

}
