/*
2. Depth First Search sequence. Read a undirected unweighted graph from a file. Read a vertex number from standard input,
representing the source for DFS. Print the sequence of vertices, in the order they are resulting from DFS.
 */

import java.io.IOException;
import java.io.File;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * SimpleEdge represents an edge in a
 * undirected, unweighted graph
 */
class SimpleEdge {
    private int either; // either of this edge's ends
    private int other;  // the other end

    public SimpleEdge(int either, int other) {
        this.either = either;
        this.other = other;
    }

    /**
     * @return either of this edge's vertices
     */
    public int either() {
        return either;
    }

    /**
     * @param v - one of the edge's vertices
     * @return the other vertex of this edge
     */
    public int other(int v) {
        if (v == either)
            return other;
        else
            return either;
    }
}

/**
 * Represents a simple undirected unweighted graph
 * having a fixed number V of vertices (nodes).
 * Vertices are numbered from 0 to V-1.
 * Edges can be added between existing vertices.
 */
interface ISimpleGraph {
    int getNrVertices();

    int getNrEdges();

    void addUndirectedEdge(int either, int other);

    Iterable<Integer> nodesAdjacentTo(int node);

    Iterable<SimpleEdge> edgesAdjacentTo(int node);

    Iterable<SimpleEdge> allEdges();

    boolean hasVertex(int node);

    boolean hasEdge(int either, int other);

    void initFromFile(String file) throws IOException;

    void printGraph();
}

/**
 * Implementation of the simple undirected unweighted graph
 * with an Adjacency Matrix.
 */
class SimpleGraphMatrix implements ISimpleGraph {

    protected int V; // number of nodes
    protected int E; // number of edges


    // The adjacency matrix holding the graph
    protected int[][] g = null;

    /**
     * Constructs a graph having V nodes and no edges.
     * The nodes are by default from 0 to V-1.
     *
     * @param V - number of nodes
     */
    public SimpleGraphMatrix(int V) {
        this.V = V;
        E = 0;
        g = new int[V][V];

        for (int i = 0; i < V; i++)
            for (int j = 0; j < V; j++) {
                g[i][j] = 0;
            }
    }

    /**
     * Constructs a graph with V nodes and E edges, reading from file.
     * The expected format of the file is:
     * On the first line, the number of nodes.
     * On the next lines, the undirected edges given as node1 node2.
     *
     * @param file - name of file
     * @throws IOException
     */
    public SimpleGraphMatrix(String file) throws IOException {
        initFromFile(file);
    }

    /**
     * @return - number of vertices(nodes)
     */
    public int getNrVertices() {
        return V;
    }

    /**
     * @return - number of edges
     */
    public int getNrEdges() {
        return E;
    }

    /**
     * @param node an integer number
     * @return - true if node represents a vertex (value between 0 and V-1)
     */
    public boolean hasVertex(int node) {
        if ((node >= 0) && (node < V))
            return true;
        else
            return false;
    }

    /**
     * @param either - an integer number representing a node
     * @param other  - another integer number representing another node
     * @return - true if there is an edge between the two nodes
     */
    public boolean hasEdge(int either, int other) {
        if (hasVertex(either) && hasVertex(other))
            if (g[either][other] == 1)
                return true;
        return false;
    }


    /**
     * Adds an edge in an undirected graph by adding two directed edges.
     * In the matrix there will be edges in both directions between nodes
     * either and other.
     *
     * @param either- an integer number representing a node
     * @param other   - another integer number representing another node
     */
    public void addUndirectedEdge(int either, int other) {
        if (hasVertex(either) && hasVertex(other)) {
            g[either][other] = 1;
            g[other][either] = 1; // in undirected graphs add 2
            // entries in the adjacency structure
            E++; // increase edge counter
        }
    }

    /**
     * Returns all edges of an undirected graph.
     * An edge node1-node2 is reported only once, when node1<node2.
     */
    public Iterable<SimpleEdge> allEdges() {
        Set<SimpleEdge> edgeSet = new HashSet<SimpleEdge>();
        for (int node1 = 0; node1 < V; node1++)
            for (int node2 = node1; node2 < V; node2++)
                if (g[node1][node2] == 1) { // in undirected graphs make sure to
                    // list every edge only once
                    SimpleEdge ed = new SimpleEdge(node1, node2);
                    edgeSet.add(ed);
                }
        return edgeSet;
    }

