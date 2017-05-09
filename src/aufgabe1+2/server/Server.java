package server;

import support.Support;
import tasks.Fibonacci;
import tasks.StringReverser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Tobias on 09.05.2017.
 */
public class Server {
    private static Support s = new Support();
    private static String defaultIP = "127.0.0.1";
    private static int defaultPort = 7;
    private static String IP;
    private static int port;
    private static boolean serverIsRunning = true;

    public static void main(String[] args) throws IOException {
        setIpAndPort(args);
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Server is running at " + IP + " and listens on Port: " + port);

        while (serverIsRunning) {
            try (
                    Socket clientSocket = serverSocket.accept();
                    PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true);
                    BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            ) {
//                int number = Integer.parseInt(input.readLine());
//                output.println(fibonacci(number));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void setIpAndPort(String[] args) {
        if (s.argsIsLessThen(2, args)) {
            IP = defaultIP;
            port = defaultPort;
        } else {
            IP = args[0];
            port = Integer.parseInt(args[1]);
        }
    }

    private static int fibonacci(int number) {
        Fibonacci fibonacci = new Fibonacci();
        return fibonacci.getFibonacci(number);
    }

}
