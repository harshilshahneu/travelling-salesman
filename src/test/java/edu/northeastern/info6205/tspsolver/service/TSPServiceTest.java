package edu.northeastern.info6205.tspsolver.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import edu.northeastern.info6205.tspsolver.constant.Constant;
import edu.northeastern.info6205.tspsolver.model.Point;
import edu.northeastern.info6205.tspsolver.model.TSPOutput;
import edu.northeastern.info6205.tspsolver.model.TSPPayload;
import edu.northeastern.info6205.tspsolver.model.TSPPayload.AntColonyOptimazationPayload;
import edu.northeastern.info6205.tspsolver.model.TSPPayload.SimulatedAnnealingPayload;
import edu.northeastern.info6205.tspsolver.model.TSPPayload.ThreeOptPayload;
import edu.northeastern.info6205.tspsolver.model.TSPPayload.TwoOptPayload;
import edu.northeastern.info6205.tspsolver.service.impl.CSVParserServiceImpl;
import edu.northeastern.info6205.tspsolver.service.impl.TSPServiceImpl;

public class TSPServiceTest {
	
    @Test
    public void instanceNotNullTest() {
        TSPService TSPService = TSPServiceImpl.getInstance();
        assertNotNull(TSPService);
    }

    @Test
    public void singletonInstanceTest() {
        TSPService firstInstance = TSPServiceImpl.getInstance();
        TSPService secondInstance = TSPServiceImpl.getInstance();
        assertEquals(firstInstance, secondInstance);
    }
    
    @Test
    public void nullIdentifierTest() {
        TSPService firstInstance = TSPServiceImpl.getInstance();
        TSPPayload payload = new TSPPayload();
        
        CSVParserService csvParserService = CSVParserServiceImpl.getInstance();
        List<Point> points = csvParserService.parsePoints(Constant.TEST_DATA_FILE_SMALL);
        
        assertThrows(NullPointerException.class, () -> {
            firstInstance.solve(null, points, 0, payload);
        });
    }
    
    @Test
    public void blankIdentifierTest() {
        TSPService firstInstance = TSPServiceImpl.getInstance();
        TSPPayload payload = new TSPPayload();
        
        CSVParserService csvParserService = CSVParserServiceImpl.getInstance();
        List<Point> points = csvParserService.parsePoints(Constant.TEST_DATA_FILE_SMALL);
        
        assertThrows(NullPointerException.class, () -> {
            firstInstance.solve(Constant.BLANK_STRING, points, 0, payload);
        });
    }
    
    @Test
    public void invalidIdentifierTest() {
        TSPService firstInstance = TSPServiceImpl.getInstance();
        TSPPayload payload = new TSPPayload();
        
        CSVParserService csvParserService = CSVParserServiceImpl.getInstance();
        List<Point> points = csvParserService.parsePoints(Constant.TEST_DATA_FILE_SMALL);
        
        assertThrows(NullPointerException.class, () -> {
            firstInstance.solve(Constant.DASH, points, 0, payload);
        });
    }
    
    @Test
    public void nullPointsTest() {
        TSPService firstInstance = TSPServiceImpl.getInstance();
        TSPPayload payload = new TSPPayload();
        
        assertThrows(NullPointerException.class, () -> {
            firstInstance.solve(Constant.KEY_IDENTIFIER_ANT_COLONY, null, 0, payload);
        });
    }
    
    @Test
    public void emptyPointsTest() {
        TSPService firstInstance = TSPServiceImpl.getInstance();
        TSPPayload payload = new TSPPayload();
        
        assertThrows(IndexOutOfBoundsException.class, () -> {
            firstInstance.solve(Constant.KEY_IDENTIFIER_ANT_COLONY, Collections.emptyList(), 0, payload);
        });
    }
    
    @Test
    public void nullPayloadTest() {
        TSPService firstInstance = TSPServiceImpl.getInstance();
        CSVParserService csvParserService = CSVParserServiceImpl.getInstance();
        List<Point> points = csvParserService.parsePoints(Constant.TEST_DATA_FILE_SMALL);
        
        assertThrows(NullPointerException.class, () -> {
            firstInstance.solve(Constant.KEY_IDENTIFIER_ANT_COLONY, points, 0, null);
        });
    }
    
