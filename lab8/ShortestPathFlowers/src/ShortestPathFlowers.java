/*
A terrain is in the form of a plain with rocks and flowers. A map of the terrain is given as a grid with N x N cells.
If a cell contains a rock it is marked with character '|', if it contains a flower it is marked with the character '*',
otherwise it is plain and marked with character '.'.  You are given a starting position in the grid
(coordinates xs, ys with values in interval 0 - N-1).

Find the shortest path from the starting position to a flower or print that there is no such path.

A path is a sequence of cells that contain no rocks. From a current cell the path can continue in any of the 8 neighbor
 cells if they contain no rocks. The length of a path is the number of visited cells of this path
 (the count includes start and end cell).

The program reads the map from a text file with following format:
- first line contains N
- second line contains xs ys the starting point coordinates (in interval 0.. N-1)
- the next N lines contain strings of N characters, that can be symbols for Rock, Flower or Plain.

Your solution must find:
- if there is a path to a flower
- the length of the shortest path to a flower
- the sequence of cells which compose the shortest path to a flower

Example input: path_flowers1.txt  For this input data the result is 3 - the shortest path to a flower visits 3 cells
(counting starting cell and final cell as well)
 */

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class ShortestPathFlowers {
    public int N;
    public char[][] terrain;
    int xs, ys;

    public ShortestPathFlowers(String file) throws IOException {
        initFromFile(file);
        findPath(xs, ys);
    }

    private boolean isValid(int x, int y, boolean[][] visited) {
        return x>=0 && x<N && y>=0 && y<N && !visited[x][y] && terrain[x][y]!='|';
    }

    private void findPath(int xs, int ys){
        if(terrain[xs][ys] == '*'){
            System.out.println("Shortest Path to Flower is 1: "+"[" + xs + ", " + ys + "]");
            return;
        }

        Queue<int[]> q = new LinkedList<>();
        q.add(new int[]{xs, ys});

        int[][] parent = new int[N][N];
        for (int i = 0; i < N; i++){
            for (int j = 0; j < N; j++){
                parent[i][j] = -1;
            }
        }

        boolean[][] visited = new boolean[N][N];
        visited[xs][ys] = true;

        int[] dx = {-1, 1, 0, 0, -1, 1, -1, 1};
        int[] dy = {0, 0, -1, 1, -1, -1, 1, 1};

        while(!q.isEmpty()){
            int[] current_pos = q.remove();
            int i = current_pos[0];
            int j = current_pos[1];

            if(terrain[i][j] == '*'){
                LinkedList<int[]> path = new LinkedList<>();
                path.addFirst(new int[]{i, j}); //add first bcs we go backward
                while(parent[i][j] != -1){
                    int prev = parent[i][j];
                    int pi =prev/N;
                    int pj=prev%N;
                    path.addFirst(new int[]{pi,pj});
                    i = pi;
                    j = pj;
                }
                System.out.println("Shortest Path to Flower is: "+path.size());
                for(int[] pos: path){
                    System.out.println("[" + pos[0] + ", " + pos[1] + "]");
                }
                return;
            }

            for(int d=0; d<8; d++){
                int newX = i+dx[d];
                int newY = j+dy[d];

                if(isValid(newX, newY, visited)){
                    visited[newX][newY] = true;
                    parent[newX][newY] = i*N +j;

                    q.add(new int[]{newX, newY});
                }
            }

        }
        System.out.println("No path found");
    }

    public void initFromFile(String file) throws IOException {
        File input = new File(file);
        Scanner is = new Scanner(input);
        N = is.nextInt();
        xs = is.nextInt();
        ys = is.nextInt();
        terrain = new char[N][N];

        is.nextLine(); //consume newline
        for(int i = 0; i < N; i++){
            terrain[i] = is.nextLine().toCharArray();
        }
    }
    public static void main(String[] args) {
        try{
            ShortestPathFlowers flowers = new ShortestPathFlowers("src/path_flowers.txt");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}