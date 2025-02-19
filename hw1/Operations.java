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
        System.out.println(args[1] + "=" + isValidNumber(args[1], num2Type));
        System.out.println(args[2] + "=" + isValidNumber(args[2], num3Type));

        System.out.println("Task 4");

        System.out.println("Start=" + args[0] +",Binary=" + convertToBinary(args[0]) + ",Decimal=" + convertToDecimal(args[0]) + ",Hexadecimal=" + convertToHex(args[0]));
        System.out.println("Start=" + args[1] +",Binary=" + convertToBinary(args[1]) + ",Decimal=" + convertToDecimal(args[1]) + ",Hexadecimal=" + convertToHex(args[1]));
        System.out.println("Start=" + args[2] +",Binary=" + convertToBinary(args[2]) + ",Decimal=" + convertToDecimal(args[2]) + ",Hexadecimal=" + convertToHex(args[2]));



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
        if (number.length() <= 2) {
            return false;
        }
        number = number.substring(2);
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
        if (number.length() == 0) {
            return false;
        }
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
        if (number.length() <= 2) {
            return false;
        }
        number = number.substring(2);
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
//conversion methodsss

    public static String convertToBinary(String number)
    {
        if (number.startsWith("0b")) {
            return number;

        }

        else if (number.startsWith("0x")) {
            String hexDec = convertToDecimal(number);
            String result = "";
            int temp = 0;
            int decimalValue = stringToInt(hexDec);
            for (int i = (decimalValue); i > 0; i/=2) {
                temp = i % 2;
                result = temp + result;
            }
            return "0b" + result;
        }

        else {
            String result = "";
            int temp = 0;
            int decimalValue = stringToInt(number);
            for (int i = stringToInt(number); i > 0; i/=2) {
                temp = i % 2;
                result = temp + result;
            }
            return "0b" + result;
        }
    }
        public static String convertToDecimal(String number) {
        if (number.startsWith("0b")) {

            number = number.substring(2);

            

            int result = 0;
            int power = 0;

            for(int i = number.length()-1; i >= 0; --i) {
                int digit = number.charAt(i) - '0';
                
                result += digit * Math.pow(2, power);

                
                ++power;
            }

            return intToString(result);

        }
        
        else if (number.startsWith("0x")) {
            number = number.substring(2);

            

            int result = 0;
            int power = 0;

            for(int i = number.length()-1; i >= 0; --i) {
                char charDigit = number.charAt(i);
                int digit;

                if(charDigit >= '0' && charDigit <= '9'){
                    digit = charDigit - '0';
                }
                else if(charDigit >= 'A' && charDigit <= 'F'){
                    digit = 10 + (charDigit - 'A');
                }
                else {
                    digit = 10 +(charDigit - 'a');
                }

                
                result += digit * Math.pow(16, power);

                
                ++power;
            }
            return intToString(result);
        }

        else {
            
            return number;
        }
    }

public static String convertToHex(String number) {
    if (number.startsWith("0b")) {
        
        String decimal = convertToDecimal(number);
        int decimalValue = stringToInt(decimal);

        
        String result = "";
        int temp = 0;
        char[] hexLetters = {'a', 'b', 'c', 'd', 'e', 'f'};
        for (int i = decimalValue; i > 0; i /= 16) {
            temp = i % 16;
            if (temp < 10) {
                result = temp + result;
            } 
            else {
                result = hexLetters[temp - 10] + result;
            }
        }
        return "0x" + result;
    } 
    else if (number.startsWith("0x")) {
        return number;
    } 
    else {
        int decimalValue = stringToInt(number);
        String result = "";
        int temp = 0;
        char[] hexLetters = {'a', 'b', 'c', 'd', 'e', 'f'};
        for (int i = decimalValue; i > 0; i /= 16) {
            temp = i % 16;
            if (temp < 10) {
                result = temp + result;
            } 
            else {
                result = hexLetters[temp - 10] + result;
            }
        }
        return "0x" + result;
    }
}


    //parsing methods because yeahhhhhhhhhhh

    public static String intToString(int bruh) {
        if (bruh == 0) {
            return "0";
        }

        String result = "";
        while (bruh > 0) {
            int digit = bruh % 10;
            result = (char)('0' + digit) + result;
            bruh /= 10;
        }

        return result;

    }

    public static int stringToInt(String bruh) {
        int result = 0;
        for (int i = 0; i < bruh.length(); i++) {
            char chr = bruh.charAt(i);
            int digit = chr - '0';
            result = result * 10 + digit;
        }

        return result;
    }
    
}

