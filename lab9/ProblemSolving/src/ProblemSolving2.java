/*
Minimum cost to connect all points
You are given the coordinates (x, y) for N points.
The cost of connecting two points [xi, yi] and [xj, yj] is the euclidian distance between them.
Compute the minimum cost to connect all points. All points are connected if there is exactly one simple path between any two points.
The input is a text file with the format: the first line contains the value of N while the next N lines contain pairs of integer numbers
which are the x and y coordinates of the N points.

The output is the minimum cost to connect all the points.
 */


import java.io.IOException;
import java.io.File;
import java.util.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;


/**
 * WeightedEdge represents an edge in a weighted undirected graph
 */
class WeightedEdge implements Comparable<WeightedEdge>{
    private int either;
    private int other;
    private int w;


    public WeightedEdge(int either, int other, int w) {
        this.either = either;
        this.other = other;
        this.w = w;
    }

    public String toString() {
        return String.format("%d-%d %d", either, other, w);
    }

    public int either() {
        return either;
    }

    public int other(int v) {
        if (v == either)
            return other;
        else
            return either;
    }

    public int weight(){
        return w;
    }


    @Override
    public int compareTo(WeightedEdge that) {
        return Integer.compare(this.w, that.w);
    }

}

/**
 * Represents a simple undirected graph having a fixed number V of vertices (nodes).
 * Vertices are numbered either 0 other V-1
 * Edges can be added between existing vertices and have weights.
 */
interface IWeightedGraph {
    int getNrVertices();
    int getNrEdges();
    void addUndirectedEdge(int either, int other, int w);
    Iterable<Integer> nodesAdjacentTo(int node);
    Iterable<WeightedEdge> edgesAdjacentTo(int node);
    Iterable<WeightedEdge> allEdges();
    boolean hasVertex(int node);
    boolean hasEdge(int from, int to);
    void initFromFile(String file) throws IOException;
    void printGraph();
}

/**
 * Implementation of undirected weighted graph. Uses adjacency lists.
 */
class WeightedGraphAdjList implements IWeightedGraph {

    protected int V; // number of nodes
    protected int E; // number of edges

    /**
     * The graph is represented as an adjacency structure.
     * At g[i] there is the collection of nodes j that are adjacent to i
     */
    protected Map<Integer, Integer>[] g = null;

    /**
     * Constructs a graph having V nodes and no edges.
     *
     * @param V number of nodes
     */
    public WeightedGraphAdjList(int V) {
        this.V = V;
        E = 0;
        g = (Map<Integer, Integer>[]) new HashMap[V];
        for (int v = 0; v < V; v++) {
            g[v] = new HashMap<>() {
            };
        }
    }

    /**
     * Constructs a graph with V nodes and E edges, reading from file.
     *
     * The expected format of the file is:
     * On the first line, the number of nodes.
     * On the next lines, the edges given as node1 node2 weight
     *
     * @param file
     * @throws IOException
     */
    public WeightedGraphAdjList(String file) throws IOException {
        initFromFile(file);
    }

    public int getNrVertices() {
        return V;
    }

    public int getNrEdges() {
        return E;
    }

    public boolean hasVertex(int node) {
        if ((node >= 0) && (node < V))
            return true;
        else
            return false;
    }

    public boolean hasEdge(int from, int to) {
        if (hasVertex(from) && hasVertex(to))
            if (g[from].containsKey(to))
                return true;
        return false;
    }

    /**
     * Adds an edge in a undirected graph by adding two directed edges.
     */
    public void addUndirectedEdge(int either, int other, int w) {
        if (hasVertex(either) && hasVertex(other)) {
            g[either].put(other, w);
            g[other].put(either, w); // in undirected graphs add 2
            // entries in the adjacency structure
            E++; // increase edge counter
        }
    }

    /**
     * Returns all edges of an undirected graph. An edge i-j is
     * reported only once, when i<j.
     */
    public Iterable<WeightedEdge> allEdges() {
        Set<WeightedEdge> edgeSet = new HashSet<>();
        for (int node = 0; node < V; node++)
            for (Integer e : g[node].keySet()) {
                if (e> node) { // in undirected graphs make sure to
                    // list every edge only once
                    WeightedEdge ed = new WeightedEdge(node, e, g[node].get(e));
                    edgeSet.add(ed);
                }
            }
        return edgeSet;
    }


    /**
     *
     * @param node
     * @return all nodes adjacent to node
     */
    public Iterable<Integer> nodesAdjacentTo(int node) {
        if ((node >= 0) && (node < V))
            return g[node].keySet();
        return null;
    }

    /**
     *
     * @param node
     * @return all edges adjacent to node
     */

