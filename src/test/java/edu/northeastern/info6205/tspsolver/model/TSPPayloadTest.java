package edu.northeastern.info6205.tspsolver.model;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;

import edu.northeastern.info6205.tspsolver.model.TSPPayload.AntColonyOptimazationPayload;
import edu.northeastern.info6205.tspsolver.model.TSPPayload.SimulatedAnnealingPayload;
import edu.northeastern.info6205.tspsolver.model.TSPPayload.ThreeOptPayload;
import edu.northeastern.info6205.tspsolver.model.TSPPayload.TwoOptPayload;

public class TSPPayloadTest {

    @Test
    public void testGetSetTwoOptPayload() {
        TwoOptPayload twoOpt = new TwoOptPayload();
        twoOpt.setBudget(500);
        twoOpt.setStrategy(1);

        TSPPayload payload = new TSPPayload();
        payload.setTwoOptPayload(twoOpt);
        
        assertEquals(twoOpt, payload.getTwoOptPayload());
    }

    @Test
    public void testGetSetThreeOptPayload() {
        ThreeOptPayload threeOpt = new ThreeOptPayload();
        threeOpt.setBudget(1000);
        threeOpt.setStrategy(2);
        
        TSPPayload payload = new TSPPayload();
        payload.setThreeOptPayload(threeOpt);
        
        assertEquals(threeOpt, payload.getThreeOptPayload());
    }

    @Test
    public void testGetSetSimulatedAnnealingPayload() {
        SimulatedAnnealingPayload annealingPayload = new SimulatedAnnealingPayload();
        annealingPayload.setCoolingRate(0.9);
        annealingPayload.setFinalTemperature(0.001);
        annealingPayload.setMaxIteration(10000);
        annealingPayload.setStartingTemperature(100);
        
        TSPPayload payload = new TSPPayload();
        payload.setSimulatedAnnealingPayload(annealingPayload);
        assertEquals(annealingPayload, payload.getSimulatedAnnealingPayload());
    }

    @Test
    public void testGetSetAntColonyOptimazationPayload() {
        AntColonyOptimazationPayload antColonyOptimazationPayload = new AntColonyOptimazationPayload();
        antColonyOptimazationPayload.setHeuristicExponent(1.0);
        antColonyOptimazationPayload.setMaxImprovementIterations(50);
        antColonyOptimazationPayload.setNumberOfAnts(20);
        antColonyOptimazationPayload.setNumberOfIterations(500);
        antColonyOptimazationPayload.setPhermoneDepositFactor(2.0);
        antColonyOptimazationPayload.setPhermoneEvaporationRate(0.5);
        antColonyOptimazationPayload.setPhermoneExponent(2.0);

        TSPPayload payload = new TSPPayload();
        payload.setAntColonyOptimazationPayload(antColonyOptimazationPayload);
        assertEquals(antColonyOptimazationPayload, payload.getAntColonyOptimazationPayload());
    }
}
