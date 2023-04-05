package edu.northeastern.info6205.tspsolver.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class WebsocketController {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebsocketController.class);

    @Autowired
    public WebsocketController() {
    	LOGGER.trace("WebsocketController() constructor called");
	}
}