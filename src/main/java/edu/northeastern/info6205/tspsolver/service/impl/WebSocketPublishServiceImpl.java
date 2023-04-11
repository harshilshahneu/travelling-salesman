package edu.northeastern.info6205.tspsolver.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import edu.northeastern.info6205.tspsolver.model.Action;
import edu.northeastern.info6205.tspsolver.service.WebSocketPublishService;

@Service
public class WebSocketPublishServiceImpl implements WebSocketPublishService {
//	private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketPublishServiceImpl.class);

	private static final String TOPIC_DESITINATION = "/topic/graph-action";
	
	@Autowired
	private SimpMessagingTemplate template;
	
	@Override
	public void publish(Action<?> action) {
//		LOGGER.trace("publishing action: {}", action);
		template.convertAndSend(TOPIC_DESITINATION, action);
	}

}
