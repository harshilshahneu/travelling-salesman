package edu.northeastern.info6205.tspsolver.service.impl;

//public class CSVWriterServiceImplTest {
//}

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.northeastern.info6205.tspsolver.service.CSVWriterService;

public class CSVWriterServiceImplTest {

    private static final String TMP_DIRECTORY = "tmp";

    private CSVWriterService csvWriterService;

    @Before
    public void setUp() throws Exception {
        csvWriterService = CSVWriterServiceImpl.getInstance();
    }

    @After
    public void tearDown() throws Exception {
        File tmpDir = new File(TMP_DIRECTORY);
        for (File file : tmpDir.listFiles()) {
            file.delete();
        }
        tmpDir.delete();
    }

    @Test
    public void testGetInstance() {
        assertNotNull(csvWriterService);
    }

//    @Test
//    public void testWriteWithPoints() throws Exception {
//        List<Point> points = new ArrayList<>();
//        points.add(new Point("0", 42.0, -71.0));
//        points.add(new Point("1", 43.0, -72.0));
//        TSPOutput output = csvWriterService.write(points);
//        assertNotNull(output);
//        File file = new File(output.getCompleteFilePath());
//        assertEquals(true, file.exists());
//        assertEquals(true, file.isFile());
//        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
//            // Check header row
//            assertEquals("index,id,latitude,longitude", reader.readLine());
//            // Check data rows
//            assertEquals("0,A,42.0,-71.0", reader.readLine());
//            assertEquals("1,B,43.0,-72.0", reader.readLine());
//            // Check end of file
//            assertEquals(null, reader.readLine());
//        }
//    }
}