package edu.northeastern.info6205.tspsolver.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import edu.northeastern.info6205.tspsolver.model.Action;
import edu.northeastern.info6205.tspsolver.model.ActionType;
import edu.northeastern.info6205.tspsolver.model.Point;
import edu.northeastern.info6205.tspsolver.service.TestService;

@Service
public class TestServiceImpl implements TestService {
	private static final Logger LOGGER = LoggerFactory.getLogger(TestServiceImpl.class);

	@Autowired
	private SimpMessagingTemplate template;
	
	@Override
	public void testAsync(List<Point> points) {
		LOGGER.trace("testAsync for points size: {}", points.size());
		
		String destination = "/topic/graph-action";

		Action<Point> clearAction = new Action<>();
		clearAction.setActionType(ActionType.CLEAR_MAP);
		template.convertAndSend(destination, clearAction);
		
		for (Point point : points) {
			LOGGER.trace("processing point id: {}", point.getId());
			
			Action<Point> action = new Action<>();
			action.setActionType(ActionType.ADD_POINT_LIST_AND_FIT_BOUND);
			action.setPayload(point);
			
			template.convertAndSend(destination, action);
		}
		
		LOGGER.trace("testAsync has finished processing for points size: {}", points.size());
	}

}
