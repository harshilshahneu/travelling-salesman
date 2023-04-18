package edu.northeastern.info6205.tspsolver.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class ActionTest {

    @Test
    public void getActionTypeTest() {
        Action<String> action = new Action<>();
        action.setActionType("testAction");
        assertEquals("testAction", action.getActionType());
    }

    @Test
    public void getPayloadTest() {
        Action<Integer> action = new Action<>();
        action.setPayload(100);
        assertEquals(Integer.valueOf(100), action.getPayload());
    }

    @Test
    public void setActionTypeTest() {
        Action<String> action = new Action<>();
        action.setActionType("testAction");
        assertEquals("testAction", action.getActionType());
    }

    @Test
    public void setPayloadTest() {
        Action<Integer> action = new Action<>();
        action.setPayload(100);
        assertEquals(Integer.valueOf(100), action.getPayload());
    }

}