    /**
     * @param node - an integer representing a vertex
     * @return - an iterable over all vertices adjacent to node
     */
    public Iterable<Integer> nodesAdjacentTo(int node) {
        Set<Integer> nodeSet = new HashSet<>();
        if ((node >= 0) && (node < V)) {
            for (int i = 0; i < V; i++)
                if (g[node][i] == 1)
                    nodeSet.add(i);
            return nodeSet;
        }
        return null;
    }

    /**
     * @param node - an integer representing a vertex
     * @return - an iterable over all edges adjacent to node
     */
    public Iterable<SimpleEdge> edgesAdjacentTo(int node) {
        Set<SimpleEdge> edgeSet = new HashSet<SimpleEdge>();
        for (int i = 0; i < V; i++)
            if (g[node][i] == 1) {
                SimpleEdge ed = new SimpleEdge(node, i);
                edgeSet.add(ed);
            }
        return edgeSet;
    }


    /**
     * Read graph from file
     *
     * @param file - name of file
     * @throws IOException
     */
    public void initFromFile(String file) throws IOException {
        File input = new File(file);
        Scanner is = new Scanner(input);

        V = is.nextInt();
        E = 0;
        g = new int[V][V];
        for (int i = 0; i < V; i++) {
            for (int j = 0; j < V; j++)
                g[i][j] = 0;
        }

        System.out.println("Reading graph with " + V + " nodes from file "
                + file + " ...");

        while (is.hasNext()) {
            int either = is.nextInt();
            int other = is.nextInt();

            addUndirectedEdge(either, other);
        }
    }

    /**
     * Displays undirected unweighted graph.
     * Undirected edges i-j are shown only when i<j
     */
    public void printGraph() {
        System.out.println("Vertices (nodes): 0 .. " + (V - 1));
        System.out.println("Edges: E=" + E);
        for (int i = 0; i < V; i++)
            for (int j = i; j < V; j++)
                if (g[i][j] == 1) //print undirected edges only once
                    System.out.println(i + "-" + j);
    }
}

/**
 * Implements DepthFirstSearch on simple undirected graph
 */
public class DepthFirstSearch {

    enum Color {WHITE, GREY, BLACK}
    private ISimpleGraph G;
    private Color[] color;      // color[v] = status of node v
    private int[] parent;      // parent[v] = previous node on DFS search

    /**
     * Performs the DFS search starting from the source vertex {@code s}
     * in the undirected graph {@code G}.
     * Only vertices in the same connected component with s will be reached.
     * Vertices reached by the DFS will have color BLACK.
     *
     * @param G the graph
     * @param s the source vertex
     * @throws IllegalArgumentException unless {@code 0 <= s < V}
     */
    public DepthFirstSearch(ISimpleGraph G, int s) {

        this.G = G;
        int V = G.getNrVertices();

        if (!G.hasVertex(s))
            throw new IllegalArgumentException("vertex " + s + " is not between 0 and " + (V - 1));

        color = new Color[V];
        parent = new int[V];

        for (int v = 0; v < G.getNrVertices(); v++) {
            color[v] = Color.WHITE;
        }
        dfs(G, s);
    }

    // depth-first search from a single source
    private void dfs(ISimpleGraph G, int source) {
        color[source] = Color.GREY;   // start exploring source
        System.out.print(source + " ");
        for (int v : G.nodesAdjacentTo(source)) {
            if (color[v] == Color.WHITE) {  // first time encounter v
                parent[v] = source;
                dfs(G, v);                  // start exploring v
            }
        }
        color[source] = Color.BLACK;     // finished exploring source
    }

    /**
     * Is there a path between the source vertex s, that has been
     * set in constructor, and vertex v?
     *
     * @param v the vertex
     * @return true if there is a path, false otherwise
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public boolean isConnectedTo(int v) {
        int V = G.getNrVertices();

        if (!G.hasVertex(v))
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V - 1));

        return (color[v] == Color.BLACK);
    }

    public static void main(String[] args) {
        try {
            SimpleGraphMatrix graph = new SimpleGraphMatrix("src/demo1.txt");
            graph.printGraph();

            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter the source vertex for DFS: ");
            int source = scanner.nextInt();

            System.out.println("DFS traversal starting from vertex " + source + ":");
            new DepthFirstSearch(graph, source);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
