package cs250.hw3;

import java.io.*;
import java.net.*;
import java.util.Random;

public class TCPClient {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage: java cs250.hw3.TCPClient <server-host> <server-port>");
            return;
        }

        String serverHost = args[0];
        int serverPort;

        try {
            serverPort = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.err.println("Port number must be an integer");
            return;
        }

        try {
            
            Socket socket = new Socket(serverHost, serverPort);
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            
            int numberOfMessages = in.readInt();
            int seed = in.readInt();

            System.out.println("Received config");
            System.out.println("number of messages = " + numberOfMessages);
            System.out.println("seed = " + seed);

            
            Random random = new Random(seed);

            
            long senderSum = 0;
            int numOfSentMessages = 0;

            System.out.println("Starting to send messages to server...");

            
            for (int i = 0; i < numberOfMessages; i++) {
                int randomNumber = random.nextInt();
                out.writeInt(randomNumber);
                senderSum += randomNumber;
                numOfSentMessages++;
            }

            System.out.println("Finished sending messages to server.");
            System.out.println("Total messages sent: " + numOfSentMessages);
            System.out.println("Sum of messages sent: " + senderSum);

            
            in.close();
            out.close();
            socket.close();

        } catch (UnknownHostException e) {
            System.err.println("Unknown host: " + serverHost);
        } catch (IOException e) {
            System.err.println("I/O error: " + e.getMessage());
        }
    }
}