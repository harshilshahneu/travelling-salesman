package edu.northeastern.info6205.tspsolver.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

public class MinIndexedDHeapTest {

    @Test
    public void testInsert() {
        MinIndexedDHeap<Integer> heap = new MinIndexedDHeap<>(2, 5);
        heap.insert(0, 10);
        heap.insert(1, 20);
        heap.insert(2, 5);
        assertEquals(3, heap.size());
    }

    @Test
    public void testContains() {
        MinIndexedDHeap<Integer> heap = new MinIndexedDHeap<>(2, 5);
        heap.insert(0, 10);
        assertTrue(heap.contains(0));
        assertFalse(heap.contains(1));
    }

    @Test
    public void testPeekMinValue() {
        MinIndexedDHeap<Integer> heap = new MinIndexedDHeap<>(2, 5);
        heap.insert(0, 10);
        heap.insert(1, 20);
        heap.insert(2, 5);
        assertEquals(Integer.valueOf(5), heap.peekMinValue());
    }

    @Test
    public void testPollMinValue() {
        MinIndexedDHeap<Integer> heap = new MinIndexedDHeap<>(2, 5);
        heap.insert(0, 10);
        heap.insert(1, 20);
        heap.insert(2, 5);
        assertEquals(Integer.valueOf(5), heap.pollMinValue());
        assertEquals(Integer.valueOf(10), heap.pollMinValue());
    }

    @Test
    public void testDelete() {
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
        MinIndexedDHeap<Integer> heap = new MinIndexedDHeap<>(2, 5);
        heap.insert(0, 10);
        heap.insert(1, 20);
        heap.insert(2, 5);
        heap.increase(0, 25);
        assertEquals(Integer.valueOf(25), heap.valueOf(0));
        assertEquals(Integer.valueOf(5), heap.peekMinValue());
    }
}