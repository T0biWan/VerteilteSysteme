package task1And2.server;

import task1And2.support.Support;
import task1And2.tasks.Fibonacci;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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
    private static int minimum = 0;
    private static int maximum = 20;

    public static void main(String[] args) throws IOException {
        setIpAndPort(args);
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Server is running at " + IP + " and listens on Port: " + port);

        while (serverIsRunning) {
            try (
                    Socket clientSocket = serverSocket.accept();
                    DataInputStream clientInput = new DataInputStream(clientSocket.getInputStream());
                    DataOutputStream clientOutput = new DataOutputStream(clientSocket.getOutputStream());
            ) {
                int number = clientInput.readInt();
                if (numberIsWithinBoundaries(number, minimum, maximum)) clientOutput.writeInt(fibonacci(number));
                else clientOutput.writeInt(-2);
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

    private static boolean numberIsWithinBoundaries(int number, int minimum, int maximum) {
        return !(number < minimum || number > maximum);
    }

}