    public Iterable<WeightedEdge> edgesAdjacentTo(int node) {
        Set<WeightedEdge> edgeSet = new HashSet<WeightedEdge>();
        for (Integer e : g[node].keySet()) {
            WeightedEdge ed = new WeightedEdge(node, e, g[node].get(e));
            edgeSet.add(ed);
        }
        return edgeSet;
    }

    /**
     *
     * @param file name of file. Initializes graph with nodes and edges from file.
     * @throws IOException
     */
    public void initFromFile(String file) throws IOException {
        File input = new File(file);
        Scanner is = new Scanner(input);

        V = is.nextInt();
        E = 0;
        g = (Map<Integer, Integer>[]) new HashMap[V];
        for (int v = 0; v < V; v++) {
            g[v] = new HashMap<>();
        }

        System.out.println("Reading graph with " + V + " nodes from file "
                + file + " ...");

        int from, to;
        int weight;

        while (is.hasNext()) {
            from = is.nextInt();
            to = is.nextInt();
            weight = is.nextInt();
            addUndirectedEdge(from, to, weight);

        }
        is.close();
    }

    /**
     * Print all edges. Every undirected edge is print once.
     */
    public void printGraph() {
        for (int s = 0; s < V; s++) {
            for (Integer t : g[s].keySet()) {
                if (t>s) //print undirected edges only once
                    System.out.println(s + "-" +t+"   w="+g[s].get(t));
            }
        }
    }
}

interface IMST{
    public Iterable<WeightedEdge> mstEdgeList(IWeightedGraph G) ;
}

class PrimWithDistanceArray implements IMST {

    private static final int INF = Integer.MAX_VALUE;

    private boolean[] inMST;
    private int[] dist;
    private int[] parent;


    public PrimWithDistanceArray() {
    }

    public Iterable<WeightedEdge> mstEdgeList(IWeightedGraph G) {
        int V = G.getNrVertices();

        inMST = new boolean[V];
        dist = new int[V];
        parent = new int[V];

        primWithDistanceArray(G, 0); // fills out parent[v]

        // restores list of edges
        Queue<WeightedEdge> mst = new LinkedList<>();
        for (int u = 0; u < V; u++)
            if (parent[u] != -1) {  // u is not a root of a MST tree
                WeightedEdge e = new WeightedEdge(parent[u], u, dist[u]);
                mst.add(e);
            }
        return mst;
    }

    private void primWithDistanceArray(IWeightedGraph G, int src) {
        int V = G.getNrVertices();
        // Initialize arrays
        Arrays.fill(inMST, false);
        Arrays.fill(dist, INF - 1);
        Arrays.fill(parent, -1);

        dist[src] = 0;

        // Loop until all nodes are added to MST
        for (int count = 0; count < V; count++) {

            int minkey = INF;
            int v = -1; //search node v outside the MST
            // at minimum distance from some node in the MST
            for (int i = 0; i < V; i++) {
                if ((!inMST[i]) && (dist[i] < minkey)) {
                    minkey = dist[i];
                    v = i;
                }
            }

            inMST[v] = true; // add node v to  MST
            // the MST edge is parent[v]-v

            // Traverse all adjacent vertices of v
            for (WeightedEdge e : G.edgesAdjacentTo(v)) {
                int vv = e.other(v);
                int weight = e.weight();

                // If vv is not in MST and weight of (v,vv) is smaller than current dist of vv
                if ((!inMST[vv]) && (dist[vv] > weight)) {
                    // Update dist of vv
                    dist[vv] = weight;
                    parent[vv] = v;
                }
            }
        }

    }
}
public class ProblemSolving2 {
    public static void main(String[] args) throws IOException {

        Scanner scanner = new Scanner(new File("src/points_1000.txt"));
        int N = scanner.nextInt();
        int[][] points = new int[N][2];

        for (int i = 0; i < N; i++) {
            points[i][0] = scanner.nextInt();
            points[i][1] = scanner.nextInt();
        }
        scanner.close();

        WeightedGraphAdjList graph = new WeightedGraphAdjList(N);

        for (int i = 0; i < N; i++) { //add edges with distance
            for (int j = i + 1; j < N; j++) {
                int dx = points[i][0] - points[j][0];
                int dy = points[i][1] - points[j][1];
                int dist = (int) Math.round(Math.sqrt(dx * dx + dy * dy));
                graph.addUndirectedEdge(i, j, dist);
            }
        }

        PrimWithDistanceArray prim = new PrimWithDistanceArray();
        Iterable<WeightedEdge> mstEdges = prim.mstEdgeList(graph);

        int totalCost = 0;
        for (WeightedEdge e : mstEdges) {
            totalCost += e.weight();
        }

        System.out.println("Minimum cost to connect all points: " + totalCost);
    }

}