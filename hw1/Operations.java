package cs250.hw1;


public class Operations {

    public static void main(String[] args) {
        System.out.println("Task 1");
        if (args.length == 3)
            {
                System.out.println("Correct number of arguments given.");
            }   
        else 
            {
                System.out.println("Incorrect number of arguments have been provided. Program Terminating!");
                return;
            }
    }
}