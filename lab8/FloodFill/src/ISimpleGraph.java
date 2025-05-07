

import java.io.IOException;


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
public interface ISimpleGraph {
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