package edu.northeastern.info6205.tspsolver.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import edu.northeastern.info6205.tspsolver.TSPApplicationContext;
import edu.northeastern.info6205.tspsolver.model.Action;
import edu.northeastern.info6205.tspsolver.service.WebSocketPublishService;

public class WebSocketPublishServiceImpl implements WebSocketPublishService {
	private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketPublishServiceImpl.class);

	private static WebSocketPublishService instance;
	
	private SimpMessagingTemplate simpMessagingTemplate;

	private static final String TOPIC_DESITINATION = "/topic/graph-action";
	
	private WebSocketPublishServiceImpl() {
		LOGGER.info("Initialising the instance");
		
		ApplicationContext applicationContext = TSPApplicationContext.getContext();
		if (applicationContext != null) {
			simpMessagingTemplate = applicationContext.getBean(SimpMessagingTemplate.class);
			LOGGER.info("simpMessagingTemplate: {}", simpMessagingTemplate);
		} else {
			// This is expected to print while running unit test
			LOGGER.info("applicationContext is NULL");
		}
	}
	
	public static WebSocketPublishService getInstance() {
		if (instance == null) {
			instance = new WebSocketPublishServiceImpl();
		}
		
		return instance;
	}
	
	@Override
	public void publish(Action<?> action) {
//		LOGGER.trace("publishing action: {}", action);
		if (simpMessagingTemplate == null) {
			// This is expected to print while running unit test
			LOGGER.trace("looks like unit test running, template is NULL, so will not actually publish the event");
			return;
		}
		
		simpMessagingTemplate.convertAndSend(TOPIC_DESITINATION, action);
	}

}
