package edu.northeastern.info6205.tspsolver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import edu.northeastern.info6205.tspsolver.controller.WebsocketController;

@Component
public class TSPApplicationContext implements ApplicationContextAware {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebsocketController.class);

    private static ApplicationContext applicationContext;
    
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		LOGGER.info("setting applicationContext: {}", applicationContext);
		TSPApplicationContext.applicationContext = applicationContext;
	}
	
	public static ApplicationContext getContext() {
		return applicationContext;
	}

}
