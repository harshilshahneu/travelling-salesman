package edu.northeastern.info6205.tspsolver.model;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;

public class ActionTest {

    @Test
    public void setGetActionTypeTest() {
        Action<String> action = new Action<>();
        action.setActionType("testAction");
        assertEquals("testAction", action.getActionType());
    }

    @Test
    public void setGetPayloadTest() {
        Action<Integer> action = new Action<>();
        action.setPayload(100);
        assertEquals(Integer.valueOf(100), action.getPayload());
    }

}

