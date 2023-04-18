package edu.northeastern.info6205.tspsolver.model;

import edu.northeastern.info6205.tspsolver.constant.Constant;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TSPOutputTest {

    @Test
    public void testGetCompleteFilePath() {
        TSPOutput tspOutput = new TSPOutput();
        tspOutput.setCompleteFilePath(Constant.APP_NAME);
        assertEquals(Constant.APP_NAME, tspOutput.getCompleteFilePath());
    }

    @Test
    public void testGetFileName() {
        TSPOutput tspOutput = new TSPOutput();
        tspOutput.setFileName(Constant.APP_NAME);
        assertEquals(Constant.APP_NAME, tspOutput.getFileName());
    }
}
