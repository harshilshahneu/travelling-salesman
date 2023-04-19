package edu.northeastern.info6205.tspsolver.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import edu.northeastern.info6205.tspsolver.algorithm.mst.PrimsMST;
import edu.northeastern.info6205.tspsolver.constant.Constant;
import edu.northeastern.info6205.tspsolver.model.Point;
import edu.northeastern.info6205.tspsolver.model.TSPPayload;
import edu.northeastern.info6205.tspsolver.model.TSPPayload.AntColonyOptimazationPayload;
import edu.northeastern.info6205.tspsolver.model.TSPPayload.SimulatedAnnealingPayload;
import edu.northeastern.info6205.tspsolver.model.TSPPayload.ThreeOptPayload;
import edu.northeastern.info6205.tspsolver.model.TSPPayload.TwoOptPayload;
import edu.northeastern.info6205.tspsolver.service.CSVParserService;
import edu.northeastern.info6205.tspsolver.service.TSPSolverService;
import edu.northeastern.info6205.tspsolver.util.PointUtil;

public class TSPSolverServiceImplTest {
    
	@Test
    public void ACOThreeOpt_instanceNotNullTest() {
        TSPSolverService tspACOThreeOptSolverService = TSPACOThreeOptSolverServiceImpl.getInstance();
        assertNotNull(tspACOThreeOptSolverService);
    }

	@Test
    public void AntColonyOptimzation_instanceNotNullTest() {
        TSPSolverService tspACOThreeOptSolverService = TSPAntColonyOptimzationSolverServiceImpl.getInstance();
        assertNotNull(tspACOThreeOptSolverService);
    }
	
	@Test
    public void Christofides_instanceNotNullTest() {
        TSPSolverService tspChristofidesSolverService = TSPChristofidesSolverServiceImpl.getInstance();
        assertNotNull(tspChristofidesSolverService);
    }
	
	@Test
    public void SimulatedAnnealing_instanceNotNullTest() {
        TSPSolverService tspSimulatedAnnealingSolverService = TSPSimulatedAnnealingSolverServiceImpl.getInstance();
        assertNotNull(tspSimulatedAnnealingSolverService);
    }
	
	@Test
    public void ThreeOpt_instanceNotNullTest() {
        TSPSolverService tspRandomThreeOptSolverService = TSPRandomThreeOptSolverServiceImpl.getInstance();
        assertNotNull(tspRandomThreeOptSolverService);
    }
	
	@Test
    public void TwoOpt_instanceNotNullTest() {
        TSPSolverService tspRandomTwoOptSolverService = TSPRandomTwoOptSolverServiceImpl.getInstance();
        assertNotNull(tspRandomTwoOptSolverService);
    }
	
    @Test
    public void ACOThreeOpt_singletonInstanceTest() {
        TSPSolverService firstInstance = TSPACOThreeOptSolverServiceImpl.getInstance();
        TSPSolverService secondInstance = TSPACOThreeOptSolverServiceImpl.getInstance();
        assertEquals(firstInstance, secondInstance);
    }
    
    @Test
    public void AntColonyOptimzation_singletonInstanceTest() {
        TSPSolverService firstInstance = TSPAntColonyOptimzationSolverServiceImpl.getInstance();
        TSPSolverService secondInstance = TSPAntColonyOptimzationSolverServiceImpl.getInstance();
        assertEquals(firstInstance, secondInstance);
    }
    
    @Test
    public void Christofides_singletonInstanceTest() {
        TSPSolverService firstInstance = TSPChristofidesSolverServiceImpl.getInstance();
        TSPSolverService secondInstance = TSPChristofidesSolverServiceImpl.getInstance();
        assertEquals(firstInstance, secondInstance);
    }
    
    @Test
    public void SimulatedAnnealing_singletonInstanceTest() {
        TSPSolverService firstInstance = TSPSimulatedAnnealingSolverServiceImpl.getInstance();
        TSPSolverService secondInstance = TSPSimulatedAnnealingSolverServiceImpl.getInstance();
        assertEquals(firstInstance, secondInstance);
    }
    
