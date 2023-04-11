package edu.northeastern.info6205.tspsolver.harshil;

// in a given Eulerian or Semi-Eulerian Graph
import java.util.ArrayList;
import java.util.List;

// An Undirected graph using
// adjacency list representation
public class FluerysAlgorithm {

    private int vertices; // No. of vertices
    private ArrayList<Integer>[] adj; // adjacency list
    private ArrayList<int[]> result = new ArrayList<>(); // list of edges

    // Constructor
    public FluerysAlgorithm(int numOfVertices)
    {
        // initialise vertex count
        this.vertices = numOfVertices;

        // initialise adjacency list
        initGraph();
    }

    // utility method to initialise adjacency list
    @SuppressWarnings("unchecked") private void initGraph()
    {
        adj = new ArrayList[vertices];
        for (int i = 0; i < vertices; i++) {
            adj[i] = new ArrayList<>();
        }
    }

    // add edge u-v
    public void addEdge(Integer u, Integer v)
    {
        adj[u].add(v);
        adj[v].add(u);
    }

    // This function removes edge u-v from graph.
    private void removeEdge(Integer u, Integer v)
    {
        adj[u].remove(v);
        adj[v].remove(u);
    }

    /* The main function that print Eulerian Trail.
       It first finds an odd degree vertex (if there
       is any) and then calls printEulerUtil() to
       print the path */
    public void printEulerTour()
    {
        // Find a vertex with odd degree
        Integer u = 0;
        for (int i = 0; i < vertices; i++) {
            if (adj[i].size() % 2 == 1) {
                u = i;
                break;
            }
        }

        // Print tour starting from oddv
        printEulerUtil(u);
        System.out.println();
    }

    // Print Euler tour starting from vertex u
    private void printEulerUtil(Integer u)
    {
        // Recur for all the vertices adjacent to this
        // vertex
        for (int i = 0; i < adj[u].size(); i++) {
            Integer v = adj[u].get(i);
            // If edge u-v is a valid next edge
            if (isValidNextEdge(u, v)) {
                System.out.print(u + "-" + v + " ");
                result.add(new int[] { u, v });
                // This edge is used so remove it now
                removeEdge(u, v);
                printEulerUtil(v);
            }
        }
    }

    //return the list of edges
    public List<int[]> getResult()
    {
        return result;
    }

    // The function to check if edge u-v can be
    // considered as next edge in Euler Tout
    private boolean isValidNextEdge(Integer u, Integer v)
    {
        // The edge u-v is valid in one of the
        // following two cases:

        // 1) If v is the only adjacent vertex of u
        // ie size of adjacent vertex list is 1
        if (adj[u].size() == 1) {
            return true;
        }

        // 2) If there are multiple adjacents, then
        // u-v is not a bridge Do following steps
        // to check if u-v is a bridge
        // 2.a) count of vertices reachable from u
        boolean[] isVisited = new boolean[this.vertices];
        int count1 = dfsCount(u, isVisited);

        // 2.b) Remove edge (u, v) and after removing
        //  the edge, count vertices reachable from u
        removeEdge(u, v);
        isVisited = new boolean[this.vertices];
        int count2 = dfsCount(u, isVisited);

        // 2.c) Add the edge back to the graph
        addEdge(u, v);
        return (count1 > count2) ? false : true;
    }

    // A DFS based function to count reachable
    // vertices from v
    private int dfsCount(Integer v, boolean[] isVisited)
    {
        // Mark the current node as visited
        isVisited[v] = true;
        int count = 1;
        // Recur for all vertices adjacent to this vertex
        for (int adj : adj[v]) {
            if (!isVisited[adj]) {
                count = count + dfsCount(adj, isVisited);
            }
        }
        return count;
    }

    // Driver program to test above function
    public static void main(String a[])
    {
        // Let us first create and test
        // graphs shown in above figure
        // Graph g1 = new Graph(4);
        // g1.addEdge(0, 1);
        // g1.addEdge(0, 2);
        // g1.addEdge(1, 2);
        // g1.addEdge(2, 3);
        // g1.printEulerTour();

    }
}