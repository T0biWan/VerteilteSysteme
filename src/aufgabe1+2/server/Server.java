package server;

import tasks.Fibonacci;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws IOException {
        final int port = 7;

        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server is running and listens on Port: " + port);
            Socket clientSocket = serverSocket.accept();
            PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String inputLine;

            while ((inputLine = input.readLine()) != null) {
                String[] arguments = inputLine.split(" "); // TODO Erst sicherheitsabfrage machen!
                if (arguments[0].equals("fibonacci")) { // TODO Arguments[0] in variable auslagern
                    output.println(fibonacci(arguments[1]));
                } else output.println("NOPE!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int fibonacci(String number) {
        Fibonacci fibonacci = new Fibonacci();
        return fibonacci.getFibonacci(Integer.parseInt(number));
    }

    private static boolean argsIsLessThen(int numberOfArguments, String[] args) {
        return args.length < numberOfArguments;
    }

    private static void shutDownIfNotEnoughArguments(String[] args) {
        if (argsIsLessThen(1, args)) {
            System.err.println("Missing Argument(s) in String[] args");
            System.exit(1);
        }
    }
}