    @Test
    public void ThreeOpt_singletonInstanceTest() {
        TSPSolverService firstInstance = TSPRandomThreeOptSolverServiceImpl.getInstance();
        TSPSolverService secondInstance = TSPRandomThreeOptSolverServiceImpl.getInstance();
        assertEquals(firstInstance, secondInstance);
    }
    
    @Test
    public void TwoOpt_singletonInstanceTest() {
        TSPSolverService firstInstance = TSPRandomTwoOptSolverServiceImpl.getInstance();
        TSPSolverService secondInstance = TSPRandomTwoOptSolverServiceImpl.getInstance();
        assertEquals(firstInstance, secondInstance);
    }
    
    @Test
    public void ACOThreeOpt_getKeyIdentifierTest() {
    	TSPSolverService instance = TSPACOThreeOptSolverServiceImpl.getInstance();
    	assertEquals(instance.getKeyIdentifier(), Constant.KEY_IDENTIFIER_ANT_COLONY_THREE_OPT);
    }
    
    @Test
    public void AntColonyOptimzation_getKeyIdentifierTest() {
    	TSPSolverService instance = TSPAntColonyOptimzationSolverServiceImpl.getInstance();
    	assertEquals(instance.getKeyIdentifier(), Constant.KEY_IDENTIFIER_ANT_COLONY);
    }
    
    @Test
    public void Christofides_getKeyIdentifierTest() {
    	TSPSolverService instance = TSPChristofidesSolverServiceImpl.getInstance();
    	assertEquals(instance.getKeyIdentifier(), Constant.KEY_IDENTIFIER_CHRISTOFIDES);
    }
    
    @Test
    public void SimulatedAnnealing_getKeyIdentifierTest() {
    	TSPSolverService instance = TSPSimulatedAnnealingSolverServiceImpl.getInstance();
    	assertEquals(instance.getKeyIdentifier(), Constant.KEY_IDENTIFIER_SIMULATED_ANNEALING);
    }
    
    @Test
    public void ThreeOpt_getKeyIdentifierTest() {
    	TSPSolverService instance = TSPRandomThreeOptSolverServiceImpl.getInstance();
    	assertEquals(instance.getKeyIdentifier(), Constant.KEY_IDENTIFIER_RANDOM_THREE_OPT);
    }
    
    @Test
    public void TwoOpt_getKeyIdentifierTest() {
    	TSPSolverService instance = TSPRandomTwoOptSolverServiceImpl.getInstance();
    	assertEquals(instance.getKeyIdentifier(), Constant.KEY_IDENTIFIER_RANDOM_TWO_OPT);
    }
    
    @Test
    public void ACOThreeOpt_getNameTest() {
    	TSPSolverService instance = TSPACOThreeOptSolverServiceImpl.getInstance();
    	assertEquals(instance.getName(), Constant.NAME_ANT_COLONY_THREE_OPT);
    }
    
    @Test
    public void AntColonyOptimzation_getNameTest() {
    	TSPSolverService instance = TSPAntColonyOptimzationSolverServiceImpl.getInstance();
    	assertEquals(instance.getName(), Constant.NAME_ANT_COLONY);
    }
    
    @Test
    public void Christofides_getNameTest() {
    	TSPSolverService instance = TSPChristofidesSolverServiceImpl.getInstance();
    	assertEquals(instance.getName(), Constant.NAME_CHRISTOFIDES);
    }
    
    @Test
    public void SimulatedAnnealing_getNameTest() {
    	TSPSolverService instance = TSPSimulatedAnnealingSolverServiceImpl.getInstance();
    	assertEquals(instance.getName(), Constant.NAME_SIMULATED_ANNEALING);
    }
    
    @Test
    public void ThreeOpt_getNameTest() {
    	TSPSolverService instance = TSPRandomThreeOptSolverServiceImpl.getInstance();
    	assertEquals(instance.getName(), Constant.NAME_RANDOM_THREE_OPT);
    }
    
