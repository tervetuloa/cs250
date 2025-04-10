package cs250.hw3;

import java.io.*;
import java.net.*;
import java.util.Random;

public class TCPServer {
    public static void main(String[] args) {
        if (args.length != 3) {
            System.err.println("Usage: java cs250.hw3.TCPServer <port-number> <seed> <number-Of-Messages>");
            return;
        }

        int portNumber;
        int seed;
        int numberOfMessages;

        try {
            portNumber = Integer.parseInt(args[0]);
            if (portNumber <= 1024 || portNumber > 65535) {
                System.err.println("Port number must be > 1024 and <= 65535");
                return;
            }
        } catch (NumberFormatException e) {
            System.err.println("Port number must be an integer");
            return;
        }

        try {
            seed = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.err.println("Seed must be an integer");
            return;
        }

        try {
            numberOfMessages = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            System.err.println("Number of messages must be an integer");
            return;
        }

        try {
            ServerSocket serverSocket = new ServerSocket(portNumber);
            InetAddress localHost = InetAddress.getLocalHost();
            System.out.println("IP Address: " + localHost.getHostName() + "/" + localHost.getHostAddress());
            System.out.println("Port Number " + portNumber);
            
            Random random = new Random(seed);
            System.out.println("waiting for client...");
            
            Socket clientSocket1 = serverSocket.accept();
            DataOutputStream out1 = new DataOutputStream(clientSocket1.getOutputStream());
            DataInputStream in1 = new DataInputStream(clientSocket1.getInputStream());
            
            Socket clientSocket2 = serverSocket.accept();
            DataOutputStream out2 = new DataOutputStream(clientSocket2.getOutputStream());
            DataInputStream in2 = new DataInputStream(clientSocket2.getInputStream());
            
            System.out.println("Clients Connected!");
            System.out.println("Sending config to clients...");
            
            int seed1 = random.nextInt();
            int seed2 = random.nextInt();
            
            out1.writeInt(numberOfMessages);
            out1.writeInt(seed1);
            
            out2.writeInt(numberOfMessages);
            out2.writeInt(seed2);
            
            String client1Host = clientSocket1.getInetAddress().getHostName();
            String client2Host = clientSocket2.getInetAddress().getHostName();
            
            System.out.println(client1Host + " " + seed1);
            System.out.println(client2Host + " " + seed2);
            
            System.out.println("Finished sending config to clients.");

            long[] sums = {0, 0};
            int[] counts = {0, 0};

            Thread t1 = new Thread(() -> {
                try {
                    for (int i = 0; i < numberOfMessages; i++) {
                        int num = in1.readInt();
                        out2.writeInt(num);
                        sums[0] += num;
                        counts[0]++;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            Thread t2 = new Thread(() -> {
                try {
                    for (int i = 0; i < numberOfMessages; i++) {
                        int num = in2.readInt();
                        out1.writeInt(num);
                        sums[1] += num;
                        counts[1]++;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            t1.start();
            t2.start();
            t1.join();
            t2.join();

            System.out.println(client1Host);
            System.out.println("\tMessages received: " + counts[1]);
            System.out.println("\tSum received: " + sums[1]);
            System.out.println(client2Host);
            System.out.println("\tMessages received: " + counts[0]);
            System.out.println("\tSum received: " + sums[0]);

            in1.close();
            out1.close();
            clientSocket1.close();
            in2.close();
            out2.close();
            clientSocket2.close();
            serverSocket.close();
            
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }
}

    

