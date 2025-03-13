package cs250.hw2;

import java.util.Random;
import java.util.TreeSet;
import java.util.LinkedList;

public class Memory {

    private static volatile int volatileLoopVariable;
    public static void main(String[] args) {
        
        int size = Integer.parseInt(args[0]); 
        int experiments = Integer.parseInt(args[1]); 
        int seed = Integer.parseInt(args[2]); 

        
        long regularTime = 0;
        long volatileTime = 0;
        long regularSum = 0;
        long volatileSum = 0;

        
        for (int i = 0; i < experiments; i++) {
            
            long startTime = System.nanoTime();
            regularSum = runRegularLoop(size);
            long endTime = System.nanoTime();
            regularTime += (endTime - startTime);

            
            startTime = System.nanoTime();
            volatileSum = runVolatileLoop(size);
            endTime = System.nanoTime();
            volatileTime += (endTime - startTime);
        }

        
        double avgRegularTime = (regularTime / (double) experiments) / 1_000_000_000.0;
        double avgVolatileTime = (volatileTime / (double) experiments) / 1_000_000_000.0;

        

        Integer[] array = new Integer[size];
        Random random = new Random(seed);
        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt();
        }

        long timeKnown = 0;
        long timeRandom = 0;
        long sum = 0;

        for (int i = 0; i < experiments; i++) {
            int knownIndex = random.nextInt(size / 10);
            long startTime = System.nanoTime();
            int knownElement = array[knownIndex];
            long endTime = System.nanoTime();
            timeKnown += (endTime - startTime);
            sum += knownElement;

            int randomIndex = size - size / 10 + random.nextInt(size / 10);
            startTime = System.nanoTime();
            int randomElement = array[randomIndex];
            endTime = System.nanoTime();
            timeRandom += (endTime - startTime);
            sum += randomElement;
        }

        double avgTimeKnown = (double) timeKnown / experiments;
        double avgTimeRandom = (double) timeRandom / experiments;

        TreeSet<Integer> treeSet = new TreeSet<>();
        LinkedList<Integer> linkedList = new LinkedList<>();
        for (int i = 0; i < size; i++) {
            treeSet.add(i);
            linkedList.add(i);
        }

        long treeSetTime = 0;
        long linkedListTime = 0;

        for (int i = 0; i < experiments; i++) {
            int randomNumber = random.nextInt(size);

            
            long startTime = System.nanoTime();
            treeSet.contains(randomNumber);
            long endTime = System.nanoTime();
            treeSetTime += (endTime - startTime);

            
            startTime = System.nanoTime();
            linkedList.contains(randomNumber);
            endTime = System.nanoTime();
            linkedListTime += (endTime - startTime);
        }

        double avgTreeSetTime = (double) treeSetTime / experiments;
        double avgLinkedListTime = (double) linkedListTime / experiments;



        // Task 1 output
        System.out.println("Task 1");
        System.out.printf("Regular: %.5f seconds%n", avgRegularTime);
        System.out.printf("Volatile: %.5f seconds%n", avgVolatileTime);
        System.out.printf("Avg regular sum: %.2f%n", (double) regularSum / experiments);
        System.out.printf("Avg volatile sum: %.2f%n", (double) volatileSum / experiments);


        // TYask 2 output
        System.out.println("Task 2");
        System.out.printf("Avg time to access known element: %.2f nanoseconds%n", avgTimeKnown);
        System.out.printf("Avg time to access random element: %.2f nanoseconds%n", avgTimeRandom);
        System.out.printf("Sum: %.2f%n", (double) sum);

        //Task 3 output
        System.out.println("Task 3");
        System.out.printf("Avg time to find in set: %.2f nanoseconds%n", avgTreeSetTime);
        System.out.printf("Avg time to find in list: %.2f nanoseconds%n", avgLinkedListTime);
    }



    



    
    private static long runRegularLoop(int size) {
        long runningTotal = 0;
        for (int i = 0; i < size; i++) {
            if (i % 2 == 0) {
                runningTotal += i;
            } else {
                runningTotal -= i;
            }
        }
        return runningTotal;
    }

    
    private static long runVolatileLoop(int size) {
        
        long runningTotal = 0;
        for (volatileLoopVariable = 0; volatileLoopVariable < size; volatileLoopVariable++) {
            if (volatileLoopVariable % 2 == 0) {
                runningTotal += volatileLoopVariable;
            } else {
                runningTotal -= volatileLoopVariable;
            }
        }
        return runningTotal;
    }
}

