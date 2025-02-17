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
        
        
        String num1Type = whichNumberSystem(args[0]);
        String num2Type = whichNumberSystem(args[1]);
        String num3Type = whichNumberSystem(args[2]);

        System.out.println("Task 2");

        System.out.println(args[0] +  "=" + num1Type);
        System.out.println(args[1] +  "=" + num2Type);
        System.out.println(args[2] +  "=" + num3Type);

        System.out.println("Task 3");
        
        System.out.println(args[0] + "=" + isValidNumber(args[0], num1Type));
        System.out.println(args[0] + "=" + isValidNumber(args[1], num2Type));
        System.out.println(args[0] + "=" + isValidNumber(args[3], num3Type));


        if(isValidNumber(args[0], num1Type) == false || isValidNumber(args[1], num2Type) == false || isValidNumber(args[2], num3Type) == false) {
            return;
        }

            
        
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


    public static boolean validateBinary(String number)
    {
        for (int i = 0; i < number.length(); ++i)
        {
            char c = number.charAt(i);

            if (c < '0' || c > '1'){ 
                return false; 
            }
            
        }
        return true;
    }

    public static boolean validateDecimal(String number)
    {   
        
        for (int i = 0; i < number.length(); i++)
        {
            char c = number.charAt(i);

            if (c < '0' || c > '9'){ 
                return false; 
            }
            
        }
        return true;
    }

    public static boolean validateHexadecimal(String number)
    {
        for (int i = 0; i < number.length(); ++i)
        {
            char c = number.charAt(i);

            if ((c < '0' || c > '9') && (c < 'A' || c > 'F') && (c < 'a' || c > 'f')){
                return false;
            }
            
        }
        return true;
    }

    public static boolean isValidNumber(String number, String type) {
        if(type.equals("Binary")) {
            return validateBinary(number);
        }
        if(type.equals("Decimal")) {
            return validateDecimal(number);
        }
        if(type.equals("Hexadecimal")) {
            return validateHexadecimal(number);
        }
        return false;

    }
}