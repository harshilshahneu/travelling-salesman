package edu.northeastern.info6205.tspsolver.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TSPOutputTest {

    //TODO: double check if this test cases are correct
    @Test
    public void testGetCompleteFilePath() {
        TSPOutput tspOutput = new TSPOutput();
        tspOutput.setCompleteFilePath("/home/user/tspsolution/output.txt");
        assertEquals("/home/user/tspsolution/output.txt", tspOutput.getCompleteFilePath());
    }

    @Test
    public void testGetFileName() {
        TSPOutput tspOutput = new TSPOutput();
        tspOutput.setFileName("output.txt");
        assertEquals("output.txt", tspOutput.getFileName());
    }

    @Test
    public void testToString() {
        TSPOutput tspOutput = new TSPOutput();
        tspOutput.setCompleteFilePath("/home/user/tspsolution/output.txt");
        tspOutput.setFileName("output.txt");
        assertEquals("TSPOutput [completeFilePath=/home/user/tspsolution/output.txt, fileName=output.txt]", tspOutput.toString());
    }
}
