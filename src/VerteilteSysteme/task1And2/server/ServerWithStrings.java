package task1And2.server;

import task1And2.support.Support;
import task1And2.tasks.Fibonacci;
import task1And2.tasks.StringReverser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerWithStrings {
    public static void main(String[] args) throws IOException {
        Support s = new Support();
        final int port = 7;
        final String IP = "127.0.0.1";

        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("ServerWithStrings is running at " + IP + " and listens on Port: " + port);
            Socket clientSocket = serverSocket.accept();
            PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String inputLine;

            while ((inputLine = input.readLine()) != null) {
                String[] arguments = s.splitInputArguments(inputLine);
                String command = arguments[0];
                if (command.equals("fibonacci")) {
                    output.println(fibonacci(arguments[1]));
                }
                if (command.equals("reverse")) {

                    output.println(reverse(arguments[1]));
                }
                else output.println("Wrong input.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int fibonacci(String number) {
        Fibonacci fibonacci = new Fibonacci();
        return fibonacci.getFibonacci(Integer.parseInt(number));
    }

    private static String reverse(String string) {
        StringReverser sr = new StringReverser();
        return sr.reverse(string);
    }
}