    @Test
    public void ACOThreeOpt_smallDataTest() {
    	ACOThreeOpt_dataTest(Constant.TEST_DATA_FILE_SMALL);
    }
    
    @Test
    public void AntColonyOptimzation_smallDataTest() {
    	AntColonyOptimzation_dataTest(Constant.TEST_DATA_FILE_SMALL);
    }
    
    @Test
    public void Christofides_smallDataTest() {
    	Christofides_dataTest(Constant.TEST_DATA_FILE_SMALL);
    }
    
    @Test
    public void SimulatedAnnealing_smallDataTest() {
    	SimulatedAnnealing_dataTest(Constant.TEST_DATA_FILE_SMALL);
    }
    
    @Test
    public void ThreeOpt_smallDataStrategy1Test() {
    	ThreeOpt_dataTest(Constant.TEST_DATA_FILE_SMALL, 1);
    }
    
    @Test
    public void ThreeOpt_smallDataStrategy2Test() {
    	ThreeOpt_dataTest(Constant.TEST_DATA_FILE_SMALL, 2);
    }
    
    @Test
    public void ThreeOpt_smallDataStrategy3Test() {
    	ThreeOpt_dataTest(Constant.TEST_DATA_FILE_SMALL, 3);
    }
    
    @Test
    public void ThreeOpt_smallDataStrategy4Test() {
    	ThreeOpt_dataTest(Constant.TEST_DATA_FILE_SMALL, 4);
    }
    
    @Test
    public void TwoOpt_smallDataStrategy1Test() {
    	TwoOpt_dataTest(Constant.TEST_DATA_FILE_SMALL, 1);
    }
    
    @Test
    public void TwoOpt_smallDataStrategy2Test() {
    	TwoOpt_dataTest(Constant.TEST_DATA_FILE_SMALL, 2);
    }
    
    @Test
    public void TwoOpt_smallDataStrategy3Test() {
    	TwoOpt_dataTest(Constant.TEST_DATA_FILE_SMALL, 3);
    }
    
    @Test
    public void ACOThreeOpt_bigDataTest() {
    	ACOThreeOpt_dataTest(Constant.TEST_DATA_FILE_BIG);
    }
    
    @Test
    public void AntColonyOptimzation_bigDataTest() {
    	AntColonyOptimzation_dataTest(Constant.TEST_DATA_FILE_BIG);
    }
    
    @Test
    public void Christofides_bigDataTest() {
    	Christofides_dataTest(Constant.TEST_DATA_FILE_BIG);
    }
    
    @Test
    public void SimulatedAnnealing_bigDataTest() {
    	SimulatedAnnealing_dataTest(Constant.TEST_DATA_FILE_BIG);
    }
    
    @Test
    public void ThreeOpt_bigDataTestStrategy1Test() {
    	ThreeOpt_dataTest(Constant.TEST_DATA_FILE_BIG, 1);
    }
    
    @Test
    public void ThreeOpt_bigDataTestStrategy2Test() {
    	ThreeOpt_dataTest(Constant.TEST_DATA_FILE_BIG, 2);
    }
    
    @Test
    public void ThreeOpt_bigDataTestStrategy3Test() {
    	ThreeOpt_dataTest(Constant.TEST_DATA_FILE_BIG, 3);
    }
    
    @Test
    public void ThreeOpt_bigDataTestStrategy4Test() {
    	ThreeOpt_dataTest(Constant.TEST_DATA_FILE_BIG, 4);
    }
    
    @Test
    public void TwoOpt_bigDataTestStrategy1Test() {
    	TwoOpt_dataTest(Constant.TEST_DATA_FILE_BIG, 1);
    }
    
    @Test
    public void TwoOpt_bigDataTestStrategy2Test() {
    	TwoOpt_dataTest(Constant.TEST_DATA_FILE_BIG, 2);
    }
    
