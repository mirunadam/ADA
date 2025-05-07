/*
Implement Prim's algorithm to determine the Minimum Spanning Forest of a weighted undirected graph with N nodes,
the graph can be a disconnected graph. Use adiacency lists to represent the graph and a priority queue based on
heaps as auxiliary structure. You can use a priority queue from standard colections.

The input is given in a text file with following format:
The first line contains the total number of nodes, N.
The next lines contain the edges, given as triplets of three numbers. Number1 and number2 are integers in the range 0..N-1,
node ID's and number3 is a positive number representing the weight of the edge.

The output of the program is the list of the MST that compose the forest. For each MST of the forest give
its cost and the list of edges which compose the MST.
 */


import java.io.IOException;
import java.io.File;
import java.util.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.PriorityQueue;
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

class PrimWithJavaPriorityQueue implements IMST {

    class NodePriority implements Comparable<NodePriority> {
        int node;
        int priority;

        NodePriority(int node, int priority) {
            this.node = node;
            this.priority = priority;
        }

        @Override
        public int compareTo(NodePriority other) {
            return Integer.compare(this.priority, other.priority);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof NodePriority)
                return this.node == ((NodePriority) obj).node;
            return false;
        }
    }

    private static final int INF = Integer.MAX_VALUE;


    private boolean[] inMST;
    private int[] parent;
    private int[] key;



    public Iterable<WeightedEdge> mstEdgeList(IWeightedGraph G) {
        int V = G.getNrVertices();

        inMST = new boolean[V];
        key = new int[V];
        parent = new int[V];

        Arrays.fill(inMST, false);
        Arrays.fill(key, INF);
        Arrays.fill(parent, -1);

        for (int v = 0; v < V; v++)     // needed if graph is disconnected
            if (!inMST[v])               // restart Prim once in every connected component
                doPrimWithJavaPQ(G, v);  // fills out parent array

        //restore edges that form the mst
        Queue<WeightedEdge> mst = new LinkedList<>(); // edges in the MST
        for (int u = 0; u < V; u++)
            if (parent[u] != -1) {  // u is not a root of a MST tree
                WeightedEdge e = new WeightedEdge(parent[u], u, key[u]);
                mst.add(e);
            }
        return mst;
    }
    private void doPrimWithJavaPQ(IWeightedGraph G, int src) {
        // Priority queue to store vertices being processed
        PriorityQueue<NodePriority> pq = new PriorityQueue<>();

        // Insert source vertex into priority queue and set its priority key to 0
        key[src] = 0;
        pq.add(new NodePriority(src, key[src]));

        // Loop until priority queue becomes empty
        while (!pq.isEmpty()) {
            int v = pq.remove().node; // Extract vertex with minimum key

            inMST[v] = true; // Include vertex v in MST

            // Traverse all adjacent vertices of v
            for (WeightedEdge e : G.edgesAdjacentTo(v)) {
                int vv = e.other(v);
                int weight = e.weight();

                // If vv is not in MST and weight of (v,vv) is smaller than current key of vv
                if (!inMST[vv] && key[vv] > weight) {
                    // Update key of vv - realize equivalent of decreaseKey
                    pq.remove(new NodePriority(vv, key[vv]));
                    key[vv] = weight;
                    pq.add(new NodePriority(vv, key[vv]));
                    parent[vv] = v;  //
                }
            }
        }
        // in this point we have the MST of the connected component of src
    }

}
class PrimForest {
    private static final int INF = Integer.MAX_VALUE;

    class NodePriority implements Comparable<NodePriority> {
        int node;
        int priority;

        NodePriority(int node, int priority) {
            this.node = node;
            this.priority = priority;
        }

        public int compareTo(NodePriority o) {
            return Integer.compare(this.priority, o.priority);
        }
    }

    public List<List<WeightedEdge>> mstForest(IWeightedGraph G) {
        int V = G.getNrVertices();
        boolean[] visited = new boolean[V];
        List<List<WeightedEdge>> forest = new ArrayList<>();

        for (int i = 0; i < V; i++) {
            if (!visited[i]) { //for each unvisited node
                forest.add(primMST(G, i, visited)); //use prim to find the mst and add to the forest
            }
        }
        return forest;
    }

    private List<WeightedEdge> primMST(IWeightedGraph G, int i, boolean[] visited) { //O(E log E)
        PriorityQueue<WeightedEdge> pq = new PriorityQueue<>(); //to select the min weight edge
        List<WeightedEdge> mst = new ArrayList<>();

        visited[i] = true;
        for (WeightedEdge e : G.edgesAdjacentTo(i)) { //all edges connected to i
            pq.add(e);
        }

        while (!pq.isEmpty()) {
            WeightedEdge e = pq.remove(); //extract the min edge
            int u = e.either(), v = e.other(u);

            if (visited[u] && visited[v]){
                continue; //if both nodes of the edge are visited skip the edge
            }

            mst.add(e); //if one is not visited add the edge

            int newNode; //see exactly which node is new and was not visited
            if (visited[u]) {
                newNode = v;
            } else {
                newNode = u;
            }
            visited[newNode] = true;

            for (WeightedEdge adj : G.edgesAdjacentTo(newNode)) { //all edges connected to the new node
                int neighbor = adj.other(newNode); //takes the other node of the edge
                if (!visited[neighbor]) { //add only if it wasnt visided already to avoid cycle
                    pq.add(adj);
                }
            }
        }
        return mst;
    }
}

public class PrimAdjStructForest{
    public static void main(String[] args) {
        try{
            WeightedGraphAdjList graph = new WeightedGraphAdjList("src/wdisconn.txt");
            PrimForest prim = new PrimForest();
            List<List<WeightedEdge>> forests = prim.mstForest(graph);

            int count = 1;
            for (List<WeightedEdge> mst : forests) {
                int cost = mst.stream().mapToInt(WeightedEdge::weight).sum();
                System.out.println("MST: " + count + ", cost=" + cost);
                for (WeightedEdge e : mst) {
                    System.out.println("   " + e);
                }
                count++;
            }

        }catch (IOException e){
            e.printStackTrace();
        }
    }
}