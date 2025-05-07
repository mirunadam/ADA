/*
A pixel map is a two-dimensional array of size N*N,  whose value represent colors; the individual entries in the array are called pixels.
A connected region in a pixel map is a connected subset of pixels that all have the same color, where two pixels are
considered adjacent if they are immediate horizontal or vertical neighbours. The flood fill operation, commonly represented by a paint can
in raster-graphics editing software, changes every pixel in a connected region to a new color;
the input to the operation consists of the indices i and j of one pixel in the target  region and the new color.

The program reads the pixel map from a text file with following format:
- first line contains value of N

- the second line contains the coordinates (i,j) of the starting pixel and the new color value
- the next N lines contain each line N integer values representing the colors of the N pixels on that line.

Your solution must print out the new pixel map with the new colors resulting after the flood fill operation.
¬use some kind of bfs
 */

import java.io.IOException;
import java.io.File;
import java.util.*;

public class FloodFill {
    public int N;
    public int[][] pixel_map;
    int x, y;
    int new_color;

    public FloodFill(String file) throws IOException {
        initFromFile(file);
        floodFill(x, y, new_color);
    }
    private boolean isValid(int x, int y) {
        return x >= 0 && x < N && y >= 0 && y < N;
    }
    public void floodFill(int x, int y, int new_color){
        int original_color = pixel_map[x][y];
        Queue<int[]> q = new LinkedList<>(); //queue with the coord of pixels that need to be proccesed
        q.add(new int[]{x, y}); //add starting coord first

        int[] dx = {-1, 1, 0, 0};
        int[] dy = {0, 0, -1, 1};

        while (!q.isEmpty()) {
            int[] position = q.remove(); //retrieves the next position coord
            int i = position[0];
            int j = position[1];
            pixel_map[i][j] = new_color;

            for(int d=0; d<4; d++){ //iterates through neighbors
                int newi = i+dx[d];
                int newj = j+dy[d];

                if(isValid(newi,newj) && pixel_map[newi][newj] == original_color){
                    q.add(new int[]{newi,newj});
                }
            }
        }
    }
    public void initFromFile(String file) throws IOException {
        File input = new File(file);
        Scanner is = new Scanner(input);
        N = is.nextInt();
        x = is.nextInt();
        y = is.nextInt();
        new_color = is.nextInt();
        pixel_map = new int[N][N];

        for(int i = 0; i < N; i++){
            for(int j = 0; j < N; j++){
                pixel_map[i][j] = is.nextInt();
            }
        }
    }

    public void printNewPixels() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                System.out.print(pixel_map[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        try{
            FloodFill floodFill = new FloodFill("src/pixels1.txt");
            floodFill.printNewPixels();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}