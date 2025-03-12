package cs250.hw2;

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

        
        double avgRegularTime = (regularSum / (double) experiments) / 1_000_000_000.0;
        double avgVolatileTime = (volatileTime / (double) experiments) / 1_000_000_000.0;

        // Output results
        System.out.println("Task 1");
        System.out.printf("Regular: %.5f seconds%n", avgRegularTime);
        System.out.printf("Volatile: %.5f seconds%n", avgVolatileTime);
        System.out.printf("Avg regular sum: %.2f%n", (double) regularSum / experiments);
        System.out.printf("Avg volatile sum: %.2f%n", (double) volatileSum / experiments);
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