    @Test
    public void TwoOpt_getNameTest() {
    	TSPSolverService instance = TSPRandomTwoOptSolverServiceImpl.getInstance();
    	assertEquals(instance.getName(), Constant.NAME_RANDOM_TWO_OPT);
    }
    
    @Test
    public void ACOThreeOpt_pointsNullTest() {
    	TSPSolverService instance = TSPACOThreeOptSolverServiceImpl.getInstance();
    	TSPPayload payload = new TSPPayload();
    	
    	assertThrows(NullPointerException.class, () -> {
    		instance.solve(null, 0, payload);
    	});
    }

    @Test
    public void AntColonyOptimzation_pointsNullTest() {
    	TSPSolverService instance = TSPAntColonyOptimzationSolverServiceImpl.getInstance();
    	TSPPayload payload = new TSPPayload();
    	
    	assertThrows(NullPointerException.class, () -> {
    		instance.solve(null, 0, payload);
    	});
    }
    
    @Test
    public void Christofides_pointsNullTest() {
    	TSPSolverService instance = TSPChristofidesSolverServiceImpl.getInstance();
    	TSPPayload payload = new TSPPayload();
    	
    	assertThrows(NullPointerException.class, () -> {
    		instance.solve(null, 0, payload);
    	});
    }
    
    @Test
    public void SimulatedAnnealing_pointsNullTest() {
    	TSPSolverService instance = TSPSimulatedAnnealingSolverServiceImpl.getInstance();
    	TSPPayload payload = new TSPPayload();
    	
    	assertThrows(NullPointerException.class, () -> {
    		instance.solve(null, 0, payload);
    	});
    }
    
    @Test
    public void ThreeOpt_pointsNullTest() {
    	TSPSolverService instance = TSPRandomThreeOptSolverServiceImpl.getInstance();
    	TSPPayload payload = new TSPPayload();
    	
    	assertThrows(NullPointerException.class, () -> {
    		instance.solve(null, 0, payload);
    	});
    }
    
    @Test
    public void TwoOpt_pointsNullTest() {
    	TSPSolverService instance = TSPRandomTwoOptSolverServiceImpl.getInstance();
    	TSPPayload payload = new TSPPayload();
    	
    	assertThrows(NullPointerException.class, () -> {
    		instance.solve(null, 0, payload);
    	});
    }
    
    @Test
    public void ACOThreeOpt_pointsEmptyTest() {
    	TSPSolverService instance = TSPACOThreeOptSolverServiceImpl.getInstance();
    	TSPPayload payload = new TSPPayload();
    	
    	assertThrows(NegativeArraySizeException.class, () -> {
    		instance.solve(Collections.emptyList(), 0, payload);
    	});
    }
    
    @Test
    public void AntColonyOptimzation_pointsEmptyTest() {
    	TSPSolverService instance = TSPAntColonyOptimzationSolverServiceImpl.getInstance();
    	TSPPayload payload = new TSPPayload();
    	
    	assertThrows(NegativeArraySizeException.class, () -> {
    		instance.solve(Collections.emptyList(), 0, payload);
    	});
    }
    
    @Test
    public void Christofides_pointsEmptyTest() {
    	TSPSolverService instance = TSPChristofidesSolverServiceImpl.getInstance();
    	TSPPayload payload = new TSPPayload();
    	
    	assertThrows(NegativeArraySizeException.class, () -> {
    		instance.solve(Collections.emptyList(), 0, payload);
    	});
    }
    
    @Test
    public void SimulatedAnnealing_pointsEmptyTest() {
    	TSPSolverService instance = TSPSimulatedAnnealingSolverServiceImpl.getInstance();
    	TSPPayload payload = new TSPPayload();
    	
    	assertThrows(NegativeArraySizeException.class, () -> {
    		instance.solve(Collections.emptyList(), 0, payload);
    	});
    }
    
