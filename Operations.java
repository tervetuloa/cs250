public static void main(String[] args) {
    System.out.print("Task 1");
    if (args.length == 3)
        {
            System.out.println("Correct number of arguments given.");
        }   
    else if (args.length > 3 || args.length < 3)
        {
            System.out.println("Incorrect number of arguments have been provided. Program Terminating!");
            return;
        }
}