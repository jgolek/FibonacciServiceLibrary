package org.fibonacci;

import static org.junit.Assert.*;

import org.junit.Test;

public class FibonacciSequenceTest {

    @Test
    public void testSuccessfullCalculatIndexFor() throws Exception {

        assertEquals(0, FibonacciSequence.calculateIndexForNumber(0));

        // some examples
        assertEquals(1, FibonacciSequence.calculateIndexForNumber(1));
        assertEquals(4, FibonacciSequence.calculateIndexForNumber(3));
        assertEquals(5, FibonacciSequence.calculateIndexForNumber(5));
        assertEquals(6, FibonacciSequence.calculateIndexForNumber(8));
        assertEquals(7, FibonacciSequence.calculateIndexForNumber(13));
        assertEquals(8, FibonacciSequence.calculateIndexForNumber(21));
        assertEquals(9, FibonacciSequence.calculateIndexForNumber(34));

    }

    @Test
    public void testWorngFibonacciIndex() throws Exception {

        try {
            FibonacciSequence.calculateIndexForNumber(4);
            fail("IllegalArgument exception is expected");
        } catch (Exception exc) {
            assertTrue(exc instanceof IllegalArgumentException);
        }
    }
    
    @Test
    public void testNegativeFibonacciIndex() throws Exception {

        try {
            FibonacciSequence.calculateIndexForNumber(-1);
            fail("IllegalArgument exception is expected");
        } catch (Exception exc) {
            assertTrue(exc instanceof IllegalArgumentException);
        }
    }

}