    private void ACOThreeOpt_dataTest(String dataFileName) {
    	TSPPayload payload = new TSPPayload();
    	
    	ThreeOptPayload threeOptPayload = new ThreeOptPayload();
    	threeOptPayload.setStrategy(1);
    	threeOptPayload.setBudget(10000);
    	payload.setThreeOptPayload(threeOptPayload);
    	
    	AntColonyOptimazationPayload antColonyOptimazationPayload = new AntColonyOptimazationPayload();
    	antColonyOptimazationPayload.setNumberOfAnts(10);
		antColonyOptimazationPayload.setPhermoneExponent(1);
		antColonyOptimazationPayload.setHeuristicExponent(2);
		antColonyOptimazationPayload.setPhermoneEvaporationRate(0.1);
		antColonyOptimazationPayload.setPhermoneDepositFactor(1);
		antColonyOptimazationPayload.setNumberOfIterations(20);
		antColonyOptimazationPayload.setMaxImprovementIterations(1000);
    	payload.setAntColonyOptimazationPayload(antColonyOptimazationPayload);
    	
    	dataTest(
    			dataFileName, 
    			payload, 
    			Constant.KEY_IDENTIFIER_ANT_COLONY_THREE_OPT);
	}
    
    private void AntColonyOptimzation_dataTest(String dataFileName) {
    	TSPPayload payload = new TSPPayload();
    	
    	AntColonyOptimazationPayload antColonyOptimazationPayload = new AntColonyOptimazationPayload();
    	antColonyOptimazationPayload.setNumberOfAnts(10);
		antColonyOptimazationPayload.setPhermoneExponent(1);
		antColonyOptimazationPayload.setHeuristicExponent(2);
		antColonyOptimazationPayload.setPhermoneEvaporationRate(0.1);
		antColonyOptimazationPayload.setPhermoneDepositFactor(1);
		antColonyOptimazationPayload.setNumberOfIterations(20);
		antColonyOptimazationPayload.setMaxImprovementIterations(1000);
    	payload.setAntColonyOptimazationPayload(antColonyOptimazationPayload);
    	
    	dataTest(
    			dataFileName, 
    			payload, 
    			Constant.KEY_IDENTIFIER_ANT_COLONY);
	}

	private void Christofides_dataTest(String dataFileName) {
    	dataTest(
    			dataFileName, 
    			null, 
    			Constant.KEY_IDENTIFIER_CHRISTOFIDES);
	}

	private void SimulatedAnnealing_dataTest(String dataFileName) {
    	TSPPayload payload = new TSPPayload();
    	
    	SimulatedAnnealingPayload annealingPayload = new SimulatedAnnealingPayload();
    	annealingPayload.setMaxIteration(1000000);
    	annealingPayload.setStartingTemperature(1000);
    	annealingPayload.setFinalTemperature(1);
    	annealingPayload.setCoolingRate(0.9995);
    	payload.setSimulatedAnnealingPayload(annealingPayload);
    	
    	dataTest(
    			dataFileName, 
    			payload, 
    			Constant.KEY_IDENTIFIER_SIMULATED_ANNEALING);
	}
    
    private void ThreeOpt_dataTest(String dataFileName, int strategy) {
    	TSPPayload payload = new TSPPayload();
    	
    	ThreeOptPayload threeOptPayload = new ThreeOptPayload();
    	threeOptPayload.setStrategy(strategy);
    	threeOptPayload.setBudget(10000);
    	payload.setThreeOptPayload(threeOptPayload);
    	
        dataTest(
    			dataFileName, 
    			payload, 
    			Constant.KEY_IDENTIFIER_RANDOM_THREE_OPT);
	}
    
    private void TwoOpt_dataTest(String dataFileName, int strategy) {
    	TSPPayload payload = new TSPPayload();
    	
    	TwoOptPayload twoOptPayload = new TwoOptPayload();
    	twoOptPayload.setStrategy(strategy);
    	twoOptPayload.setBudget(10000);
    	payload.setTwoOptPayload(twoOptPayload);
    	
    	dataTest(
    			dataFileName, 
    			payload, 
    			Constant.KEY_IDENTIFIER_RANDOM_TWO_OPT);
	}
    
    private void dataTest(
    		String dataFileName,
    		TSPPayload payload,
    		String keyIdentifier) {
    	CSVParserService csvParserService = CSVParserServiceImpl.getInstance();
    	List<Point> points = csvParserService.parsePoints(dataFileName);
    	
    	TSPService service = TSPServiceImpl.getInstance();
    	TSPOutput tspOutput = service.solve(keyIdentifier, points, 0, payload);
    	
    	String completeFilePath = tspOutput.getCompleteFilePath();
    	File file = new File(completeFilePath);
    	assertTrue(file.exists());
    }
}
