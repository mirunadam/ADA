/*
Read data from file of integers in 2 ways: using Scanner and using BufferedReader.
See examples using the two ways of reading from files: FileInputV1.java and FileInputV2.java.
Measure and compare the time for reading a big file of 1M Integers RandomIntegers_1M.txt in these 2 ways.
Which way is better?
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

class FileInputV1 {
    // Uses BufferedReader
    // Reads integer numbers from a text file. First line contains number of values.
    // Returns an array containing the numbers
    public static int[] readIntegers(String filename) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));

            // Read  number of integers n on first line
            int n = Integer.parseInt(reader.readLine());

            // Initialize the array to hold the integers
            int[] integers = new int[n];

            // Read the next n lines containing the integers
            for (int i = 0; i < n; i++) {
                integers[i] = Integer.parseInt(reader.readLine());
            }
            return integers;
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error reading file " + filename);
            e.printStackTrace();
            return null;  // Return for error
        }
    }
}

class FileInputV2 {
    // Uses Scanner
    // Reads integer numbers from a text file.  First line contains number of values.
    // Returns an array containing the numbers
    public static int[] readIntegers(String filename) {
        try {
            Scanner scanner = new Scanner(new File(filename));

            // Read number of integers n on first line
            int n = Integer.parseInt(scanner.nextLine());

            // Initialize the array to hold the integers
            int[] integers = new int[n];

            // Read the next n lines containing the integers
            for (int i = 0; i < n; i++) {
                if (scanner.hasNextInt()) {
                    integers[i] = scanner.nextInt();
                } else {
                    System.out.println("Error: Not enough numbers");
                    return null;
                }
            }
            return integers;

        } catch (FileNotFoundException e) {
            System.out.println("Error reading file " + filename);
            e.printStackTrace();
            return null;  // Return for error
        }
    }
}

class Stopwatch {
    private long startTime;

    public Stopwatch(){
        startTime = System.nanoTime();
    }

    // Restart the stopwatch
    public void start() {
        startTime = System.nanoTime();
    }

    // Get the elapsed time in seconds
    public double getElapsedTime() {
        long endTime = System.nanoTime();
        return (endTime - startTime) / 1000000000.0;  // nanoseconds to seconds
    }

}

public class ReadingData {
    public static void main(String[] args) {
        //File myFile = new File("D:\\An 2\\SEMESTRU 2\\ADA\\homeworks ADA\\ReadInt\\src\\RandomIntegers_1M.txt");
        //String filename = myFile.getName();
        //String filename = args[1];
        String filename = "src/RandomIntegers_1M.txt";

        Stopwatch timerV1 = new Stopwatch();
        // some code to be measured
        int [] ArrayV1 = FileInputV1.readIntegers(filename);
        // end measured code
        double timeV1 = timerV1.getElapsedTime();
        System.out.println("Measured runtime for V1 (buffered reader) in seconds:"+timeV1);

        Stopwatch timerV2 = new Stopwatch();
        // some code to be measured
        int [] ArrayV2 = FileInputV2.readIntegers(filename);
        // end measured code
        double timeV2 = timerV2.getElapsedTime();
        System.out.println("Measured runtime for V2 (Scanner) in seconds:"+timeV2);

        if(timeV1 > timeV2){
            System.out.println("Runtime V1 (buffered reader) is greater than the runtime for V2 (Scanner)");
            System.out.println("V2 (Scanner) is better");
        } else{
            System.out.println("Runtime V2 (Scanner) is greater than the runtime for V1 (buffered reader)");
            System.out.println("V1 (buffered reader) is better");
        }
    }
}