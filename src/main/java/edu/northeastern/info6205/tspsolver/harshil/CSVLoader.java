package edu.northeastern.info6205.tspsolver.harshil;

import edu.northeastern.info6205.tspsolver.model.Point;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CSVLoader {
    static boolean removeDuplicates = true;

    // Loads nodes from Kaggle CSV ~ 84k rows.
    public static List<Point> loadNodesFromCSV(String filepath) throws IOException {
        List<Point> nodes = new ArrayList<>();
        Set<String> uniqueLocations = new HashSet<>();
        BufferedReader reader = new BufferedReader(new FileReader(filepath));
        String line = reader.readLine(); // read the header line
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            if(!parts[6].equals("No Location")) {
                double lat = Double.parseDouble(parts[5]);
                double lng = Double.parseDouble(parts[4]);
                String id = parts[0];
                String location = lat + "," + lng;
                if (!uniqueLocations.contains(location) || !removeDuplicates) {
                    uniqueLocations.add(location);
                    nodes.add(new Point(id, lat, lng));
                }
            }
        }
        reader.close();
        return nodes;
    }

    // Loads nodes from Prof's sample CSV ~150 rows .
    public static List<Point> loadNodesFromCleanData(String filepath) throws IOException {
        List<Point> nodes = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filepath));
        String line = reader.readLine(); // read the header line
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            double lat = Double.parseDouble(parts[2]);
            double lng = Double.parseDouble(parts[1]);
            String id = parts[0];
            nodes.add(new Point(id, lat, lng));
        }
        reader.close();
        return nodes;
    }
    public static void printNodes(List<Point> nodes) {
        for(int i = 0; i < nodes.size(); i++) {
            //print the distance between all the nodes
            for(int j = 0; j < nodes.size(); j++) {
                if(i != j) {
                    System.out.print("From : " + nodes.get(i).getId() + " To: " + nodes.get(j).getId());
                    System.out.println(" " + HaversineDistance.haversine(nodes.get(i), nodes.get(j)));
                }
            }
        }
    }
}