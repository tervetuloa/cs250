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
        System.out.println("Task 2");
        
        String num1Type = whichNumberSystem(args[0]);
        String num2Type = whichNumberSystem(args[1]);
        String num3Type = whichNumberSystem(args[3]);

        System.out.println(args[0] +  "=" + num1Type);
        System.out.println(args[1] +  "=" + num2Type);
        System.out.println(args[2] +  "=" + num3Type);
            
        
    }
    public static String whichNumberSystem(String number) {
        if (number.startsWith("0b")) {
            return "Binary";
        }
        else if (number.startsWith("0x")) {
            return "Hexadecimal";
        }
        else {
            return "Decimal";
        }
    }


}