package edu.northeastern.info6205.tspsolver.service.impl;

import edu.northeastern.info6205.tspsolver.model.Action;
import edu.northeastern.info6205.tspsolver.service.WebSocketPublishService;
import org.junit.Test;

public class WebSocketPublishServiceImplTest {
    @Test
    public void testPublish() {
        WebSocketPublishService webSocketPublishService = WebSocketPublishServiceImpl.getInstance();
        Action<?> action = new Action<>();
        // call publish() method and expect no exceptions
        webSocketPublishService.publish(action);
    }
}
