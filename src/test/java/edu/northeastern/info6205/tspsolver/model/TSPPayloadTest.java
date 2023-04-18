package edu.northeastern.info6205.tspsolver.model;

import org.junit.Test;
import static org.junit.Assert.*;

public class TSPPayloadTest {

    @Test
    public void testGetSetTwoOptPayload() {
        TSPPayload payload = new TSPPayload();
        TSPPayload.TwoOptPayload twoOpt = new TSPPayload.TwoOptPayload();
        twoOpt.setBudget(500L);
        twoOpt.setStrategy(1);
        payload.setTwoOptPayload(twoOpt);
        assertEquals(twoOpt, payload.getTwoOptPayload());
    }

    @Test
    public void testGetSetThreeOptPayload() {
        TSPPayload payload = new TSPPayload();
        TSPPayload.ThreeOptPayload threeOpt = new TSPPayload.ThreeOptPayload();
        threeOpt.setBudget(1000L);
        threeOpt.setStrategy(2);
        payload.setThreeOptPayload(threeOpt);
        assertEquals(threeOpt, payload.getThreeOptPayload());
    }

    @Test
    public void testGetSetSimulatedAnnealingPayload() {
        TSPPayload payload = new TSPPayload();
        TSPPayload.SimulatedAnnealingPayload sa = new TSPPayload.SimulatedAnnealingPayload();
        sa.setCoolingRate(0.9);
        sa.setFinalTemperature(0.001);
        sa.setMaxIteration(10000);
        sa.setStartingTemperature(100);
        payload.setSimulatedAnnealingPayload(sa);
        assertEquals(sa, payload.getSimulatedAnnealingPayload());
    }

    @Test
    public void testGetSetAntColonyOptimazationPayload() {
        TSPPayload payload = new TSPPayload();
        TSPPayload.AntColonyOptimazationPayload aco = new TSPPayload.AntColonyOptimazationPayload();
        aco.setHeuristicExponent(1.0);
        aco.setMaxImprovementIterations(50);
        aco.setNumberOfAnts(20);
        aco.setNumberOfIterations(500);
        aco.setPhermoneDepositFactor(2.0);
        aco.setPhermoneEvaporationRate(0.5);
        aco.setPhermoneExponent(2.0);
        payload.setAntColonyOptimazationPayload(aco);
        assertEquals(aco, payload.getAntColonyOptimazationPayload());
    }
}
