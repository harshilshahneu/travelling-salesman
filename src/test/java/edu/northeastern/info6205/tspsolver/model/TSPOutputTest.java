package edu.northeastern.info6205.tspsolver.model;

import edu.northeastern.info6205.tspsolver.constant.Constant;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;

public class TSPOutputTest {

    @Test
    public void setGetCompleteFilePathTest() {
        TSPOutput tspOutput = new TSPOutput();
        tspOutput.setCompleteFilePath(Constant.APP_NAME);
        assertEquals(Constant.APP_NAME, tspOutput.getCompleteFilePath());
    }

    @Test
    public void setGetFileNameTest() {
        TSPOutput tspOutput = new TSPOutput();
        tspOutput.setFileName(Constant.APP_NAME);
        assertEquals(Constant.APP_NAME, tspOutput.getFileName());
    }
}
