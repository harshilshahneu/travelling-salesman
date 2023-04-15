package edu.northeastern.info6205.tspsolver.harshil;


import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.northeastern.info6205.tspsolver.model.MinIndexedDHeap;

import static org.junit.Assert.*;

public class MinIndexedDHeapTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(edu.northeastern.info6205.tspsolver.harshil.MinIndexedDHeapTest.class);

    @Test
    public void testInsert() {
        LOGGER.trace("testInsert()");
        MinIndexedDHeap<Integer> heap = new MinIndexedDHeap<>(2, 5);
        heap.insert(0, 10);
        heap.insert(1, 20);
        heap.insert(2, 5);
        assertEquals(3, heap.size());
    }

    @Test
    public void testContains() {
        LOGGER.trace("testContains()");
        MinIndexedDHeap<Integer> heap = new MinIndexedDHeap<>(2, 5);
        heap.insert(0, 10);
        assertTrue(heap.contains(0));
        assertFalse(heap.contains(1));
    }

    @Test
    public void testPeekMinValue() {
        LOGGER.trace("testPeekMinValue()");
        MinIndexedDHeap<Integer> heap = new MinIndexedDHeap<>(2, 5);
        heap.insert(0, 10);
        heap.insert(1, 20);
        heap.insert(2, 5);
        assertEquals(Integer.valueOf(5), heap.peekMinValue());
    }

    @Test
    public void testPollMinValue() {
        LOGGER.trace("testPollMinValue()");
        MinIndexedDHeap<Integer> heap = new MinIndexedDHeap<>(2, 5);
        heap.insert(0, 10);
        heap.insert(1, 20);
        heap.insert(2, 5);
        assertEquals(Integer.valueOf(5), heap.pollMinValue());
        assertEquals(Integer.valueOf(10), heap.pollMinValue());
    }

    @Test
    public void testDelete() {
        LOGGER.trace("testDelete()");
        MinIndexedDHeap<Integer> heap = new MinIndexedDHeap<>(2, 5);
        heap.insert(0, 10);
        heap.insert(1, 20);
        heap.insert(2, 5);
        heap.delete(2);
        assertFalse(heap.contains(2));
        assertEquals(Integer.valueOf(10), heap.peekMinValue());
    }

    @Test
    public void testUpdate() {
        LOGGER.trace("testUpdate()");
        MinIndexedDHeap<Integer> heap = new MinIndexedDHeap<>(2, 5);
        heap.insert(0, 10);
        heap.insert(1, 20);
        heap.insert(2, 5);
        heap.update(0, 15);
        assertEquals(Integer.valueOf(15), heap.valueOf(0));
        assertEquals(Integer.valueOf(5), heap.peekMinValue());
    }

    @Test
    public void testDecrease() {
        LOGGER.trace("testDecrease()");
        MinIndexedDHeap<Integer> heap = new MinIndexedDHeap<>(2, 5);
        heap.insert(0, 10);
        heap.insert(1, 20);
        heap.insert(2, 5);
        heap.decrease(1, 15);
        assertEquals(Integer.valueOf(15), heap.valueOf(1));
        assertEquals(Integer.valueOf(5), heap.peekMinValue());
    }

    @Test
    public void testIncrease() {
        LOGGER.trace("testIncrease()");
        MinIndexedDHeap<Integer> heap = new MinIndexedDHeap<>(2, 5);
        heap.insert(0, 10);
        heap.insert(1, 20);
        heap.insert(2, 5);
        heap.increase(0, 25);
        assertEquals(Integer.valueOf(25), heap.valueOf(0));
        assertEquals(Integer.valueOf(5), heap.peekMinValue());
    }
}
