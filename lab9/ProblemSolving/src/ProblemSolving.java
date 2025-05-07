/*
Minimum cost to connect all points
You are given the coordinates (x, y) for N points.
The cost of connecting two points [xi, yi] and [xj, yj] is the euclidian distance between them.
Compute the minimum cost to connect all points. All points are connected if there is exactly one simple path between any two points.
The input is a text file with the format: the first line contains the value of N while the next N lines contain pairs of integer numbers
which are the x and y coordinates of the N points.

The output is the minimum cost to connect all the points.
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.Arrays;

class Point {
    int x, y;
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
}

public class ProblemSolving {

    public static double calculateDistance(Point a, Point b) {
        return Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2));
    }

    private static List<Point> initFromFile(String filename) throws IOException {
        List<Point> points = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filename));

        int n = Integer.parseInt(reader.readLine());
        for (int i = 0; i < n; i++) {
            String[] coords = reader.readLine().split(" ");
            int x = Integer.parseInt(coords[0]);
            int y = Integer.parseInt(coords[1]);
            points.add(new Point(x, y));
        }
        reader.close();
        return points;
    }

    public static double primMST(List<Point> points) {
        int n = points.size();
        boolean[] inMST = new boolean[n];
        double[] minDist = new double[n];
        Arrays.fill(minDist, Double.MAX_VALUE);
        minDist[0] = 0.0;

        double totalCost = 0.0;

        for (int i = 0; i < n; i++) {
            int u = -1;
            for (int j = 0; j < n; j++) {   //find the point with minimum distance
                if (!inMST[j] && (u == -1 || minDist[j] < minDist[u])) {
                    u = j;
                }
            }

            inMST[u] = true; //add u in mst + its distance to the cost
            totalCost += minDist[u];

            for (int v = 0; v < n; v++) {
                if (!inMST[v] && u != v) { //for each v which is not in the mst calculate distance betw it and the new added u
                    double dist = calculateDistance(points.get(u), points.get(v));
                    if (dist < minDist[v]) { //if the new distance calculated is smoller update it
                        minDist[v] = dist;
                    }
                }
            }
        }
        return totalCost;
    }

    public static void main(String[] args) {
        try {
            List<Point> points10 = initFromFile("src/points_10.txt");
            double minCost10 = primMST(points10);
            System.out.println("minimum cost to connect all 10 points: " + minCost10);

            List<Point> points100 = initFromFile("src/points_100.txt");
            double minCost100 = primMST(points100);
            System.out.println("minimum cost to connect all 100 points: "+ minCost100);

            List<Point> points1000 = initFromFile("src/points_1000.txt");
            double minCost1000 = primMST(points1000);
            System.out.println("minimum cost to connect all 1000 points: " + minCost1000);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}