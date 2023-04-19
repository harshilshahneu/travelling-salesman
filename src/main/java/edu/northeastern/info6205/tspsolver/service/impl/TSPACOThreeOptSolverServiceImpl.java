package edu.northeastern.info6205.tspsolver.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.northeastern.info6205.tspsolver.algorithm.antcolony.AntColonyOptimization;
import edu.northeastern.info6205.tspsolver.algorithm.christofides.Christofides;
import edu.northeastern.info6205.tspsolver.algorithm.opt.ThreeOpt;
import edu.northeastern.info6205.tspsolver.constant.Constant;
import edu.northeastern.info6205.tspsolver.model.Point;
import edu.northeastern.info6205.tspsolver.model.TSPPayload;
import edu.northeastern.info6205.tspsolver.service.TSPSolverService;
import edu.northeastern.info6205.tspsolver.util.HaversineDistanceUtil;

public class TSPACOThreeOptSolverServiceImpl implements TSPSolverService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TSPACOThreeOptSolverServiceImpl.class);

    private static TSPSolverService instance;

    private TSPACOThreeOptSolverServiceImpl() {
        LOGGER.info("Initialising the instance");
    }

    public static TSPSolverService getInstance() {
        if (instance == null) {
            instance = new TSPACOThreeOptSolverServiceImpl();
        }

        return instance;
    }

    @Override
    public String getKeyIdentifier() {
        return Constant.KEY_IDENTIFIER_ANT_COLONY_THREE_OPT;
    }

    @Override
    public String getName() {
        return Constant.NAME_ANT_COLONY_THREE_OPT;
    }

    @Override
    public List<Point> solve(
            List<Point> points,
            int startingPointIndex,
            TSPPayload payload) {
        LOGGER.info(
                "Ant Colony with 3-opt will solve for points size: {}, startingPointIndex: {}, payload: {}",
                points.size(),
                startingPointIndex,
                payload);

        TSPPayload.ThreeOptPayload threeOptPayload = payload.getThreeOptPayload();
        Map<Integer, Point> pointMap = new HashMap<>();
        for (Point point : points) {
            pointMap.put(Integer.parseInt(point.getId()), point);
        }

        Christofides christofides = new Christofides(points);
        List<Point> tour = christofides.solve();

        // Last point and first point are same in Christofides tour
        tour.remove(tour.size() - 1);

        int[] christofidesTour = tour.stream()
                .mapToInt(p -> Integer.parseInt(p.getId()))
                .toArray();

        int n = points.size();
        double[][] graph = new double[n][n];
        for (int i = 0; i < n; i++) {
            Point source = points.get(i);
            for (int j = i + 1; j < n; j++) {
                Point destination = points.get(j);
                double distance = HaversineDistanceUtil.haversine(destination, source);
                graph[i][j] = distance;
                graph[j][i] = distance;
            }
        }

        TSPPayload.AntColonyOptimazationPayload antColonyOptimazationPayload = payload.getAntColonyOptimazationPayload();

        AntColonyOptimization optimization = new AntColonyOptimization(
                graph,
                christofidesTour,
                antColonyOptimazationPayload.getNumberOfAnts(),
                antColonyOptimazationPayload.getPhermoneExponent(),
                antColonyOptimazationPayload.getHeuristicExponent(),
                antColonyOptimazationPayload.getPhermoneEvaporationRate(),
                antColonyOptimazationPayload.getPhermoneDepositFactor(),
                antColonyOptimazationPayload.getNumberOfIterations(),
                antColonyOptimazationPayload.getMaxImprovementIterations());

        int[] path = optimization.runACO();

        List<Point> result = new ArrayList<>();
        for (int node : path) {
            result.add(pointMap.get(node));
        }

        Point firstPoint = pointMap.get(startingPointIndex);
        int firstPointIndex = result.indexOf(firstPoint);
        LOGGER.trace("firstPointIndex: {}", firstPointIndex);

        if (firstPointIndex != -1) {
            int rotations = firstPointIndex;
            LOGGER.info("will rotate the tour by rotations: {}", rotations);
            Collections.rotate(result, -rotations);
        } else {
            LOGGER.info("ACO gave correct tours, no need to rotate array");
        }

        result.add(result.get(0));

        ThreeOpt threeOpt = new ThreeOpt(result, threeOptPayload.getStrategy(), threeOptPayload.getBudget());
        threeOpt.improve();
        List<Point> improvedTour = threeOpt.getImprovedTour();
        return improvedTour;
    }

}
