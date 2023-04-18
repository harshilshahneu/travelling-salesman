package edu.northeastern.info6205.tspsolver.service.impl;

import edu.northeastern.info6205.tspsolver.model.Action;
import edu.northeastern.info6205.tspsolver.service.WebSocketPublishService;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class WebSocketPublishServiceImplTest {
	
    @Test
    public void instanceNotNullTest() {
        WebSocketPublishService webSocketPublishService = WebSocketPublishServiceImpl.getInstance();
        assertNotNull(webSocketPublishService);
    }

    @Test
    public void singletonInstanceTest() {
        WebSocketPublishService firstInstance = WebSocketPublishServiceImpl.getInstance();
        WebSocketPublishService secondInstance = WebSocketPublishServiceImpl.getInstance();
        assertEquals(firstInstance, secondInstance);
    }
    
    @Test
    public void testPublish() {
        WebSocketPublishService webSocketPublishService = WebSocketPublishServiceImpl.getInstance();
        Action<?> action = new Action<>();
        webSocketPublishService.publish(action);
    }
}