    @Test
    public void ThreeOpt_pointsEmptyTest() {
    	TSPSolverService instance = TSPRandomThreeOptSolverServiceImpl.getInstance();
    	TSPPayload payload = new TSPPayload();
    	
    	assertThrows(NegativeArraySizeException.class, () -> {
    		instance.solve(Collections.emptyList(), 0, payload);
    	});
    }
    
    @Test
    public void TwoOpt_pointsEmptyTest() {
    	TSPSolverService instance = TSPRandomTwoOptSolverServiceImpl.getInstance();
    	TSPPayload payload = new TSPPayload();
    	
    	assertThrows(NegativeArraySizeException.class, () -> {
    		instance.solve(Collections.emptyList(), 0, payload);
    	});
    }
    
    @Test
    public void ACOThreeOpt_payloadNullTest() {
    	TSPSolverService instance = TSPACOThreeOptSolverServiceImpl.getInstance();
    	
    	assertThrows(NullPointerException.class, () -> {
    		instance.solve(Collections.emptyList(), 0, null);
    	});
    }
    
    @Test
    public void AntColonyOptimzation_payloadNullTest() {
    	TSPSolverService instance = TSPAntColonyOptimzationSolverServiceImpl.getInstance();
    	
    	assertThrows(NegativeArraySizeException.class, () -> {
    		instance.solve(Collections.emptyList(), 0, null);
    	});
    }
    
    @Test
    public void Christofides_payloadNullTest() {
    	TSPSolverService instance = TSPChristofidesSolverServiceImpl.getInstance();
    	
    	assertThrows(NegativeArraySizeException.class, () -> {
    		instance.solve(Collections.emptyList(), 0, null);
    	});
    }
    
    @Test
    public void SimulatedAnnealing_payloadNullTest() {
    	TSPSolverService instance = TSPSimulatedAnnealingSolverServiceImpl.getInstance();
    	
    	assertThrows(NegativeArraySizeException.class, () -> {
    		instance.solve(Collections.emptyList(), 0, null);
    	});
    }
    
    @Test
    public void ThreeOpt_payloadNullTest() {
    	TSPSolverService instance = TSPRandomThreeOptSolverServiceImpl.getInstance();
    	
    	assertThrows(NegativeArraySizeException.class, () -> {
    		instance.solve(Collections.emptyList(), 0, null);
    	});
    }
    
    @Test
    public void TwoOpt_payloadNullTest() {
    	TSPSolverService instance = TSPRandomTwoOptSolverServiceImpl.getInstance();
    	
    	assertThrows(NegativeArraySizeException.class, () -> {
    		instance.solve(Collections.emptyList(), 0, null);
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
    
    @Test
    public void TwoOpt_bigDataTestStrategy3Test() {
    	TwoOpt_dataTest(Constant.TEST_DATA_FILE_BIG, 3);
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
    			TSPACOThreeOptSolverServiceImpl.getInstance());
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
    			TSPAntColonyOptimzationSolverServiceImpl.getInstance());
	}

	private void Christofides_dataTest(String dataFileName) {
    	dataTest(
    			dataFileName, 
    			null, 
    			TSPChristofidesSolverServiceImpl.getInstance());
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
    			TSPSimulatedAnnealingSolverServiceImpl.getInstance());
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
    			TSPRandomThreeOptSolverServiceImpl.getInstance());
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
    			TSPRandomTwoOptSolverServiceImpl.getInstance());
	}
    
    private void dataTest(
    		String dataFileName,
    		TSPPayload payload,
    		TSPSolverService instance) {
    	CSVParserService csvParserService = CSVParserServiceImpl.getInstance();
    	List<Point> points = csvParserService.parsePoints(dataFileName);
    	
    	List<Point> tspTour = instance.solve(points, 0, payload);
        double tspTourCost = PointUtil.getTotalCost(tspTour);

    	PrimsMST primsMst = new PrimsMST(points);
        double mstCost = primsMst.getMstCost();
        
        double percentage = ((tspTourCost - mstCost)/mstCost) * 100;
        assertTrue(percentage < 100);
    }


	
}
