package edu.northeastern.info6205.tspsolver.service;

import edu.northeastern.info6205.tspsolver.model.Action;

/**
 * Service to publish messages in the Web Socket
 * channel
 * */
public interface WebSocketPublishService {

	/**
	 * Will publish a single action
	 * in the web-socket channel
	 * */
	void publish(Action<?> action);
	
}
