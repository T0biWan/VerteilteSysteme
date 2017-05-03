package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class CommandLineInterface {
    public static void main(String[] args) throws IOException {
        String IP;
        int port;
        String prompt = "$>\t";
        String space = "\t";

        if (argsIsLessThen(2, args)) { // todo auslagenr in Methode, dazu müssen variablen static sein und außerhalb der main liegen
            IP = "127.0.0.1";
            port = 7;
        } else {
            IP = args[0];
            port = Integer.parseInt(args[1]);
        }

        try {
            Socket socket = new Socket(IP, port);
            System.out.println("Connected to Server on IP: " + IP + " and Port: " + port);

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
            String userInput;
            System.out.print(prompt);

            while ((userInput = stdIn.readLine()) != null) {
                String[] arguments = userInput.split(" "); // TODO Erst sicherheitsabfrage machen!
                arguments[0] = arguments[0].toLowerCase();
                if (arguments[0].equals("f") || arguments[0].equals("fibonacci")) { // TODO Arguments[0] in variable auslagernm
                    out.println("fibonacci "+ arguments[1]);
//                    System.out.println("echo: " + in.readLine());
                    System.out.println(space + "Fibonacci of " + arguments[1] + " is " + in.readLine());
                    System.out.println();
                }
                System.out.print(prompt);
            }
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + IP);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + IP);
            System.exit(1);
        }
    }

    private static boolean argsIsLessThen(int numberOfArguments, String[] args) {
        return args.length < numberOfArguments;
    }

    private static void shutDownIfNotEnoughArguments(String[] args) {
        if (argsIsLessThen(2, args)) {
            System.err.println("Missing Argument(s) in String[] args");
            System.exit(1);
        }
    }
}
